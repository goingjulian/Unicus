package unicus

import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import java.io.IOException
import java.net.ServerSocket


class MongoTestDBWrapper {
    /** The embedded MongoDB executable  */
    var mongodExecutable: MongodExecutable? = null
    /** The URL to connect on  */
    var connectionUrl: String? = null

    val url = "localhost"
    var port: String? = null

    /**
     * Start MongoDB running
     * @throws IOException if an error occurs starting MongoDB
     */
    @Throws(IOException::class)
    fun start() {
        if (mongodExecutable == null) {
            val port = 60000 //getFreePort() //getFreePort does not work on Windows for some reason

            val starter = MongodStarter.getDefaultInstance()
            val mongodConfig = MongodConfigBuilder()
                    .version(Version.LATEST_NIGHTLY)
                    .net(Net("localhost", port, Network.localhostIsIPv6()))
                    .build()

            mongodExecutable = starter.prepare(mongodConfig)
            mongodExecutable!!.start()

            this.port = port.toString()

            connectionUrl = "mongodb://localhost:$port"
        }
    }

    /**
     * Stop MongoDB
     */
    fun stop() {
        if (mongodExecutable != null) {
            mongodExecutable!!.stop()
        }
    }

    /**
     * Get a free port to listen on
     * @return the port
     * @throws IOException if an error occurs finding a port
     */
    @Throws(IOException::class)
    private fun getFreePort(): Int {
        val s = ServerSocket(0)
        return s.localPort
    }
}