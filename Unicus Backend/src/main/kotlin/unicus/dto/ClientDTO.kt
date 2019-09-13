package unicus.dto

import unicus.entity.Betaalgegevens
import unicus.entity.BrpZegel
import unicus.entity.Contactgegevens
import unicus.entity.Contactmoment
import unicus.entity.ExtendedAdres
import unicus.entity.ExtendedDossier
import unicus.entity.IdentiteitsDocument
import unicus.entity.Label
import unicus.entity.Partner
import java.util.*


data class ClientDTO(
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
        @JvmField var adressen: MutableList<ExtendedAdres>?,
        @JvmField var partner: MutableList<Partner>?,
        @JvmField var contactgegevens: MutableList<Contactgegevens>?,
        @JvmField var betaalgegevens: MutableList<Betaalgegevens>?,
        @JvmField var labels: MutableList<Label>?,
        @JvmField var contactmomenten: MutableList<Contactmoment>?,
        @JvmField var dossiers: MutableList<ExtendedDossier>?,
        @JvmField var identiteitsdocumenten: MutableList<IdentiteitsDocument>?,
        @JvmField var brpZegel: BrpZegel?
)
