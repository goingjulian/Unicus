package unicus.service

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import unicus.dao.DuplicateDAO
import unicus.dto.DuplicateClientDTO
import unicus.entity.Client
import unicus.exception.AlreadyScanningException
import unicus.exception.NoScanningResultsAvailableException

@Service
class DuplicateService {

    companion object {
        var scanState: ScanState = ScanState.IDLE
        var progressPercentage: Double = 0.0
    }

    @Autowired
    private lateinit var duplicateDAO: DuplicateDAO

    private var calculateSimilarityService: CalculateSimilarityService = CalculateSimilarityService()

    val logger = KotlinLogging.logger {}

    private val maxProgressPercentage = 100.0
    private val minProgressPercentage = 0.0

    private val DBErrorString = "An error occurred during database operations while a scan was running. Scan status is set to ERROR"

    /**
     *  This function starts the scan
     */

    fun detectDuplicateClients(): MutableList<DuplicateClientDTO> {
        if (scanState == ScanState.DETECTING || scanState == ScanState.FETCHING) {
            logger.error { "Tried to start a scan while another one was already running" }
            throw AlreadyScanningException()
        }

        scanState = ScanState.FETCHING
        progressPercentage = minProgressPercentage

        logger.info { "Started duplicates scan, fetching clients" }

        lateinit var clients: MutableList<Client?>

        try {
            clients = duplicateDAO.getAllClients()
        } catch (e: Exception) {
            logger.error { DBErrorString }
            scanState = ScanState.ERROR
            throw e
        }

        val duplicateClientResults: MutableList<DuplicateClientDTO> = mutableListOf()

        logger.info { "${clients.size} clients fetched, starting detection process" }

        scanState = ScanState.DETECTING
        clients.forEachIndexed { index, client ->

            val progressSmall: Double = index.toDouble() / clients.size
            progressPercentage = progressSmall * maxProgressPercentage

            if (client != null) {
                clients[index] = null
                val clientsWithSameBSN: List<Client> = clients.filterNotNull().filter { it.bsn == client.bsn }
                val duplicateClientResult: DuplicateClientDTO? = if (clientsWithSameBSN.isNotEmpty()) {
                    calculateSimilarityService.identifyDuplicateClients(client, clientsWithSameBSN)
                } else {
                    val clientPool: List<Client> = clients.filterNotNull().filter { it.voornamen == client.voornamen || it.geslachtsnaam == client.geslachtsnaam || it.geboortedatum == client.geboortedatum }
                    calculateSimilarityService.identifyDuplicateClients(client, clientPool)
                }


                if (duplicateClientResult != null) {
                    duplicateClientResults.add(duplicateClientResult)
                    val doubleRecordIndex = clients.indexOf(duplicateClientResult.doubleRecord)
                    clients[doubleRecordIndex] = null
                }
            }
        }
        storeDuplicateResults(duplicateClientResults)
        scanState = ScanState.IDLE
        progressPercentage = maxProgressPercentage
        return duplicateClientResults
    }

    /**
     * This function sends all duplicate client objects to the DAO to be stored in the database
     *
     * @Throws Exception
     */

    @Throws(Exception::class)
    fun storeDuplicateResults(results: MutableList<DuplicateClientDTO>) {
        try {
            duplicateDAO.deleteAllPreviousResults()
            if (results.isNotEmpty()) duplicateDAO.insertManyResults(results)
        } catch (e: Exception) {
            logger.error { DBErrorString }
            scanState = ScanState.ERROR
            throw e
        }
    }

    /**
     * This function returns the results that were detected and stored in the database in the last performed scan
     *
     * @throws NoScanningResultsAvailableException when no results are available in the database
     */

    @Throws(NoScanningResultsAvailableException::class)
    fun getScannedDuplicatesList(minResults: Int, maxResults: Int): MutableList<DuplicateClientDTO> {
        val scannedDuplicates = duplicateDAO.getScannedDuplicates()
        if (scannedDuplicates.isEmpty()) {
            throw NoScanningResultsAvailableException()
        } else {
            scannedDuplicates.sortBy { it.similarityScore }
            val currentClientsOfScannedDuplicates = duplicateDAO.getClientsFromScannedDuplicates(scannedDuplicates)
            val duplicateClients: MutableList<DuplicateClientDTO> = checkIfDuplicateMatches(scannedDuplicates, currentClientsOfScannedDuplicates)
            return shortenList(minResults, maxResults, duplicateClients)
        }
    }

    /**
     * This function checks if results that were previously stored still match the live results in the database
     */
    fun checkIfDuplicateMatches(scannedDuplicates: MutableList<DuplicateClientDTO>, latestClientsOfScannedDuplicates: MutableList<Client>): MutableList<DuplicateClientDTO> {
        val matchingDuplicates: MutableList<DuplicateClientDTO> = mutableListOf()

        scannedDuplicates.forEach {
            val mainRecord = it.mainRecord.toString()
            val duplicateRecord = it.doubleRecord.toString()
            val currentMainRecord = latestClientsOfScannedDuplicates.find { client -> it.mainRecord.relatienummer == client.relatienummer }.toString()
            val currentDuplicateRecord = latestClientsOfScannedDuplicates.find { client -> it.doubleRecord.relatienummer == client.relatienummer }.toString()
            if (mainRecord != currentMainRecord || duplicateRecord != currentDuplicateRecord) {
                duplicateDAO.deleteScannedDuplicate(it)
            } else {
                matchingDuplicates.add(it)
            }
        }

        return matchingDuplicates
    }

    /**
     * This function gives the user the ability to limit how many results he wants to get from the database at once
     */

    fun shortenList(minResults: Int, maxResults: Int, list: MutableList<DuplicateClientDTO>): MutableList<DuplicateClientDTO> {
        val minSize = when {
            minResults < 0 -> 0
            minResults > list.size -> list.size
            else -> minResults
        }
        val maxSize = when {
            maxResults > list.size -> list.size
            maxResults < 0 -> 0
            else -> maxResults
        }
        return list.subList(minSize, maxSize)
    }

    /**
     * This function returns how much percentual progress
     */

    fun getProgressInt(): Int {
        return progressPercentage.toInt()
    }
}