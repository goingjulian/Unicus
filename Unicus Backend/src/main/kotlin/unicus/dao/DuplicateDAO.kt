package unicus.dao

import com.mongodb.MongoTimeoutException
import mu.KotlinLogging
import org.litote.kmongo.MongoOperator
import org.litote.kmongo.deleteMany
import org.litote.kmongo.deleteOne
import org.litote.kmongo.find
import org.springframework.stereotype.Repository
import unicus.dto.DuplicateClientDTO
import unicus.entity.Client

@Repository
class DuplicateDAO : DAO() {

    @Throws(Exception::class)
    fun getAllClients(): MutableList<Client?> {
        val clients = mutableListOf<Client?>()
        collection.getCollection<Client>(CollectionType.CLIENT).find().forEach { clients.add(it) }

        return clients
    }

    @Throws(Exception::class)
    fun insertManyResults(results: MutableList<DuplicateClientDTO>) {
        collection.getCollection<DuplicateClientDTO>(CollectionType.DUPLICATES).insertMany(results)

        KotlinLogging.logger {}.info { "New results inserted" }
    }

    fun getScannedDuplicates(): MutableList<DuplicateClientDTO> {
        val results = mutableListOf<DuplicateClientDTO>()
        try {
            collection.getCollection<DuplicateClientDTO>(CollectionType.DUPLICATES).find<DuplicateClientDTO>().forEach { results.add(it) }
        } catch (e: MongoTimeoutException) {
            KotlinLogging.logger {}.error {
                errorDBConnectionMessage
            }
        }
        return results
    }

    @Throws(Exception::class)
    fun deleteAllPreviousResults() {
        collection.getCollection<DuplicateClientDTO>(CollectionType.DUPLICATES).deleteMany()

        KotlinLogging.logger {}.info { "Old results deleted" }
    }

    fun deleteScannedDuplicate(duplicateClientDTO: DuplicateClientDTO) {
        val filter = "{'mainRecord.relatienummer': '${duplicateClientDTO.mainRecord.relatienummer}'}"

        collection.getCollection<DuplicateClientDTO>(CollectionType.DUPLICATES).deleteOne(filter)
    }

    fun getClientsFromScannedDuplicates(results: MutableList<DuplicateClientDTO>): MutableList<Client> {
        val clients = mutableListOf<Client>()
        var filter = ""

        results.forEach {
            filter += "{ relatienummer: '${it.mainRecord.relatienummer}' }, "
            filter += "{ relatienummer: '${it.doubleRecord.relatienummer}' }, "
        }

        filter = "{ ${MongoOperator.or}: [ $filter ] }"

        try {
            collection.getCollection<Client>(CollectionType.CLIENT).find(filter).forEach { clients.add(it) }
        } catch (e: MongoTimeoutException) {
            KotlinLogging.logger {}.error {
                errorDBConnectionMessage
            }
        }

        return clients
    }
}