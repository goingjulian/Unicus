package unicus.dao

import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.litote.kmongo.find
import unicus.TestHelper
import unicus.entity.Client

class DeleteClientDAOTest {

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

    private val sut = DeleteClientDAO()

    @Test
    fun testIfclientIsDeletedWhenValidIdIsGiven() {

        sut.deleteClient(helper.client1.relatienummer!!)

        val results = getClients("relatienummer: '${helper.client1.relatienummer}'")

        assertEquals(0, results.size)
    }

    @Test
    fun testIfNothingIsDeletedOrThrownWhenInvalidIdIsGiven() {

        val before = getClients("")

        sut.deleteClient(helper.invalidSearchCriteria)

        val after = getClients("")

        assertEquals(true, before.size == after.size)
    }

    private fun getClients(query: String): MutableList<Client> {
        val results = mutableListOf<Client>()

        helper.clientCollection.find("{$query}").forEach { results.add(it) }

        return results
    }
}