package unicus.dao

import com.mongodb.MongoTimeoutException
import mu.KotlinLogging
import org.litote.kmongo.find
import org.springframework.stereotype.Repository
import unicus.entity.Client

@Repository
class ClientValidityDAO : DAO() {
    fun doesClientExist(clientId: String): Boolean {
        try {
            KotlinLogging.logger {}.info {
                "Checking client with id: $clientId"
            }
            getClientById(clientId)
        } catch (e: IllegalArgumentException) {
            KotlinLogging.logger {}.warn {
                "Client with id: $clientId was not found"
            }
            return false
        }
        return true
    }

//    @Throws(InvalidArgumentException::class)
fun getClientById(clientId: String): Client {

    val results = mutableListOf<Client>()

        try {
            collection.getCollection<Client>(CollectionType.CLIENT)
                    .find("{ relatienummer: '$clientId'}").limit(1)
                    .forEach { results.add(it) }

            if (results.isEmpty()) {
                throw IllegalArgumentException()
            }
        } catch (e: MongoTimeoutException) {
            KotlinLogging.logger {}.error {
                errorDBConnectionMessage
            }
        }

        return results[0]
    }
}