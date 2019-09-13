package unicus.dao

import com.mongodb.MongoTimeoutException
import mu.KotlinLogging
import org.litote.kmongo.replaceOne
import org.springframework.stereotype.Repository
import unicus.dto.FieldsToUpdateDTO
import unicus.entity.Client
import java.lang.reflect.Field

@Repository
class UpdateClientDAO : DAO() {

    private val validityDAO = ClientValidityDAO()

    fun mergeDataFromClients(clientToUpdateId: String,
                             clientToCopyFromId: String,
                             fieldsToUpdateDTO: FieldsToUpdateDTO): Client {

        val clientToUpdate = validityDAO.getClientById(clientToUpdateId)
        val clientToCopyFrom = validityDAO.getClientById(clientToCopyFromId)
        val newClient = copyDataFromClientBToClientA(clientToUpdate, clientToCopyFrom, fieldsToUpdateDTO)

        updateClientInDB(clientToUpdateId, newClient)
        return newClient
    }

    fun updateClientInDB(clientToUpdateId: String, newClient: Client) {

        try {
            newClient.relatienummer = clientToUpdateId
            collection.getCollection<Client>(CollectionType.CLIENT)
                    .replaceOne("{ relatienummer: '$clientToUpdateId'}", newClient)

        } catch (e: MongoTimeoutException) {
            KotlinLogging.logger {}.error {
                errorDBConnectionMessage
            }
        }
    }

    fun copyDataFromClientBToClientA(clientA: Client,
                                     clientB: Client,
                                     fieldsToUpdateDTO: FieldsToUpdateDTO): Client {

        if (clientB.dossiers != null) {
            if (clientA.dossiers == null) clientA.dossiers = mutableListOf()
            clientA.dossiers?.addAll(clientB.dossiers!!)
        }

        lateinit var fieldClientToUpdate: Field
        lateinit var fieldClientToDelete: Field

        for (fieldnameDTO in fieldsToUpdateDTO.getFieldsToUpdate()) {
            fieldClientToUpdate = clientA.javaClass.getField(fieldnameDTO)
            fieldClientToDelete = clientA.javaClass.getField(fieldnameDTO)

            fieldClientToUpdate.set(clientA, fieldClientToDelete.get(clientB))
        }

        clientA.brpZegel = null
        return clientA
    }
}
