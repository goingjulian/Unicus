package unicus.dao

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito
import unicus.TestHelper

class ClientValidityDAOTest {

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

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

    private val sut = ClientValidityDAO()

    @Test
    fun testIfCorrectclientIsFoundWhenValidIdIsSupplied() {
        assertEquals(helper.client1.voornamen, sut.getClientById(helper.client1.relatienummer!!).voornamen)
    }

    @Test
    fun testIfExceptionIsThrownWhenInvalidclientIdIsSuppliedGetclient() {
        thrown.expect(IllegalArgumentException::class.java)

        assertEquals(helper.client1.voornamen, sut.getClientById(helper.invalidSearchCriteria).voornamen)
    }

    @Test
    fun testIfclientExistsReturnsFalseWhenInvalidIdIsGiven() {
        val spySut = Mockito.spy(sut)
        Mockito.doThrow(IllegalArgumentException::class.java).`when`(spySut).getClientById(helper.invalidSearchCriteria)
        assertEquals(false, spySut.doesClientExist(helper.invalidSearchCriteria))
    }

    @Test
    fun testIfclientExistsReturnsTrueWhenValidIdIsGiven() {
        val spySut = Mockito.spy(sut)
        Mockito.doReturn(helper.client1).`when`(spySut).getClientById(helper.invalidSearchCriteria)

        assertEquals(true, spySut.doesClientExist(helper.invalidSearchCriteria))
    }
}