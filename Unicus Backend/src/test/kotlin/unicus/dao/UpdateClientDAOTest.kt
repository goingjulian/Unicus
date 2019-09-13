package unicus.dao

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.litote.kmongo.find
import unicus.TestHelper
import unicus.entity.Client

class UpdateClientDAOTest {

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

    private val sut = UpdateClientDAO()

    @Test
    fun testIfclientIsUpdatedIfValidIdsAreGiven() {
        sut.updateClientInDB(helper.client1.relatienummer!!, helper.client2)
        lateinit var foundclient: Client
        helper.clientCollection.find("{relatienummer: '${helper.client1.relatienummer}'}").forEach { foundclient = it }

        assertEquals(helper.client1.relatienummer, foundclient.relatienummer)
        assertEquals(helper.client2.voornamen, foundclient.voornamen)
        assertEquals(helper.client2.geslachtsnaam, foundclient.geslachtsnaam)
        assertEquals(helper.client2.bsn, foundclient.bsn)
        assertEquals(helper.client2.geboortedatum, foundclient.geboortedatum)
        assertEquals(helper.client2.geslacht, foundclient.geslacht)
        assertEquals(helper.client2.geboorteland, foundclient.geboorteland)
        assertEquals(helper.client2.geboorteplaats, foundclient.geboorteplaats)
        assertEquals(helper.client2.nationaliteit, foundclient.nationaliteit)
        assertEquals(helper.client2.opmerking, foundclient.opmerking)
    }

    @Test
    fun testIfclientIsNotUpdatedWhenInvalidclientIdIsSupplied() {
        sut.updateClientInDB(helper.invalidSearchCriteria, helper.client2)
        val res = mutableListOf<Client>()

        helper.clientCollection.find("{relatienummer: '${helper.invalidSearchCriteria}'}").forEach { res.add(it) }

        assertEquals(true, res.isEmpty())
    }

    @Test
    fun testThatDataFromclientBCanBeCopiedToclientAWithGivenFields() {
        val newclient = sut.copyDataFromClientBToClientA(helper.client1, helper.client2, helper.fieldsToUpdateDTO)

        assertEquals(newclient.bsn, helper.client2.bsn)
        assertEquals(newclient.voornamen, helper.client2.voornamen)
        assertEquals(newclient.voorletters, helper.client2.voorletters)
        assertEquals(newclient.geboorteplaats, helper.client2.geboorteplaats)
        assertEquals(newclient.geboorteland, helper.client2.geboorteland)
    }
}
