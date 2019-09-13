package unicus.controller

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import unicus.dto.ClientDTO
import unicus.dto.FieldsToUpdateDTO
import unicus.exception.ClientIdNotValidException
import unicus.exception.ClientIdsAreTheSameException
import unicus.service.ClientService
import javax.servlet.http.HttpServletRequest

@CrossOrigin()
@RestController
@RequestMapping("/clients")
class ClientController {
    var startTime: Long = 0

    @Autowired
    private lateinit var clientService: ClientService

    private val logger = KotlinLogging.logger {}

    @GetMapping("/search")
    @ResponseBody
    fun getClientsByWildcard(@RequestParam(value = "searchcriteria", required = true) searchCriteria: String,
                             request: HttpServletRequest): ResponseEntity<MutableList<ClientDTO>> {
        startTime = System.currentTimeMillis()
        logger.info {
            "New request received from ip ${request.remoteAddr} " +
                    "port ${request.remotePort} " +
                    "on endpoint ${request.requestURI}, " +
                    "method ${request.method} "
        }

        return try {
            logger.info {
                "Fetching client with criteria '$searchCriteria'"
            }

            val results = clientService.getClientsByWildcard(searchCriteria)

            logger.info {
                "${results.size} results returned for searchcriteria: '$searchCriteria'"
            }
            logger.info {
                "Request took ${System.currentTimeMillis() - startTime} milliseconds"
            }

            ResponseEntity.ok().body(results)
        } catch (e: ClientIdNotValidException) {
            logger.warn {
                "Returning not found error to client, " +
                        "no results found for criteria '$searchCriteria'"
            }
            logger.info {
                "Request took ${System.currentTimeMillis() - startTime} milliseconds"
            }
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    fun updateClient(@PathVariable("id") clientToUpdateId: String,
                     @RequestParam(value = "clienttocopyfrom", required = true) clientToCopyFromId: String,
                     @RequestBody fieldsToUpdateDTO: FieldsToUpdateDTO,
                     request: HttpServletRequest): ResponseEntity<ClientDTO> {
        startTime = System.currentTimeMillis()
        logger.info {
            "New request received from ip ${request.remoteAddr} " +
                    "port ${request.remotePort} " +
                    "on endpoint ${request.requestURI}, " +
                    "method ${request.method} "
        }
        return try {
            logger.info {
                "Updating clients, client to update: $clientToUpdateId, client to delete: $clientToCopyFromId"
            }

            val updatedClient = clientService.updateClient(clientToUpdateId, clientToCopyFromId, fieldsToUpdateDTO)

            logger.info {
                "Client with id: $clientToUpdateId succesfully updated"
            }
            logger.info {
                "Request took ${System.currentTimeMillis() - startTime} milliseconds"
            }

            ResponseEntity.ok().body(updatedClient)
        } catch (e: ClientIdNotValidException) {
            logger.warn {
                "Returning not found error to client, " +
                        "one or more of the supplied user ID's was not found in the database"
            }
            logger.info {
                "Request took ${System.currentTimeMillis() - startTime} milliseconds"
            }
            ResponseEntity.notFound().build()
        } catch (f: ClientIdsAreTheSameException) {
            logger.warn {
                "Returning bad request error to client, " +
                        "the supplied clientId's are the same"
            }
            logger.info {
                "Request took ${System.currentTimeMillis() - startTime} milliseconds"
            }
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    fun getClientById(@PathVariable("id") clientId: String,
                      request: HttpServletRequest): ResponseEntity<ClientDTO> {
        logger.info {
            "New request received from ip ${request.remoteAddr} " +
                    "port ${request.remotePort} " +
                    "on endpoint ${request.requestURI}, " +
                    "method ${request.method}"
        }
        return try {
            ResponseEntity.ok().body(clientService.getClient(clientId))
        } catch (e: ClientIdNotValidException) {
            logger.info {
                "Returning not found error to client, the supplied use ID was not found in the database"
            }
            ResponseEntity.notFound().build()
        }
    }
}
