package unicus.connection

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import java.io.ByteArrayOutputStream
import java.io.PrintStream

@RunWith(MockitoJUnitRunner::class)
class MongoDBConnectionTest {

    @InjectMocks
    private val sut: MongoDBClient = MongoDBClient

    private var originalOut: PrintStream? = null
    private val outContent = ByteArrayOutputStream()

    @Before
    fun setup() {
        this.originalOut = System.out
        System.setOut(PrintStream(outContent))
    }

    @Rule
    @JvmField
    var thrown: ExpectedException = ExpectedException.none()

    @Test
    fun testIfExceptionIsThrownWhenDatabaseConfigurationFileIsNotFound() {
        thrown.expect(ConfigurationFileException::class.java)
        sut.getDatabaseConfigFromFile("/fakefake")
    }

    @Test
    fun testIfExceptionIsThrownIfNotAllCredentialsAreSpecified() {
        thrown.expect(ConfigurationFileException::class.java)
        val properties = sut.getDatabaseConfigFromFile("/databaseCredentialTest.properties")
        sut.getServerCredentialsFromFile(properties)
    }

    @Test
    fun testIfExceptionIsThrownIfServerAddresOrPortIsNotSpecified() {
        thrown.expect(ConfigurationFileException::class.java)
        val properties = sut.getDatabaseConfigFromFile("/databaseAdressTest.properties")
        sut.getServerAdressFromFile(properties)
    }

    @Test
    fun testIfCorrectCredentialsAreReturnedInPropertiesObjectWhenConfigFileContainsValidValues() {
        val properties = sut.getDatabaseConfigFromFile("/databaseCorrectTest.properties")
        assertEquals("myurl", properties.getProperty("url"))
        assertEquals("1234", properties.getProperty("port"))
        assertEquals("usr", properties.getProperty("user"))
        assertEquals("pass", properties.getProperty("password"))
        assertEquals("database", properties.getProperty("authdb"))
    }

    @Test
    fun testIfCorrectServerCredentialObjectIsAreReturnedWhenConfigFileContainsValidValues() {
        val properties = sut.getDatabaseConfigFromFile("/databaseCorrectTest.properties")
        val credentials = sut.getServerCredentialsFromFile(properties)

        assertEquals("usr", credentials.userName)
        assertEquals("pass", credentials.password.joinToString(""))
        assertEquals("database", credentials.source)
    }

    @Test
    fun testIfCorrectServerAdressObjectIsReturnedWhenConfigfilecontainsValidValues() {
        val properties = sut.getDatabaseConfigFromFile("/databaseCorrectTest.properties")
        val serverAddres = sut.getServerAdressFromFile(properties)

        assertEquals("myurl", serverAddres.host)
        assertEquals(1234, serverAddres.port)
    }

    @After
    fun afterTests() {
        System.setOut(originalOut)
    }

}