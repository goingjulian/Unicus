package unicus.entity

import java.util.*

data class IdentiteitsDocument(
        val type: String?,
        val nummer: String?,
        val land: String?,
        val afgiftedatum: Date?,
        val afgifteplaats: String?,
        val geldigtot: Date?,
        val scandocument: String?,
        val vis: MutableList<Vis>?
)
