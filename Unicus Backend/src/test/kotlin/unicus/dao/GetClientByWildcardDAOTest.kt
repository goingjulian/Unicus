package unicus.dao

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import unicus.TestHelper
import unicus.exception.ClientIdNotValidException
import unicus.service.ClientService

class GetClientByWildcardDAOTest {
    @Rule
    @JvmField
    val thrown = ExpectedException.none()

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

    private val sut = GetClientByWildcardDAO()

    private val clientService = ClientService()

    @Test
    fun testThatclientsCanBeFoundByWildcard() {

        val result = sut.getClientsByWildcard(clientService.filterSearchCriteria(helper.client1.voornamen!!))

        assertEquals(helper.client1.relatienummer, result[0].relatienummer)
    }

    @Test
    fun testThatExceptionIsThrownWithInvalidSearchCriteriaGetclientsFromCollectionsByWildcard() {

        thrown.expect(ClientIdNotValidException::class.java)
        sut.getClientsByWildcard(helper.invalidSearchCriteria)
    }

    @Test
    fun testThatclientsCanBeFoundWithGivenSearchCriteria() {

        val results = sut.getClientsByWildcard(clientService.filterSearchCriteria(helper.client1.voornamen!!))

        assertEquals(helper.client1.relatienummer, results[0].relatienummer)
        assertEquals(helper.client1.geslachtsnaam, results[0].geslachtsnaam)
        assertEquals(helper.client1.voornamen, results[0].voornamen)
        assertEquals(helper.client1.tussenvoegsels, results[0].tussenvoegsels)
        assertEquals(helper.client1.geslacht, results[0].geslacht)
        assertEquals(helper.client1.geboortedatum, results[0].geboortedatum)
    }

    @Test
    fun testThatclientsCanBeFoundByContactDataPhone() {

        val results = sut.getClientsByWildcard(clientService.filterSearchCriteria(helper.client1.contactgegevens!![0].nummeradres!!))
        assertEquals(helper.client1.relatienummer, results[0].relatienummer)
    }

    @Test
    fun testThatExceptionIsThrownOnNoResultsFoundByContactDataPhone() {
        thrown.expect(ClientIdNotValidException::class.java)

        sut.getClientsByWildcard(helper.invalidPhoneNumber)
    }

    @Test
    fun testThatclientsCanBeFoundByContactDataEmail() {

        val results = sut.getClientsByWildcard(clientService.filterSearchCriteria(helper.client1.contactgegevens!![1].nummeradres!!))
        assertEquals(helper.client1.relatienummer, results[0].relatienummer)
    }

    @Test
    fun testThatExceptionIsThrownOnNoResultsFoundByContactDataEmail() {
        thrown.expect(ClientIdNotValidException::class.java)

        sut.getClientsByWildcard(helper.invalidEmail)
    }

    @Test
    fun testThatclientsCanBeFoundByValidAddress() {

        val results = sut.getClientsByWildcard(clientService.filterSearchCriteria(helper.clientDTO1.adressen!![0].straatnaam!!))

        assertEquals(helper.client1.relatienummer, results[0].relatienummer)
    }

    @Test
    fun testThatExceptionIsThrownWhenOnNoRecordsFoundByGivenAddress() {
        thrown.expect(ClientIdNotValidException::class.java)

        sut.getClientsByWildcard(helper.invalidAddress)
    }
}