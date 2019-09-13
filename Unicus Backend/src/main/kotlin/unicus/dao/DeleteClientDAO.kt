package unicus.dao

import com.mongodb.MongoTimeoutException
import mu.KotlinLogging
import org.litote.kmongo.deleteOne
import org.springframework.stereotype.Repository
import unicus.entity.Client

@Repository
class DeleteClientDAO : DAO() {

    fun deleteClient(clientId: String) {
        val clientCollection = collection.getCollection<Client>(CollectionType.CLIENT)
        try {
            clientCollection.deleteOne("{ relatienummer: '$clientId'}")
        } catch (e: MongoTimeoutException) {
            KotlinLogging.logger {}.error {
                errorDBConnectionMessage
            }
        }
    }
}
