package unicus.connection

import com.mongodb.MongoClient

interface DBClient {
    fun createNewMongoClient(): MongoClient
}
