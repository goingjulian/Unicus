package unicus.controller

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockHttpServletRequest
import unicus.TestHelper
import unicus.dto.ClientDTO
import unicus.exception.ClientIdNotValidException
import unicus.exception.ClientIdsAreTheSameException
import unicus.service.ClientService

@RunWith(MockitoJUnitRunner::class)
class ClientControllerTest {

    @InjectMocks
    lateinit var sut: ClientController

    @Mock
    private val clientServiceMock = Mockito.mock(ClientService::class.java)

    private val helper = TestHelper()

    @Test
    fun testThatStatusOkAndClientEntityIsReturnedUpdateclient() {
        Mockito.`when`(clientServiceMock.updateClient("${helper.client1.relatienummer}", "${helper.client2.relatienummer}", helper.fieldsToUpdateDTO)).thenReturn(helper.clientDTO1)
        val response: ResponseEntity<ClientDTO> = sut.updateClient("${helper.client1.relatienummer}", "${helper.client2.relatienummer}", helper.fieldsToUpdateDTO, MockHttpServletRequest())

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(helper.clientDTO1, response.body)
    }

    @Test
    fun testThatStatusNotFoundIsReturnedOnInvalidClientIdUpdateClient() {
        Mockito.`when`(clientServiceMock.updateClient("${helper.client1.relatienummer}", "${helper.client2.relatienummer}", helper.fieldsToUpdateDTO)).thenThrow(ClientIdNotValidException())
        assertEquals(HttpStatus.NOT_FOUND, sut.updateClient("${helper.client1.relatienummer}", "${helper.client2.relatienummer}", helper.fieldsToUpdateDTO, MockHttpServletRequest()).statusCode)
    }

    @Test
    fun testThatStatusBadRequestIsReturnedWhenClientIdsAreTheSame() {
        Mockito.`when`(clientServiceMock.updateClient("${helper.client1.relatienummer}", "${helper.client1.relatienummer}", helper.fieldsToUpdateDTO)).thenThrow(ClientIdsAreTheSameException())
        assertEquals(HttpStatus.BAD_REQUEST, sut.updateClient("${helper.client1.relatienummer}", "${helper.client1.relatienummer}", helper.fieldsToUpdateDTO, MockHttpServletRequest()).statusCode)
    }

    @Test
    fun testThatStatusOkAndListOfClientsEntityIsReturnedGetClientsByWildcard() {
        val clients = mutableListOf(helper.clientDTO1)

        Mockito.`when`(clientServiceMock.getClientsByWildcard(Mockito.anyString())).thenReturn(clients)
        val response = sut.getClientsByWildcard("Test", MockHttpServletRequest())

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(clients, response.body)
    }

    @Test
    fun testThatStatusNotFoundIsReturnedOnInvalidSearchCriteriaGetClientsByWildcard() {
        Mockito.`when`(clientServiceMock.getClientsByWildcard(Mockito.anyString())).thenThrow(ClientIdNotValidException())
        val response = sut.getClientsByWildcard("${helper.client1.relatienummer}", MockHttpServletRequest())

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun testThatStatusOkAndPersoonEntityIsReturnedGetClient() {
        Mockito.`when`(clientServiceMock.getClient(helper.clientDTO1.relatienummer!!)).thenReturn(helper.clientDTO1)
        val response: ResponseEntity<ClientDTO> = sut.getClientById(helper.clientDTO1.relatienummer!!, MockHttpServletRequest())

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(helper.clientDTO1, response.body)
    }

    @Test
    fun testThatStatusNotFoundIsReturnedOnInvalidClientIdGetClient() {
        Mockito.`when`(clientServiceMock.getClient(helper.invalidClientId)).thenThrow(ClientIdNotValidException())
        assertEquals(HttpStatus.NOT_FOUND, sut.getClientById(helper.invalidClientId, MockHttpServletRequest()).statusCode)
    }
}