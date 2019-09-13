package unicus.dto

import unicus.entity.Client

data class DuplicateClientDTO(
        val mainRecord: Client,
        val doubleRecord: Client,
        val similarityScore: Double
)