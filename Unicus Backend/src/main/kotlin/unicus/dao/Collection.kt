package unicus.dao

import com.mongodb.client.MongoCollection
import org.litote.kmongo.getCollection
import unicus.connection.DBClientFactory
import unicus.dto.DuplicateClientDTO
import unicus.entity.Client
import unicus.entity.ExtendedAdres
import unicus.entity.ExtendedDossier

class Collection {
    private val dbName = "unicusDB"
    val clientCollectionName = "clients"
    val dossierCollectionName = "dossiers"
    val addressCollectionName = "adresses"
    val duplicatesCollectionName = "duplicates"

    fun <T> getCollection(type: CollectionType): MongoCollection<T> {
        val mongoClient = DBClientFactory().getDBClient()

        val database = mongoClient.getDatabase(dbName)

        return when (type) {
            CollectionType.CLIENT -> database.getCollection<Client>(clientCollectionName)
            CollectionType.DOSSIER -> database.getCollection<ExtendedDossier>(dossierCollectionName)
            CollectionType.ADRESS -> database.getCollection<ExtendedAdres>(addressCollectionName)
            CollectionType.DUPLICATES -> database.getCollection<DuplicateClientDTO>(duplicatesCollectionName)
        } as MongoCollection<T>
    }

}
