package unicus.dto

class FieldsToUpdateDTO(
        @JvmField var bsn: Boolean = false,
        @JvmField var relatienummer: Boolean = true,
        @JvmField var geslachtsnaam: Boolean = false,
        @JvmField var voornamen: Boolean = false,
        @JvmField var tussenvoegsels: Boolean = false,
        @JvmField var geslacht: Boolean = false,
        @JvmField var geboortedatum: Boolean = false,
        @JvmField var geboorteplaats: Boolean = false,
        @JvmField var geboorteland: Boolean = false,
        @JvmField var nationaliteit: Boolean = false,
        @JvmField var opmerking: Boolean = false,
        @JvmField var adressen: Boolean = false,
        @JvmField var partner: Boolean = false,
        @JvmField var contactgegevens: Boolean = false,
        @JvmField var betaalgegevens: Boolean = false,
        @JvmField var labels: Boolean = false,
        @JvmField var contactmomenten: Boolean = false,
        @JvmField var identiteitsdocumenten: Boolean = false
) {
    fun getFieldsToUpdate(): MutableList<String> {
        val fields: MutableList<String> = mutableListOf()
        this.javaClass.fields.forEach {
            if (it.getBoolean(this)) {
                fields.add(it.name)
            }
        }
        if (fields.contains("voornamen")) {
            fields.add("voorletters")
        }
        return fields
    }
}
