package unicus

import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import org.litote.kmongo.getCollection
import unicus.connection.MongoDBClient
import unicus.dao.Collection
import unicus.dto.ClientDTO
import unicus.dto.DuplicateClientDTO
import unicus.dto.FieldsToUpdateDTO
import unicus.entity.*
import java.util.*

class TestHelper {

    private val mongoDBClient = MongoDBClient
    private val db = MongoTestDBWrapper()

    companion object {
        private var testDB: MongoClient? = null
    }

    private val collection = Collection()

    lateinit var clientCollection: MongoCollection<Client>
    lateinit var dossierCollection: MongoCollection<ExtendedDossier>
    lateinit var adressCollection: MongoCollection<ExtendedAdres>
    lateinit var duplicatesCollection: MongoCollection<DuplicateClientDTO>

    val dossier1P1 = ExtendedDossier(
            "0", 1234, "testDossier1"
    )

    val dossier2P1 = ExtendedDossier(
            "1", 5678, "testDossier2"
    )

    val address1 = ExtendedAdres(
            "1234",
            "Hoofdstraat",
            123,
            "5454AB",
            "Arnhem",
            "Arnhem",
            "Nederland"
    )

    val clientNull = Client(
            null, null, null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null, null,
            null, null, null)

    val client1 = Client(
            "1111",
            "1",
            "Petersen",
            "Jan",
            "J.",
            null,
            "M",
            Date(631148400000),
            "Arnhem",
            "Nederland",
            "Nederlandse",
            "Dit is een opmerking",
            mutableListOf(Adres(address1._id!!, "WOONADRES", Date(631148400000))), null,
            mutableListOf(Contactgegevens("telefoon", "zakelijk", "+3165833175"),
                    Contactgegevens("e-mailadres", "zakelijk", "Jan.petersen@mailprovider.nl")
            ), null, null, null,
            mutableListOf(Dossier(dossier1P1._id!!), Dossier(dossier2P1._id!!)), null, null)

    val client2 = Client(
            "2222",
            "2",
            "Gerrits",
            "Pieter",
            "P.",
            null,
            "V",
            Date(631148400000),
            "Arnhem",
            "Nederland",
            "Nederlandse",
            "Dit is een opmerking",
            null, null, null, null, null, null, null, null, null)

    val client3: Client = Client(
            "3333",
            "3",
            "Van Heesch",
            "Uwe",
            "J.",
            null,
            "M",
            Date(631148400000),
            "Arnhem",
            "Nederland",
            "Nederlandse",
            null, null, null, null, null, null,
            null, null, null, null)

    val client4: Client = Client(
            "4444",
            "4",
            "Korf de Gidts",
            "Julian",
            "J.",
            null,
            "M",
            Date(631148400000),
            "Arnhem",
            "Nederland",
            "Nederlandse",
            null, null, null, null, null, null,
            null, null, null, null)

    val client5: Client = Client(
            "5555",
            "5",
            "Broers",
            "Ricky",
            "J.",
            null,
            "M",
            Date(631148400000),
            "Arnhem",
            "Nederland",
            "Nederlandse",
            null, null, null, null, null, null,
            null, null, null, null)

    val client6: Client = Client(
            "6666",
            "6",
            "Broerss",
            "Ricky",
            "J.",
            null,
            "M",
            Date(631148400000),
            "Arnhem",
            "Nederland",
            "Nederlandse",
            null, null, null, null, null, null,
            null, null, null, null)

    val client7: Client = Client(
            "5555",
            "7",
            "Broerss",
            "Ricky",
            "J.",
            null,
            "M",
            Date(631148400000),
            "Arnhem",
            "Nederland",
            "Nederlandse",
            null, null, null, null, null, null,
            null, null, null, null)
    
    val clientDTO1 = ClientDTO(client1.bsn,
            client1.relatienummer,
            client1.geslachtsnaam,
            client1.voornamen,
            client1.voorletters,
            client1.tussenvoegsels,
            client1.geslacht,
            client1.geboortedatum,
            client1.geboorteplaats,
            client1.geboorteland,
            client1.nationaliteit,
            client1.opmerking,
            mutableListOf(address1), null,
            client1.contactgegevens, client1.betaalgegevens, client1.labels, client1.contactmomenten,
            mutableListOf(dossier1P1, dossier2P1), client1.identiteitsdocumenten, client1.brpZegel)

    val clientSimilarity1 = ClientSimilarity(clientNull.copy(), clientNull.copy(), 0, 2, 2, 0.0)
    val clientSimilarity2 = ClientSimilarity(clientNull.copy(), clientNull.copy(), 2, 0, 2, 0.0)
    val clientSimilarity3 = ClientSimilarity(clientNull.copy(), clientNull.copy(), 2, 2, 0, 0.0)

    val fieldsToUpdateDTO = FieldsToUpdateDTO(
            true, false,
            false, true, false,
            false, false, true,
            true, true, false,
            false, false, false,
            false, false, false,
            false)

    val invalidSearchCriteria = "XXXXXXXXXXX"
    val invalidPhoneNumber = "600000000"
    val invalidEmail = "FAKE@FAKE.ZZ"
    val invalidAddress = "somestreet9"
    val invalidClientId = "999999999999"

    val duplicateClientDTO1: DuplicateClientDTO = DuplicateClientDTO(client5, client6,0.0)
    val duplicateClientDTO2: DuplicateClientDTO = DuplicateClientDTO(client3, client4,2.0)
    val duplicateClientDTO3: DuplicateClientDTO = DuplicateClientDTO(client1, client2,2.0)

    val duplicateClientDTO4: DuplicateClientDTO = DuplicateClientDTO(client6, client7,-50.0)

    fun start() {
        if(testDB == null) {
            db.start()
            testDB = mongoDBClient.createNewMongoClient()
        }

        prepareData()
    }

    fun prepareData() {
        val db = testDB!!.getDatabase("unicusDB")

        if(db.listCollections().first() != null) {
            db.listCollections().forEach {
                db.getCollection(it["name"].toString()).drop()
            }
        }

        db.createCollection(collection.clientCollectionName)
        db.createCollection(collection.dossierCollectionName)
        db.createCollection(collection.addressCollectionName)
        db.createCollection(collection.duplicatesCollectionName)

        clientCollection = db.getCollection<Client>(collection.clientCollectionName)
        dossierCollection = db.getCollection<ExtendedDossier>(collection.dossierCollectionName)
        adressCollection = db.getCollection<ExtendedAdres>(collection.addressCollectionName)
        duplicatesCollection = db.getCollection<DuplicateClientDTO>(collection.duplicatesCollectionName)

        clientCollection.insertMany(listOf(client1, client2, client3, client4, client5, client6))
        dossierCollection.insertMany(listOf(dossier1P1, dossier2P1))
        duplicatesCollection.insertMany(listOf(duplicateClientDTO1, duplicateClientDTO2))
        adressCollection.insertOne(address1)
    }

    fun stop() {
        db.stop()
    }
}