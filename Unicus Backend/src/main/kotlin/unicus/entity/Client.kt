package unicus.entity

import java.util.*

data class Client(
        @JvmField var bsn: String?,
        @JvmField var relatienummer: String?,
        @JvmField var geslachtsnaam: String?,
        @JvmField var voornamen: String?,
        @JvmField var voorletters: String?,
        @JvmField var tussenvoegsels: String?,
        @JvmField var geslacht: String?,
        @JvmField var geboortedatum: Date?,
        @JvmField var geboorteplaats: String?,
        @JvmField var geboorteland: String?,
        @JvmField var nationaliteit: String?,
        @JvmField var opmerking: String?,
        @JvmField var adressen: MutableList<Adres>?,
        @JvmField var partner: MutableList<Partner>?,
        @JvmField var contactgegevens: MutableList<Contactgegevens>?,
        @JvmField var betaalgegevens: MutableList<Betaalgegevens>?,
        @JvmField var labels: MutableList<Label>?,
        @JvmField var contactmomenten: MutableList<Contactmoment>?,
        @JvmField var dossiers: MutableList<Dossier>?,
        @JvmField var identiteitsdocumenten: MutableList<IdentiteitsDocument>?,
        @JvmField var brpZegel: BrpZegel?
)
