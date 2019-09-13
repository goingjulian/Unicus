package unicus.dao

import org.litote.kmongo.MongoOperator
import org.litote.kmongo.find
import org.springframework.stereotype.Repository
import unicus.dto.ClientDTO
import unicus.entity.Adres
import unicus.entity.Client
import unicus.entity.Dossier
import unicus.entity.ExtendedAdres
import unicus.entity.ExtendedDossier

@Repository
class ClientToDTODAO : DAO() {

    fun convertClientToDTO(client: Client): ClientDTO {
        logger.info {
            "Client with id: '${client.relatienummer} is being converted to DTO"
        }

        val clientDTO = ClientDTO(
                client.bsn, client.relatienummer,
                client.geslachtsnaam, client.voornamen,
                client.voorletters, client.tussenvoegsels,
                client.geslacht,
                client.geboortedatum,
                client.geboorteplaats,
                client.geboorteland, client.nationaliteit,
                client.opmerking, null,
                client.partner, client.contactgegevens,
                client.betaalgegevens, client.labels,
                client.contactmomenten, null,
                client.identiteitsdocumenten, client.brpZegel
        )

        if (client.dossiers != null) {
            logger.info {
                "client with id: '${client.relatienummer} has dossiers, fetching extra dossier data"
            }

            clientDTO.dossiers = getExtraDossierDataFromClient(client.dossiers!!)

            logger.info {
                "${clientDTO.dossiers!!.size} dossiers found"
            }
        }
        if (client.adressen != null) {
            logger.info {
                "client with id: '${client.relatienummer} has adresses, fetching extra adress data"
            }

            clientDTO.adressen = getExtraAdressDataFromClient(client.adressen!!)

            logger.info {
                "${clientDTO.adressen!!.size} adresses found"
            }
        }


        return clientDTO
    }

    fun getExtraDossierDataFromClient(dossiers: MutableList<Dossier>): MutableList<ExtendedDossier> {
        val dossierCollection = collection.getCollection<ExtendedDossier>(CollectionType.DOSSIER)
        val extendedDossiers = mutableListOf<ExtendedDossier>()
        var filter = ""

        dossiers.forEach {
            filter += "{ _id: '${it.dossiernummer}' }, "
        }

        filter = "{ ${MongoOperator.or}: [ $filter ] }"

        dossierCollection.find(filter).forEach { extendedDossiers.add(it) }

        return extendedDossiers
    }

    fun getExtraAdressDataFromClient(addresses: MutableList<Adres>): MutableList<ExtendedAdres> {
        val adressCollection = collection.getCollection<ExtendedAdres>(CollectionType.ADRESS)
        val extendedAdresses = mutableListOf<ExtendedAdres>()
        var filter = ""

        addresses.forEach {
            filter += "{_id: '${it.adresnummer}'}"
        }

        filter = "{ ${MongoOperator.or}: [ $filter ] }"

        adressCollection.find(filter).forEach {
            extendedAdresses.add(it)
        }

        return extendedAdresses
    }

}
