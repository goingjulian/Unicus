package unicus.service

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import unicus.TestHelper
import unicus.dao.ClientToDTODAO
import unicus.dao.ClientValidityDAO
import unicus.dao.DeleteClientDAO
import unicus.dao.GetClientByWildcardDAO
import unicus.dao.UpdateClientDAO
import unicus.exception.ClientIdNotValidException
import unicus.exception.ClientIdsAreTheSameException

@RunWith(MockitoJUnitRunner.Silent::class)
class ClientServiceTest {

    @Rule
    @JvmField
    var thrown: ExpectedException = ExpectedException.none()

    @Mock
    private val clientValidityDAOMock = Mockito.mock(ClientValidityDAO::class.java)

    @Mock
    private val clientToDTODAOMock = Mockito.mock(ClientToDTODAO::class.java)

    @Mock
    private val updateclientDAO = Mockito.mock(UpdateClientDAO::class.java)

    @Mock
    private val wildcardDAOMock = Mockito.mock(GetClientByWildcardDAO::class.java)

    @Mock
    private val deleteclientDAOMock = Mockito.mock(DeleteClientDAO::class.java)

    @InjectMocks
    lateinit var sut: ClientService

    private val helper = TestHelper()

    @Test
    fun testThatPersoonEntityIsReturnedUpdateclient() {
        Mockito.`when`(clientValidityDAOMock.doesClientExist(Mockito.anyString())).thenReturn(true)
        Mockito.`when`(updateclientDAO.mergeDataFromClients("${helper.client1.relatienummer}", "${helper.client2.relatienummer}", helper.fieldsToUpdateDTO)).thenReturn(helper.client1)
        Mockito.`when`(clientToDTODAOMock.convertClientToDTO(helper.client1)).thenReturn(helper.clientDTO1)
        Mockito.doNothing().`when`(deleteclientDAOMock).deleteClient(helper.client1.relatienummer!!)

        assertEquals(helper.clientDTO1, sut.updateClient("${helper.client1.relatienummer}", "${helper.client2.relatienummer}", helper.fieldsToUpdateDTO))
    }

    @Test
    fun testThatExceptionIsThrownOnInvalidclientIdUpdateclient() {
        thrown.expect(ClientIdNotValidException::class.java)

        Mockito.`when`(clientValidityDAOMock.doesClientExist(Mockito.anyString())).thenReturn(false)
        sut.updateClient(helper.invalidSearchCriteria, "${helper.client1.relatienummer}", helper.fieldsToUpdateDTO)
    }

    @Test
    fun testThatExceptionIsThrownWhenIdenticalIdsAreSuppliedUpdateclient() {
        thrown.expect(ClientIdsAreTheSameException::class.java)

        sut.updateClient("${helper.client1.relatienummer}", "${helper.client1.relatienummer}", helper.fieldsToUpdateDTO)
    }

    @Test
    fun testThatListOfclientsIsReturnedOnValidSearchCriteria() {
        val clients = mutableListOf(helper.clientDTO1)

        Mockito.`when`(wildcardDAOMock.getClientsByWildcard(Mockito.anyString())).thenReturn(clients)
        val result = sut.getClientsByWildcard("${helper.client1.relatienummer}")

        assertEquals(clients, result)
    }

    @Test
    fun testThatExceptionIsThrownAndNotHandledInServiceOnInvalidSearchCriteria() {
        thrown.expect(ClientIdNotValidException::class.java)

        Mockito.`when`(wildcardDAOMock.getClientsByWildcard(Mockito.anyString())).thenThrow(ClientIdNotValidException::class.java)

        sut.getClientsByWildcard("${helper.client1.relatienummer}")
    }

    @Test
    fun testThatDateCanBeValidated() {
        assertTrue(sut.isValidEUDate("17-12-2018"))
        assertTrue(sut.isValidEUDate("7-12-2018"))
        assertTrue(sut.isValidEUDate("07-12-2018"))
        assertTrue(sut.isValidEUDate("07-02-2018"))
        assertTrue(sut.isValidEUDate("07-2-2018"))

        assertFalse(sut.isValidEUDate("2018-12-17"))
    }

    @Test
    fun testThatDateCanBeFormattedToUs() {
        assertEquals(sut.formatDateFromEUToUS("14-12-2018"), "2018-12-14")
    }

    @Test
    fun testThatPhoneCanBeValidated() {
        assertTrue(sut.isValidPhone("31654876215"))
        assertTrue(sut.isValidPhone("31-654876215"))
        assertTrue(sut.isValidPhone("0485568952"))
        assertTrue(sut.isValidPhone("0485-568952"))

        assertFalse(sut.isValidPhone("31 654876215"))
        assertFalse(sut.isValidPhone("0485 568952"))
    }

    @Test
    fun testThatSpecialCharsInSearchCriteriaAreFiltered() {
        val criteria = "+SomeSentence'\$"

        val spySut = Mockito.spy(sut)

        Mockito.doReturn(false).`when`(spySut).isValidEUDate(criteria)
        Mockito.doReturn(false).`when`(spySut).isValidPhone(criteria)

        assertEquals("somesentence", spySut.filterSearchCriteria(criteria))
    }

    @Test
    fun testThatMailCharactersInSearchCriteriaAreNotFiltered() {
        val criteria = "peter@test.zz"

        val spySut = Mockito.spy(sut)

        Mockito.doReturn(false).`when`(spySut).isValidEUDate(criteria)
        Mockito.doReturn(false).`when`(spySut).isValidPhone(criteria)

        assertEquals("peter@test.zz", spySut.filterSearchCriteria(criteria))
    }

    @Test
    fun testThatPersoonDTOEntityIsReturnedGetClient() {
        Mockito.`when`(clientValidityDAOMock.doesClientExist(helper.client1.relatienummer!!)).thenReturn(true)
        Mockito.`when`(clientValidityDAOMock.getClientById(helper.client1.relatienummer!!)).thenReturn(helper.client1)
        Mockito.`when`(clientToDTODAOMock.convertClientToDTO(helper.client1)).thenReturn(helper.clientDTO1)

        val result = sut.getClient(helper.client1.relatienummer!!)

        assertEquals(helper.client1.relatienummer, result.relatienummer)
        assertEquals(helper.client1.geslachtsnaam, result.geslachtsnaam)
        assertEquals(helper.client1.geboortedatum, result.geboortedatum)

    }

    @Test
    fun testThatExceptionIsThrownOnInvalidClientIdGetClient() {
        thrown.expect(ClientIdNotValidException::class.java)

        Mockito.`when`(clientValidityDAOMock.doesClientExist(Mockito.anyString())).thenReturn(false)
        sut.getClient(helper.invalidClientId)
    }


}