package unicus.dao

import com.mongodb.MongoTimeoutException
import com.mongodb.client.MongoCollection
import mu.KotlinLogging
import org.litote.kmongo.aggregate
import org.springframework.stereotype.Repository
import unicus.dto.ClientDTO
import unicus.entity.Client
import unicus.exception.ClientIdNotValidException

@Repository
class GetClientByWildcardDAO : DAO() {

    @Throws(ClientIdNotValidException::class)
    fun getClientsByWildcard(searchCriteria: String): MutableList<ClientDTO> {
        val clientsFound = mutableListOf<ClientDTO>()
        val clientCollection: MongoCollection<Client> = collection.getCollection(CollectionType.CLIENT)

        try {

            clientCollection.aggregate<ClientDTO>(
                    "[{" +
                            "    \$lookup: {" +
                            "        from: '${collection.addressCollectionName}'," + //join de collection adressen op adressen.adresnummer als _id
                            "        localField: 'adressen.adresnummer'," +
                            "        foreignField: '_id'," +
                            "        as: 'adressen'" +
                            "    }" +
                            "}, {" +
                            "    \$lookup: {" +
                            "        from: '${collection.dossierCollectionName}'," + //join de collection dossiers op dossiers.dossiernummer als _id
                            "        localField: 'dossiers.dossiernummer'," +
                            "        foreignField: '_id'," +
                            "        as: 'dossiers'" +
                            "    }" +
                            "}, {" +
                            "    \$unwind: {" +
                            "        path: '\$adressen'," + //splits adressen array
                            "        preserveNullAndEmptyArrays: true" +
                            "    }" +
                            "}, {" +
                            "    \$unwind: {" +
                            "        path: '\$contactgegevens'," + //splits contactgegevens array
                            "        preserveNullAndEmptyArrays: true" +
                            "    }" +
                            "}, {" +
                            "    \$addFields: {" +
                            "        clientData: {" + //maak een nieuw veld aan met de naam clientData
                            "            \$concat: [{" + //concat alle onderstaande velden
                            "                    \$ifNull: [{" + //controleert of het veld niet null is
                            "                            \$toLower: {" + //convert data naar lowercase
                            "                                \$reduce: {" + //<!------------------------->
                            "                                    input: {" +
                            "                                        \$split: [" +
                            "                                            '\$voornamen'," +
                            "                                            ' '" +
                            "                                        ]" +
                            "                                    }," +
                            "                                    initialValue: ''," + // binnen dit gehele stuk worden spaties uit een string verwijderd.
                            "                                    'in': {" +
                            "                                        \$concat: [" +
                            "                                            '\$\$value'," +
                            "                                            '\$\$this'" +
                            "                                        ]" +
                            "                                    }" + //----------------------->
                            "                                }" +
                            "                            }" +
                            "                        }," +
                            "                        ''" +
                            "                    ]" +
                            "                }," +
                            "                {" +
                            "                    \$ifNull: [{" +
                            "                            \$toLower: {" +
                            "                                \$reduce: {" +
                            "                                    input: {" +
                            "                                        \$split: [" +
                            "                                            '\$tussenvoegsels'," +
                            "                                            ' '" +
                            "                                        ]" +
                            "                                    }," +
                            "                                    initialValue: ''," +
                            "                                    'in': {" +
                            "                                        \$concat: [" +
                            "                                            '\$\$value'," +
                            "                                            '\$\$this'" +
                            "                                        ]" +
                            "                                    }" +
                            "                                }" +
                            "                            }" +
                            "                        }," +
                            "                        ''" +
                            "                    ]" +
                            "                }," +
                            "                {" +
                            "                    \$ifNull: [{" +
                            "                            \$toLower: {" +
                            "                                \$reduce: {" +
                            "                                    input: {" +
                            "                                        \$split: [" +
                            "                                            '\$geslachtsnaam'," +
                            "                                            ' '" +
                            "                                        ]" +
                            "                                    }," +
                            "                                    initialValue: ''," +
                            "                                    'in': {" +
                            "                                        \$concat: [" +
                            "                                            '\$\$value'," +
                            "                                            '\$\$this'" +
                            "                                        ]" +
                            "                                    }" +
                            "                                }" +
                            "                            }" +
                            "                        }," +
                            "                        ''" +
                            "                    ]" +
                            "                }," +
                            "                {" +
                            "                    \$ifNull: [{" +
                            "                            \$toLower: '\$geboortedatum'" +
                            "                        }," +
                            "                        ''" +
                            "                    ]" +
                            "                }," +
                            "                {" +
                            "                    \$ifNull: [{" +
                            "                            \$toLower: {" +
                            "                                \$reduce: {" +
                            "                                    input: {" +
                            "                                        \$split: [" +
                            "                                            '\$adressen.straatnaam'," +
                            "                                            ' '" +
                            "                                        ]" +
                            "                                    }," +
                            "                                    initialValue: ''," +
                            "                                    'in': {" +
                            "                                        \$concat: [" +
                            "                                            '\$\$value'," +
                            "                                            '\$\$this'" +
                            "                                        ]" +
                            "                                    }" +
                            "                                }" +
                            "                            }" +
                            "                        }," +
                            "                        ''" +
                            "                    ]" +
                            "                }," +
                            "                {" +
                            "                    \$ifNull: [{" +
                            "                            \$toLower: '\$adressen.huisnummer'" +
                            "                        }," +
                            "                        ''" +
                            "                    ]" +
                            "                }," +
                            "                {" +
                            "                    \$ifNull: [{" +
                            "                            \$toLower: {" +
                            "                                \$reduce: {" +
                            "                                    input: {" +
                            "                                        \$split: [" +
                            "                                            '\$adressen.postcode'," +
                            "                                            ' '" +
                            "                                        ]" +
                            "                                    }," +
                            "                                    initialValue: ''," +
                            "                                    'in': {" +
                            "                                        \$concat: [" +
                            "                                            '\$\$value'," +
                            "                                            '\$\$this'" +
                            "                                        ]" +
                            "                                    }" +
                            "                                }" +
                            "                            }" +
                            "                        }," +
                            "                        ''" +
                            "                    ]" +
                            "                }," +
                            "                {" +
                            "                    \$ifNull: [{" +
                            "                            \$toLower: {" +
                            "                                \$reduce: {" +
                            "                                    input: {" +
                            "                                        \$split: [" +
                            "                                            '\$adressen.plaats'," +
                            "                                            ' '" +
                            "                                        ]" +
                            "                                    }," +
                            "                                    initialValue: ''," +
                            "                                    'in': {" +
                            "                                        \$concat: [" +
                            "                                            '\$\$value'," +
                            "                                            '\$\$this'" +
                            "                                        ]" +
                            "                                    }" +
                            "                                }" +
                            "                            }" +
                            "                        }," +
                            "                        ''" +
                            "                    ]" +
                            "                }," +
                            "                {" +
                            "                    \$ifNull: [{" +
                            "                            \$toLower: {" +
                            "                                \$reduce: {" +
                            "                                    input: {" +
                            "                                        \$split: [" +
                            "                                            '\$contactgegevens.nummeradres'," +
                            "                                            ' '" +
                            "                                        ]" +
                            "                                    }," +
                            "                                    initialValue: ''," +
                            "                                    'in': {" +
                            "                                        \$concat: [" +
                            "                                            '\$\$value'," +
                            "                                            '\$\$this'" +
                            "                                        ]" +
                            "                                    }" +
                            "                                }" +
                            "                            }" +
                            "                        }," +
                            "                        ''" +
                            "                    ]" +
                            "                }," +
                            "            ]" +
                            "        }" +
                            "    }" +
                            "}, {" +
                            "    \$match: {" +
                            "        \"clientData\": /$searchCriteria/" + //zoek in het nieuwe aangemaakte veld of de string gevonden kan worden
                            "    }" +
                            "}, {\n" +
                            "    \$addFields: {\n" +
                            "        positionInString: {\n" +
                            "            \$indexOfBytes: [\"\$clientData\", '$searchCriteria']\n" + //sla op wat de positie is van de gevonden string, wordt gebruikt voor sortering
                            "        }\n" +
                            "    }" +
                            "}, {" +
                            "    \$group: {" +
                            "        _id: \"\$_id\"," + //groepeer alle velden op _id
                            "        client: {" +
                            "            \$first: \"\$\$ROOT\"" + //pak het originele document met $$ROOT en voeg het toe aan het nieuwe document
                            "        }," +
                            "        adressen: {" +
                            "            \$addToSet: \"\$adressen\"" + //voeg alle adressen weer samen in een nieuw veld
                            "        }," +
                            "        contactgegevens: {" +
                            "            \$addToSet: \"\$contactgegevens\"" + //voeg alle contactgegevens weer samen in een nieuw veld
                            "        }" +
                            "    }" +
                            "}, {" +
                            "    \$addFields: {" +
                            "        \"client.adressen\": \"\$adressen\"," + //vervang de adressen van de client met het nieuwe samengevoegde veld
                            "        \"client.contactgegevens\": \"\$contactgegevens\"" + //vervang de contactgegevens van de client met het nieuwe samengevoegde veld
                            "    }" +
                            "}, {" +
                            "    \$replaceRoot: {" +
                            "        newRoot: \"\$client\"" + //verwijder alle velden behalve client om weer het originele document te hebben
                            "    }" +
                            "}, {\n" +
                            "    \$sort: {\n" +
                            "        positionInString: 1\n" + //sorteer op het veld positionInString zodat de personen waar de zoekcriteria eerder gevonden is bovenaan komen te staan.
                            "    }" +
                            "}, {\n" +
                            "    \$limit: 5" + //pak de eerste 5 gevonden documenten
                            "}]"
            ).forEach {
                clientsFound.add(it)
            }
        } catch (e: MongoTimeoutException) {
            KotlinLogging.logger {}.error {
                errorDBConnectionMessage
            }
        }

        if (clientsFound.isEmpty()) {
            throw ClientIdNotValidException()
        }

        return clientsFound
    }



}
