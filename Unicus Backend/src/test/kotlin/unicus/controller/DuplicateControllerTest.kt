package unicus.controller

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockHttpServletRequest
import unicus.TestHelper
import unicus.dto.DuplicateClientDTO
import unicus.exception.NoScanningResultsAvailableException
import unicus.scheduled.ScheduledScanningTasks
import unicus.service.DuplicateService
import unicus.service.ScanState

@RunWith(MockitoJUnitRunner::class)
class DuplicateControllerTest {

    val testHelper: TestHelper = TestHelper()

    @Rule
    @JvmField
    var thrown: ExpectedException = ExpectedException.none()

    @Mock
    private val duplicateServiceMock = Mockito.mock(DuplicateService::class.java)

    @Mock
    private val scheduledScanningTasksMock = Mockito.mock(ScheduledScanningTasks::class.java)

    @InjectMocks
    lateinit var sut: DuplicateController


    @Test
    fun testThatTheStartScanFunctionInTheControllerReturnsOK() {
        val response: ResponseEntity<Any> = sut.startScan(MockHttpServletRequest())

        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun testThatACorrectResponseEntityIsReturnedWhenGetLatestScanResultsIsCalled() {
        Mockito.`when`(duplicateServiceMock.getScannedDuplicatesList(1,2))
                .thenReturn(mutableListOf(
                        testHelper.duplicateClientDTO1,
                        testHelper.duplicateClientDTO2)
                )

        val response: ResponseEntity<MutableList<DuplicateClientDTO>> = sut.getLatestScanResults(1, 2, MockHttpServletRequest())

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(mutableListOf(testHelper.duplicateClientDTO1, testHelper.duplicateClientDTO2), response.body)
    }

    @Test
    fun testThatResponseCodeNotFoundIsReturnedWhenTheFunctionInTheServiceThrowsAnException() {
        Mockito.`when`(duplicateServiceMock.getScannedDuplicatesList(1, 5)).thenThrow(NoScanningResultsAvailableException::class.java)

        val response: ResponseEntity<MutableList<DuplicateClientDTO>> = sut.getLatestScanResults(1, 5, MockHttpServletRequest())

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testThatGetScanProgressPercentageReturnsACorrectIntegerNumber() {
        Mockito.`when`(duplicateServiceMock.getProgressInt()).thenReturn(20)

        val response: ResponseEntity<Int> = sut.getScanProgressPercentage(MockHttpServletRequest())

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(response.body, 20)
    }

    @Test
    fun testThatGetScantStatusReturnsTheCorrectScanState() {
        val response: ResponseEntity<ScanState> = sut.getScanStatus(MockHttpServletRequest())

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(ScanState.IDLE, response.body)
    }
}