package unicus.service

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import unicus.TestHelper
import unicus.dao.DuplicateDAO
import unicus.dto.DuplicateClientDTO
import unicus.entity.*
import java.util.*
import kotlin.test.assertEquals

class CalculateSimilarityServiceTest {

    private val testHelper: TestHelper = TestHelper()

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    @InjectMocks
    var sut = CalculateSimilarityService()

    @Test
    fun testIdentifyDuplicateClientsReturnsTheCorrectDuplicateClientDTO() {
        val mainRecord = Client(
                "1111",
                "1",
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

        val doubleRecord1 = Client(
                "2222",
                "2",
                "Broersss",
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

        val doubleRecord2 = Client(
                "3333",
                "3",
                "Jansen",
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
        val duplicateClientDTO: DuplicateClientDTO = DuplicateClientDTO(mainRecord, doubleRecord1, 2.0)
        assertEquals(duplicateClientDTO, sut.identifyDuplicateClients(mainRecord, mutableListOf(doubleRecord1, doubleRecord2)))
    }

    @Test
    fun testIdentifyDuplicateClientsReturnsTheCorrectDuplicateClientDTOWhenSameBSN() {
        val mainRecord = Client(
                "1111",
                "1",
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

        val doubleRecord1 = Client(
                "1111",
                "2",
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

        val doubleRecord2 = Client(
                "1111",
                "3",
                "Jansen",
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

        val duplicateClientDTO = DuplicateClientDTO(mainRecord, doubleRecord1, -50.0)
        assertEquals(duplicateClientDTO, sut.identifyDuplicateClients(mainRecord, mutableListOf(doubleRecord1, doubleRecord2)))
    }

    @Test
    fun testDuplicateFromSimilarityListGivesBackCorrectDTO() {
        val mainRecord = testHelper.clientNull.copy()
        val doubleRecord1 = testHelper.clientNull.copy()
        val doubleRecord2 = testHelper.clientNull.copy()
        mainRecord.bsn = "1111"
        doubleRecord1.bsn = "2222"
        doubleRecord2.bsn = "3333"

        val clientSimilarityObject1 = ClientSimilarity(mainRecord, doubleRecord1, 0, 0, 0, 0.0)
        val clientSimilarityObject2 = ClientSimilarity(mainRecord, doubleRecord2, 0, 0, 0, 6.0)
        val clientSimilarityList = mutableListOf(clientSimilarityObject1, clientSimilarityObject2)
        val duplicateClientDTOMock = DuplicateClientDTO(mainRecord, doubleRecord1, 0.0)

        val sutSpy = Mockito.spy(CalculateSimilarityService())
        Mockito.`when`(sutSpy.createDuplicateClientDTOObject(clientSimilarityObject1)).thenReturn(duplicateClientDTOMock)
        assertEquals(duplicateClientDTOMock, sut.getDuplicateFromSimilarityList(clientSimilarityList))
    }

    @Test
    fun testDuplicateFromSimilarityListGivesBackNull() {
        val mainRecord = testHelper.clientNull.copy()
        val doubleRecord1 = testHelper.clientNull.copy()
        val doubleRecord2 = testHelper.clientNull.copy()
        mainRecord.bsn = "1111"
        doubleRecord1.bsn = "2222"
        doubleRecord2.bsn = "3333"

        val clientSimilarityObject1 = ClientSimilarity(mainRecord, doubleRecord1, 0, 0, 0, 6.0)
        val clientSimilarityObject2 = ClientSimilarity(mainRecord, doubleRecord2, 0, 0, 0, 7.0)
        val clientSimilarityList = mutableListOf(clientSimilarityObject1, clientSimilarityObject2)
        assertEquals(null, sut.getDuplicateFromSimilarityList(clientSimilarityList))
    }

    @Test
    fun testCreateDuplicateClientDTOObjectWhenNonDuplicateBSN() {
        val mainRecord = testHelper.clientNull.copy()
        val doubleRecord = testHelper.clientNull.copy()
        mainRecord.bsn = "1111"
        doubleRecord.bsn = "2222"

        val clientSimilarityObject = ClientSimilarity(mainRecord, doubleRecord, 0, 0, 0, 5.0)
        val duplicateClientDTO = DuplicateClientDTO(mainRecord, doubleRecord, 5.0)
        assertEquals(duplicateClientDTO, sut.createDuplicateClientDTOObject(clientSimilarityObject))
    }

    @Test
    fun testCreateDuplicateClientDTOObjectWhenDuplicateBSN() {
        val mainRecord = testHelper.clientNull.copy()
        val doubleRecord = testHelper.clientNull.copy()
        mainRecord.bsn = "1111"
        doubleRecord.bsn = "1111"

        val clientSimilarityObject = ClientSimilarity(mainRecord, doubleRecord, 0, 0, 0, 5.0)
        val duplicateClientDTO = DuplicateClientDTO(mainRecord, doubleRecord, -50.0)
        assertEquals(duplicateClientDTO, sut.createDuplicateClientDTOObject(clientSimilarityObject))
    }

    @Test
    fun testcalcSimilarityScore() {
        val clientSimilarity4 = testHelper.clientSimilarity1.copy(mainRecord = testHelper.clientNull.copy(), duplicateRecord = testHelper.clientNull.copy())
        clientSimilarity4.duplicateRecord.geslacht = "V"

        assertEquals(9.0, sut.calcSimilarityScore(testHelper.clientSimilarity1))
        assertEquals(9.0, sut.calcSimilarityScore(testHelper.clientSimilarity2))
        assertEquals(7.0, sut.calcSimilarityScore(testHelper.clientSimilarity3))
        assertEquals(11.0, sut.calcSimilarityScore(clientSimilarity4))
    }

    @Test
    fun testThatMethodReturnsCorrectDistanceValueWhenFirstNamesAreEntered() {
        assertEquals(3, sut.calcDistance("Ricky", "Randy"))
        assertEquals(5, sut.calcDistance("Ricky", "Wout"))
        assertEquals(1, sut.calcDistance("Pieter", "Peter"))
        assertEquals(0, sut.calcDistance("Julian", "Julian"))
    }

    @Test
    fun testThatMethodReturnsCorrectDistanceValueWhenLastNamesAreEntered() {
        assertEquals(3, sut.calcDistance("Broers", "Grouls"))
        assertEquals(7, sut.calcDistance("Broers", "Janssen"))
        assertEquals(3, sut.calcDistance("Pietersen", "Peters"))
        assertEquals(12, sut.calcDistance("Korf de gidts", "Janssen"))
    }

    @Test
    fun testThatMethodReturnsCorrectDistanceValueWhenOnValueIsLeftEmpty() {
        assertEquals(6, sut.calcDistance(" ", "Grouls"))
        assertEquals(6, sut.calcDistance("Broers", " "))
    }

}