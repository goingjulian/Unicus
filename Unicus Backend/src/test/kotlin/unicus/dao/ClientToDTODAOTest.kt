package unicus.dao

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import unicus.TestHelper

class ClientToDTODAOTest {


    private val sut = ClientToDTODAO()

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
    fun testIfClientsDossiersAreCollectedWhenIdsAreCorrect() {

        val results = sut.getExtraDossierDataFromClient(helper.client1.dossiers!!)

        assertEquals(helper.dossier1P1.naam, results[0].naam)
        assertEquals(helper.dossier2P1.naam, results[1].naam)
    }

    @Test
    fun testIfClientIsConvertedIntoDTO() {

        val spySut = Mockito.spy(sut)

        Mockito.doReturn(mutableListOf(helper.address1)).`when`(spySut).getExtraAdressDataFromClient(helper.client1.adressen!!)

        val newClient = sut.convertClientToDTO(helper.client1)

        assertEquals(helper.client1.relatienummer, newClient.relatienummer)
        assertEquals(helper.client1.voornamen, newClient.voornamen)
        assertEquals(helper.client1.geslachtsnaam, newClient.geslachtsnaam)
        assertEquals(helper.client1.adressen!![0].adresnummer, newClient.adressen!![0]._id)
    }

    @Test
    fun testIfAdressessAreCollectedWhenIdIsCorrect() {

        val addresses = sut.getExtraAdressDataFromClient(helper.client1.adressen!!)

        assertEquals(helper.client1.adressen!![0].adresnummer, addresses[0]._id)
    }
}
