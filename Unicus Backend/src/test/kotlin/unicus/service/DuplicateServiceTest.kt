package unicus.service

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import unicus.controller.ClientController
import unicus.dao.DuplicateDAO
import unicus.dto.DuplicateClientDTO
import unicus.entity.Client
import unicus.exception.AlreadyScanningException
import unicus.exception.NoScanningResultsAvailableException
import unicus.service.DuplicateService.Companion.progressPercentage
import unicus.service.DuplicateService.Companion.scanState
import java.util.*

@RunWith(MockitoJUnitRunner.Silent::class)
class DuplicateServiceTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    @Mock
    private val duplicateDAOMock = Mockito.mock(DuplicateDAO::class.java)

    @Mock
    private val calculateSimilarityServiceMock = Mockito.mock(CalculateSimilarityService::class.java)

    @InjectMocks
    private val sut: DuplicateService = DuplicateService()

    @After
    fun tearDown() {
        scanState = ScanState.IDLE
        progressPercentage = 0.0
    }

    /**
     * Tests for: fun detectDuplicateClients(): MutableList<DuplicateClientDTO>
     */

    @Test
    fun testThatDetectDuplicateClientsHappyPathIsExecutedProperlyAndResponseIsCorrectWhenClientsDontHaveTheSameBSN() {
        val client1 = Client(
                "999",
                "1000",
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

        val client2 = Client(
                "5555",
                "2000",
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

        val client3 = Client(
                "777",
                "888",
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

        val spySut = Mockito.spy(DuplicateService())
        val duplicateClientDTO = DuplicateClientDTO(client1, client2,0.0)

        Mockito.`when`(duplicateDAOMock.getAllClients()).thenReturn(mutableListOf(client1, client2, client3))
        Mockito.`when`(calculateSimilarityServiceMock.identifyDuplicateClients(client1, mutableListOf(client2, client3))).thenReturn(duplicateClientDTO).thenReturn(null)
        Mockito.doNothing().`when`(spySut).storeDuplicateResults(mutableListOf(duplicateClientDTO))
        assertEquals(mutableListOf(duplicateClientDTO), sut.detectDuplicateClients())
    }

    @Test
    fun testThatDetectDuplicateClientsThrowsAnExceptionWhenSomethingGoesWrongWithTheDatabase() {
        
        thrown.expect(Exception::class.java)

        Mockito.`when`(duplicateDAOMock.getAllClients()).thenThrow(Exception())

        sut.detectDuplicateClients()
    }

    @Test
    fun testThatDetectDuplicateClientsHappyPathIsExecutedProperlyAndResponseIsCorrectWhenClientsHaveTheSameBSN() {
        val duplicateBSN = "123456"

        val client1 = Client(
                duplicateBSN,
                "1000",
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

        val client2 = Client(
                duplicateBSN,
                "2000",
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

        val client3 = Client(
                "777",
                "888",
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


        val duplicateClientDTO = DuplicateClientDTO(client1, client2,-50.0)
        val spySut = Mockito.spy(DuplicateService())
        Mockito.`when`(duplicateDAOMock.getAllClients()).thenReturn(mutableListOf(client1, client2, client3))
        Mockito.`when`(calculateSimilarityServiceMock.identifyDuplicateClients(client1, listOf(client2))).thenReturn(duplicateClientDTO).thenReturn(null)
        Mockito.doNothing().`when`(spySut).storeDuplicateResults(mutableListOf(duplicateClientDTO))
        assertEquals(mutableListOf(duplicateClientDTO), sut.detectDuplicateClients())
    }

    @Test
    fun testThatWhenTheUserTriesToStartTheDetectionAlgorithmWhileItsAlreadyRunningAnExceptionGetsThrown() {
        scanState = ScanState.DETECTING
        thrown.expect(AlreadyScanningException::class.java)
        sut.detectDuplicateClients()
    }

    /**
     * Test for: fun storeDuplicateResults(results: MutableList<DuplicateClientDTO>)
     */

    @Test
    fun testThatStoreDuplicateResultsHappyPathWorksCorrectly() {

        val client1 = Client(
                "999",
                "1000",
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

        val client2 = Client(
                "5555",
                "2000",
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

        val duplicateClientDTO1 = DuplicateClientDTO(client1, client2,0.0)

        sut.storeDuplicateResults(mutableListOf(duplicateClientDTO1))

        verify(duplicateDAOMock).deleteAllPreviousResults()
        verify(duplicateDAOMock).insertManyResults(mutableListOf(duplicateClientDTO1))
    }

    @Test
    fun testThatStoreDuplicateResultsThrowsAnExceptionWhenTheDAOThrowsAnException() {

        thrown.expect(Exception::class.java)

        val client1 = Client(
                "999",
                "1000",
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

        val client2 = Client(
                "5555",
                "2000",
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

        val duplicateClientDTO1 = DuplicateClientDTO(client1, client2,0.0)

        Mockito.`when`(duplicateDAOMock.deleteAllPreviousResults()).thenThrow(Exception())

        sut.storeDuplicateResults(mutableListOf(duplicateClientDTO1))
    }

    /**
     * Tests for: fun getScannedDuplicatesList(minResults: Int, maxResults: Int): MutableList<DuplicateClientDTO>
     */

    @Test
    fun testThatIfGetScannedDuplicatesListHappyPathWorksCorrectly() {
        val spySut = Mockito.spy(sut)

        val client1 = Client(
                "999",
                "1000",
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

        val client2 = Client(
                "5555",
                "2000",
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

        val duplicateClientDTO = DuplicateClientDTO(client1, client2,0.0)

        Mockito.`when`(duplicateDAOMock.getScannedDuplicates()).thenReturn(mutableListOf(duplicateClientDTO))
        Mockito.`when`(duplicateDAOMock.getClientsFromScannedDuplicates(mutableListOf(duplicateClientDTO))).thenReturn(mutableListOf(client1, client2))
        Mockito.doReturn(mutableListOf(duplicateClientDTO)).`when`(spySut).checkIfDuplicateMatches(mutableListOf(duplicateClientDTO),mutableListOf(client1, client2))

        assertEquals(mutableListOf(duplicateClientDTO), sut.getScannedDuplicatesList(0, 5))
    }

    @Test
    fun testThatGetScannedDuplicatesListThrowsAnExceptionWhenTheReturnedListOfScannedDuplicatesIsEmpty() {
        Mockito.`when`(duplicateDAOMock.getScannedDuplicates()).thenReturn(mutableListOf( ))

        thrown.expect(NoScanningResultsAvailableException::class.java)

        sut.getScannedDuplicatesList(0,5)
    }

    /**
     * Tests for: fun checkIfDuplicateMatches(scannedDuplicates: MutableList<DuplicateClientDTO>, latestClientsOfScannedDuplicates: MutableList<Client>): MutableList<DuplicateClientDTO>
     */

    @Test
    fun testThatCheckIfDuplicateMatchesWorksCorrectlyWhenAllDataMatches() {

        val client1 = Client(
                "999",
                "1000",
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

        val client2 = Client(
                "5555",
                "2000",
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

        val duplicateClientDTO = mutableListOf(DuplicateClientDTO(client1, client2, 0.0))

        val latestClientsOfScannedDuplicates = mutableListOf(client1, client2)

        assertEquals(duplicateClientDTO, sut.checkIfDuplicateMatches(duplicateClientDTO, latestClientsOfScannedDuplicates))
    }

    @Test
    fun testThatCheckIfDuplicateMatchesWorksCorrectlyWhenNotAllDataMatches() {
        val client1 = Client(
                "999",
                "1000",
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

        val client2 = Client(
                "5555",
                "2000",
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

        val slightlyChangedClient2 = Client(
                "5555",
                "2000",
                "Broerss",
                "Rick",
                "J.",
                null,
                "M",
                Date(631148400000),
                "Arnhem",
                "Nederland",
                "Nederlandse",
                null, null, null, null, null, null,
                null, null, null, null)

        val duplicateClientDTOs = mutableListOf(DuplicateClientDTO(client1, client2, 0.0))
        val latestClientsOfScannedDuplicates = mutableListOf(client1, slightlyChangedClient2)

        val checkIfDuplicateMatchesReturnValue: MutableList<DuplicateClientDTO> = sut.checkIfDuplicateMatches(duplicateClientDTOs, latestClientsOfScannedDuplicates)

        verify(duplicateDAOMock).deleteScannedDuplicate(duplicateClientDTOs[0])
        assertTrue(checkIfDuplicateMatchesReturnValue.isEmpty())
    }

    /**
     * Tests for: fun shortenList(minResults: Int, maxResults: Int, list: MutableList<DuplicateClientDTO>): MutableList<DuplicateClientDTO>
     */

    @Test
    fun testThatShortenListWorksCorrectly() {

        val duplicateClientDTO1 = DuplicateClientDTO(
                Client(
                        "999",
                        "1000",
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
                        null, null, null, null),
                Client(
                        "5555",
                        "2000",
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
                        null, null, null, null),
                0.0
        )

        val duplicateClientDTO2 = DuplicateClientDTO(
                Client(
                        "555",
                        "666",
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
                        null, null, null, null),
                Client(
                        "777",
                        "888",
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
                        null, null, null, null),
                2.0
        )

        assertEquals(
                mutableListOf(duplicateClientDTO2),
                sut.shortenList(1,2, mutableListOf(duplicateClientDTO1, duplicateClientDTO2))
        )
    }

    /**
     * Tests for: fun getProgressInt(): Int
     */

    @Test
    fun testThatGetProgressIntReturnsCorrectPercentage() {
        progressPercentage = 35.0

        assertEquals(35, sut.getProgressInt())
    }
}