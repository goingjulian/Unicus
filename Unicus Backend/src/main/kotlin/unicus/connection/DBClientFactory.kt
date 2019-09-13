package unicus.connection

import com.mongodb.MongoClient
import org.springframework.stereotype.Service

@Service
class DBClientFactory {

    fun getDBClient(): MongoClient {
        return MongoDBClient.createNewMongoClient()
    }

}
