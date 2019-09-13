package unicus.entity

import java.util.*

data class Contactmoment(
        val soort: String?,
        val richting: String?,
        val medewerker: String?,
        val datumtijd: Date?,
        val onderwerp: String?,
        val inhoud: String?
)
