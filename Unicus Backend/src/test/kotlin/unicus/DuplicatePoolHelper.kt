package unicus

import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import unicus.connection.MongoDBClient
import unicus.entity.Client
import java.util.*

class DuplicatePoolHelper {
    private val mongoDBConnection = MongoDBClient
    private val db = MongoTestDBWrapper()
    private var testDB: MongoClient? = null

    lateinit var clientCollection: MongoCollection<Client>

    // Hoofd
    val client1: Client = Client(
            "12345",
            "1000",
            "Peter",
            "Jan",
            "J.",
            null,
            "M",
            Date(631148400000),
            "Arnhem",
            "Nederland",
            "Nederlandse",
            null, null, null, null, null, null,
            null, null, null, null)

    // Zelfde voornaam
    val client2: Client = Client(
            "123456",
            "2000",
            "Pieter",
            "Jan",
            "J.",
            null,
            "M",
            Date(631148400000),
            "Arnhem",
            "Nederland",
            "Nederlandse",
            null, null, null, null, null, null,
            null, null, null, null)

    // Zelfde geslachtsnaam
    val client3: Client = Client(
            "1234567",
            "3000",
            "Peter",
            "Jantje",
            "J.",
            null,
            "M",
            Date(631148400000),
            "Arnhem",
            "Nederland",
            "Nederlandse",
            null, null, null, null, null, null,
            null, null, null, null)

    // Zelfde bsn en gebooortedatum
    val client4: Client = Client(
            "12345",
            "1000",
            "Pieter",
            "Jantje",
            "J.",
            null,
            "M",
            Date(631148400000),
            "Arnhem",
            "Nederland",
            "Nederlandse",
            null, null, null, null, null, null,
            null, null, null, null)
}