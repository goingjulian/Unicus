package unicus.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import unicus.dao.ClientToDTODAO
import unicus.dao.ClientValidityDAO
import unicus.dao.DeleteClientDAO
import unicus.dao.GetClientByWildcardDAO
import unicus.dao.UpdateClientDAO
import unicus.dto.ClientDTO
import unicus.dto.FieldsToUpdateDTO
import unicus.exception.ClientIdNotValidException
import unicus.exception.ClientIdsAreTheSameException
import java.text.ParseException
import java.text.SimpleDateFormat

@Service
class ClientService {
    val startIndexCutPhone = 0
    val endIndexCutPhone = 3

    @Autowired
    private lateinit var clientValidityDAO: ClientValidityDAO

    @Autowired
    private lateinit var clientToDTODAO: ClientToDTODAO

    @Autowired
    private lateinit var deleteClientDAO: DeleteClientDAO

    @Autowired
    private lateinit var updateClientDAO: UpdateClientDAO

    @Autowired
    private lateinit var getClientByWildcardDAO: GetClientByWildcardDAO

    private val logger = KotlinLogging.logger {}

    @Throws(ClientIdNotValidException::class)
    fun getClient(clientId: String): ClientDTO {
        if (!clientValidityDAO.doesClientExist(clientId)) {
            logger.warn {
                "A clientID was supplied that does not exist in de database (id: $clientId)"

            }
            throw ClientIdNotValidException()
        }
        val client = clientValidityDAO.getClientById(clientId)

        logger.info {
            "client with name ${client.voornamen} ${client.geslachtsnaam} found with id: $clientId"
        }

        return clientToDTODAO.convertClientToDTO(client)
    }



    @Throws(ClientIdNotValidException::class, ClientIdsAreTheSameException::class)
    fun updateClient(clientToUpdateId: String,
                     clientToCopyFromId: String,
                     fieldsToUpdateDTO: FieldsToUpdateDTO): ClientDTO {

        if (clientToUpdateId == clientToCopyFromId) {
            logger.warn {
                "Client supplied identical clientID's to merge (id to update: $clientToUpdateId, " +
                        "id to delete: $clientToCopyFromId)"
            }
            throw ClientIdsAreTheSameException()
        }

        if (!clientValidityDAO.doesClientExist(clientToUpdateId) ||
                !clientValidityDAO.doesClientExist(clientToCopyFromId)) {
            logger.warn {
                "Client supplied one or more clientID's that do not exist in de database " +
                        "(id to update: $clientToUpdateId, id to delete $clientToCopyFromId). "
            }
            throw ClientIdNotValidException()
        }

        val newClient = updateClientDAO.mergeDataFromClients(clientToUpdateId, clientToCopyFromId, fieldsToUpdateDTO)

        logger.info {
            "client $clientToUpdateId succesfully merged with client $clientToCopyFromId"
        }

        deleteClientDAO.deleteClient(clientToCopyFromId)

        logger.info {
            "client with id $clientToCopyFromId has been removed"
        }

        return clientToDTODAO.convertClientToDTO(newClient)
    }

    @Throws(ClientIdNotValidException::class)
    fun getClientsByWildcard(searchCriteria: String): MutableList<ClientDTO> {
        val filteredSearchCriteria = filterSearchCriteria(searchCriteria)

        val clients = getClientByWildcardDAO.getClientsByWildcard(filteredSearchCriteria)

        logger.info {
            "${clients.size} results found for search criteria: '$filteredSearchCriteria'"
        }

        return clients.toMutableList()
    }

    fun filterSearchCriteria(searchCriteria: String): String {
        var formattedSearchCriteria = searchCriteria.toLowerCase().replace(Regex("([^a-zA-Z0-9@_\\-.])+"), "")

        if (isValidEUDate(formattedSearchCriteria)) {
            formattedSearchCriteria = formatDateFromEUToUS(formattedSearchCriteria)
        }
        if (isValidPhone(formattedSearchCriteria)) {
            formattedSearchCriteria = formattedSearchCriteria.removeRange(startIndexCutPhone, endIndexCutPhone)
        }

        return formattedSearchCriteria.decapitalize()
    }

    fun isValidEUDate(dateString: String): Boolean {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        sdf.isLenient = false

        try {
            sdf.parse(dateString)
        } catch (e: ParseException) {
            return false
        }
        return true
    }

    fun formatDateFromEUToUS(dateString: String): String {
        val defaultEUDateFormat = SimpleDateFormat("dd-MM-yyyy")
        val defaultUSDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val oldDate = defaultEUDateFormat.parse(dateString)

        return defaultUSDateFormat.format(oldDate)
    }

    fun isValidPhone(input: String): Boolean {
        return Regex("^((\\+|00)?[0-9]{2}(-|)(\\(0\\)[\\-]?)?|0)[1-9]((|-)?[0-9])((\\-)?[0-9])((\\-)?[0-9])[0-9]{5}\$")
                .matches(input)
    }
}
