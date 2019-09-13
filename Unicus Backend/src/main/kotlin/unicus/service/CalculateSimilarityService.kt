package unicus.service

import org.springframework.stereotype.Service
import unicus.dto.DuplicateClientDTO
import unicus.entity.Client
import unicus.entity.ClientSimilarity
import java.text.SimpleDateFormat

@Service
class CalculateSimilarityService {
    private val scoreSameBSN: Double = -50.0
    private val maxSimilarityScore: Double = 5.0
    private val birthDateScoreMultiplier: Double = 3.0
    private val lastnameScoreMultiplier: Double = 2.0
    private val firstnameScoreMultiplier: Double = 2.0
    private val fieldEqualScoreMultiplier: Double = 1.0


    /**
     * Identify if a record has a duplicate from a pool of clients
     */
    fun identifyDuplicateClients(mainRecord: Client, compareRecords: List<Client>): DuplicateClientDTO? {
        return if (compareRecords.isEmpty()) {
            null
        } else {
            val levenshteinResults: MutableList<ClientSimilarity> = calcSimilarityPool(mainRecord, compareRecords)
            getDuplicateFromSimilarityList(levenshteinResults)
        }
    }

    /**
     * Identify a possible double record from a pool of levenshtein calculations
     */
    fun getDuplicateFromSimilarityList(duplicateClients: List<ClientSimilarity>): DuplicateClientDTO? {
        val matchedClient: ClientSimilarity? = duplicateClients.filter { it.similarityScore <= maxSimilarityScore || it.mainRecord.bsn == it.duplicateRecord.bsn }.minBy { it.similarityScore }
        return if (matchedClient == null) null
        else createDuplicateClientDTOObject(matchedClient)
    }

    /**
     * Converts a clientSimilarity object to a DuplicateClientDTO
     */
    fun createDuplicateClientDTOObject(matchedClient: ClientSimilarity): DuplicateClientDTO {
        return if (matchedClient.mainRecord.bsn == matchedClient.duplicateRecord.bsn) {
            DuplicateClientDTO(matchedClient.mainRecord, matchedClient.duplicateRecord, scoreSameBSN)
        }
        else {
            DuplicateClientDTO(matchedClient.mainRecord, matchedClient.duplicateRecord, matchedClient.similarityScore)
        }
    }

    /**
     * Calculate the similarity score of a pool
     */
    fun calcSimilarityPool(client: Client, clientPool: List<Client>): MutableList<ClientSimilarity> {
        val clientSimilarityResults: MutableList<ClientSimilarity> = mutableListOf()
        clientPool.forEach { it ->
            clientSimilarityResults.add(createSimilarityScoreObject(client, it))
        }
        return clientSimilarityResults
    }

    //Persoon heeft alleen een verplichte geslachtsnaam en geslacht
    fun createSimilarityScoreObject(mainRecord: Client, compareRecord: Client): ClientSimilarity {
        var geboortedatumDistance: Int? = null
        var voornamenDistance: Int? = null

        if (mainRecord.voornamen != null && compareRecord.voornamen != null) {
            voornamenDistance = calcDistance(mainRecord.voornamen, compareRecord.voornamen)
        }
        if (mainRecord.geboortedatum != null && compareRecord.geboortedatum != null) {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
            geboortedatumDistance = calcDistance(dateFormat.format(mainRecord.geboortedatum), dateFormat.format(compareRecord.geboortedatum))
        }

        val geslachtsnaamDistance = calcDistance(mainRecord.geslachtsnaam, compareRecord.geslachtsnaam)

        val clientSimilarity = ClientSimilarity(
                mainRecord,
                compareRecord,
                voornamenDistance,
                geslachtsnaamDistance,
                geboortedatumDistance,
                maxSimilarityScore + 1.0
        )
        clientSimilarity.similarityScore = calcSimilarityScore(clientSimilarity)

        return clientSimilarity
    }

    /**
     * Calculate similarity score for a possible duplicate. Because 'voornamen' and 'geboortedatum'
     * are non-required fields and can therefore be null it will first check whether they are null
     */
    fun calcSimilarityScore(client: ClientSimilarity): Double {
        var sum = 0.0

        if (client.voornamenDistance != null) {
            sum += (client.voornamenDistance * firstnameScoreMultiplier)
            if (client.voornamenDistance == 0) sum -= fieldEqualScoreMultiplier
        }
        if (client.geboortedatumDistance != null) {
            sum += (client.geboortedatumDistance * birthDateScoreMultiplier)
            if (client.geboortedatumDistance == 0) sum -= fieldEqualScoreMultiplier
        }
        sum += (client.geslachtsnaamDistance * lastnameScoreMultiplier)

        if (client.geslachtsnaamDistance == 0) sum -= fieldEqualScoreMultiplier
        if (client.mainRecord.geslacht != client.duplicateRecord.geslacht) sum += 2

        if (client.mainRecord.geslachtsnaam == "Rijssdijk" || client.duplicateRecord.geslachtsnaam == "Rijssdijk") {
            println(sum)
        }

        return sum
    }

    /**
     * Compute Levenshtein distance
     */
    fun calcDistance(mainRecord: String?, compareRecord: String?): Int {
        val nameMatrix: Array<IntArray>
        val mainLength: Int = mainRecord!!.length
        val compareLength: Int = compareRecord!!.length

        //catch edge case where one of the records is empty.
        if (mainLength == 0) return compareLength
        if (compareLength == 0) return mainLength

        nameMatrix = Array(mainLength + 1) { IntArray(compareLength + 1) }

        for (i in 0..mainLength) nameMatrix[i][0] = i
        for (i in 0..compareLength) nameMatrix[0][i] = i

        for (i in 1..mainLength) {
            val mainI = mainRecord[i - 1]
            for (j in 1..compareLength) {
                val compareJ = compareRecord[j - 1]
                val cost = if (mainI == compareJ) 0 else 1

                nameMatrix[i][j] = lowestNumber(
                        nameMatrix[i - 1][j] + 1,
                        nameMatrix[i][j - 1] + 1,
                        nameMatrix[i - 1][j - 1] + cost
                )
            }
        }
        return nameMatrix[mainLength][compareLength]
    }

    /**
     * Get lowestNumber of three values
     */
    fun lowestNumber(above: Int, left: Int, leftAbove: Int): Int {
        return when {
            left <= above && left <= leftAbove -> left
            leftAbove <= left && leftAbove <= above -> leftAbove
            else -> above
        }
    }
}