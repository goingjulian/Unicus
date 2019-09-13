package unicus.dao

import mu.KotlinLogging

abstract class DAO {
    val errorDBConnectionMessage = "An error occured when connecting to the database, " +
            "please check your configuration file"

    var collection: Collection = Collection()

    val logger = KotlinLogging.logger {}

}

