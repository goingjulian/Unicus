package unicus.dao


import org.junit.After
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import unicus.TestHelper
import unicus.dto.DuplicateClientDTO
import unicus.entity.Client
import kotlin.test.assertTrue


class DuplicateDAOTest {
    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    val sut: DuplicateDAO = DuplicateDAO()

    @Before
    fun setUp() {
        helper.start()
    }

    companion object {
        private val helper = TestHelper()

        @AfterClass
        fun tearDown() {
            helper.stop()
        }
    }


    @Test
    fun testThatGetAllClientsReturnsTheCorrectListOfClients() {
        val allClientsFromDatabase: MutableList<Client?> = sut.getAllClients()

        assertEquals(mutableListOf(helper.client1, helper.client2, helper.client3, helper.client4, helper.client5, helper.client6), allClientsFromDatabase)
    }

    @Test
    fun testThatInsertManyResultsWorksCorrectly() {
        val duplicateClientDTOsFromDatabase: MutableList<DuplicateClientDTO> = mutableListOf()

        sut.insertManyResults(mutableListOf(helper.duplicateClientDTO3))

        helper.duplicatesCollection.find().forEach { duplicateClientDTOsFromDatabase.add(it) }

        assertEquals(mutableListOf(helper.duplicateClientDTO1, helper.duplicateClientDTO2, helper.duplicateClientDTO3), duplicateClientDTOsFromDatabase)
    }

    @Test
    fun testThatGetScannedDuplicatesCorrectlyReturnsAllDuplicatesFromTheDatabaseCollection() {
        val scannedDuplicatesFromDatabase = sut.getScannedDuplicates()

        assertEquals(mutableListOf(helper.duplicateClientDTO1, helper.duplicateClientDTO2), scannedDuplicatesFromDatabase)
    }

    @Test
    fun testThatDeleteAllPreviousResultsDeletesTheCorrectData() {
        sut.deleteAllPreviousResults()

        val duplicateClientDTOsFromDatabase: MutableList<DuplicateClientDTO> = mutableListOf()

        helper.duplicatesCollection.find().forEach { duplicateClientDTOsFromDatabase.add(it)}

        assertEquals(mutableListOf<DuplicateClientDTO>(), duplicateClientDTOsFromDatabase)
    }

    @Test
    fun testThatDeleteScannedDuplicateDeletesTheCorrectDataInDeDatabase() {
        var duplicateClientDTOsFromDatabase: MutableList<DuplicateClientDTO> = mutableListOf()

        helper.duplicatesCollection.find().forEach { duplicateClientDTOsFromDatabase.add(it) }

        //check if the data to delete is even in the database in the first place
        assertTrue(duplicateClientDTOsFromDatabase.contains(helper.duplicateClientDTO1))

        sut.deleteScannedDuplicate(helper.duplicateClientDTO1)

        duplicateClientDTOsFromDatabase = mutableListOf()
        helper.duplicatesCollection.find().forEach { duplicateClientDTOsFromDatabase.add(it) }

        assertEquals(mutableListOf(helper.duplicateClientDTO2), duplicateClientDTOsFromDatabase)
    }

    @Test
    fun testThatGetClientsFromScannedDuplicatesRetrievesTheCorrectDataFromTheDatabase() {
        val clientCollection: MutableList<Client> = mutableListOf()

        helper.clientCollection.find().forEach { clientCollection.add(it) }

        val functionResult: MutableList<Client> = sut.getClientsFromScannedDuplicates(mutableListOf(helper.duplicateClientDTO1, helper.duplicateClientDTO2, helper.duplicateClientDTO3))

        assertTrue(functionResult.any {it == helper.client1 || it == helper.client2 || it == helper.client3 || it ==  helper.client4 || it == helper.client5 || it == helper.client6})
    }

}