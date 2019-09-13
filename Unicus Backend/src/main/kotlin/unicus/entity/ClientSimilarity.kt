package unicus.entity

data class ClientSimilarity (
        val mainRecord: Client,
        val duplicateRecord: Client,
        val voornamenDistance: Int?,
        val geslachtsnaamDistance: Int,
        val geboortedatumDistance: Int?,
        var similarityScore: Double
)