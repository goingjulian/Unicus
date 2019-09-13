package unicus.connection

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import mu.KotlinLogging
import org.litote.kmongo.KMongo
import java.util.*

object MongoDBClient : DBClient {
    val propertiesFileName = "/database.properties"
    val userPropertyName = "user"
    val databasePropertyName = "authdb"
    val passwordPropertyName = "password"
    val urlPropertyName = "url"
    val portPropertyName = "port"

    val logger = KotlinLogging

    var client: MongoClient? = null

    override fun createNewMongoClient(): MongoClient {

        val properties = getDatabaseConfigFromFile(propertiesFileName)

        val credentials: MongoCredential = getServerCredentialsFromFile(properties)
        val serverAddr: ServerAddress = getServerAdressFromFile(properties)

        if (client == null) {
            client = if (credentials.userName == null ||
                    credentials.password == null ||
                    credentials.userName.isEmpty() ||
                    credentials.password.isEmpty()) {
                 makeClientFromProperties(serverAddr)
            }else{
                makeClientFromProperties(serverAddr, credentials)
            }
        }

        return client!!
    }

    fun makeClientFromProperties(serverAddr: ServerAddress, credentials: MongoCredential): MongoClient {
        return KMongo.createClient(serverAddr, Arrays.asList(credentials))
    }

    fun makeClientFromProperties(serverAddr: ServerAddress): MongoClient {
        return KMongo.createClient(serverAddr)
    }

    fun getDatabaseConfigFromFile(fileName: String): Properties {
        val properties = Properties()

        try {
            properties.load(javaClass.getResourceAsStream(fileName))
        } catch (e: Exception) {
            logArgumentError()
            throw ConfigurationFileException()
        }

        return properties
    }

    fun getServerCredentialsFromFile(props: Properties): MongoCredential {
        lateinit var credentials: MongoCredential
        try {
            if (props.getProperty(userPropertyName) == null ||
                    props.getProperty(databasePropertyName) == null ||
                    props.getProperty(passwordPropertyName) == null) {
                throw IllegalArgumentException()
            }
            credentials = MongoCredential.createCredential(
                    props.getProperty(userPropertyName),
                    props.getProperty(databasePropertyName),
                    props.getProperty(passwordPropertyName).toCharArray()
            )

        } catch (e: IllegalArgumentException) {
            logArgumentError()
            throw ConfigurationFileException()
        }

        return credentials
    }

    fun getServerAdressFromFile(props: Properties): ServerAddress {
        lateinit var serverAddr: ServerAddress
        try {
            if (props.getProperty(urlPropertyName) == null ||
                    props.getProperty(portPropertyName) == null) {
                throw IllegalArgumentException()
            }

            serverAddr = ServerAddress(
                    props.getProperty(urlPropertyName),
                    props.getProperty(portPropertyName).toInt()
            )

        } catch (e: IllegalArgumentException) {
            logArgumentError()
            throw ConfigurationFileException()
        }
        return serverAddr
    }

    fun logArgumentError() {
        logger.logger { }.error {
            "An error occured when connecting to the database, please check your configuration file"
        }
    }
}
