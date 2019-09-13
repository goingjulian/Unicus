package unicus.controller

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import unicus.dto.DuplicateClientDTO
import unicus.exception.NoScanningResultsAvailableException
import unicus.scheduled.ScheduledScanningTasks
import unicus.service.DuplicateService
import unicus.service.ScanState
import javax.servlet.http.HttpServletRequest

@CrossOrigin()
@RestController
@RequestMapping("/duplicates")
class DuplicateController {
    var startTime: Long = 0

    private val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var duplicateService: DuplicateService

    @Autowired
    private lateinit var scheduledScanningTasks: ScheduledScanningTasks

    @GetMapping("/initiatescan")
    fun startScan(request: HttpServletRequest): ResponseEntity<Any> {
        startTime = System.currentTimeMillis()
        logger.info {
            "New request received from ip ${request.remoteAddr} " +
                    "port ${request.remotePort} " +
                    "on endpoint ${request.requestURI}, " +
                    "method ${request.method} "
        }
        scheduledScanningTasks.toggle()

        logger.info {
            "Manual scan requested"
        }

        logger.info {
            "Request took ${System.currentTimeMillis() - startTime} milliseconds"
        }
        return ResponseEntity.ok().build()
    }

    @GetMapping("/getlatestresults")
    @ResponseBody
    fun getLatestScanResults(@RequestParam("min")
                             minResults: Int, @RequestParam("max")
                             maxResults: Int,
                             request: HttpServletRequest): ResponseEntity<MutableList<DuplicateClientDTO>> {
        startTime = System.currentTimeMillis()
        logger.info {
            "New request received from ip ${request.remoteAddr} " +
                    "port ${request.remotePort} " +
                    "on endpoint ${request.requestURI}, " +
                    "method ${request.method} "
        }
        return try {
            val results = duplicateService.getScannedDuplicatesList(minResults, maxResults)

            logger.info {
                "Scanning results found, returning ${results.size} results"
            }

            logger.info {
                "Request took ${System.currentTimeMillis() - startTime} milliseconds"
            }

            ResponseEntity.ok().body(results)
        } catch (e: NoScanningResultsAvailableException) {
            logger.info {
                "There are no results available, returning 404 to client"
            }

            logger.info {
                "Request took ${System.currentTimeMillis() - startTime} milliseconds"
            }

            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/getscanstate")
    @ResponseBody
    fun getScanStatus(request: HttpServletRequest): ResponseEntity<ScanState> {
        startTime = System.currentTimeMillis()
        logger.info {
            "New request received from ip ${request.remoteAddr} " +
                    "port ${request.remotePort} " +
                    "on endpoint ${request.requestURI}, " +
                    "method ${request.method} "
        }

        val state = DuplicateService.scanState

        logger.info {
            "Current scan state: $state"
        }

        logger.info {
            "Request took ${System.currentTimeMillis() - startTime} milliseconds"
        }

        return ResponseEntity.ok().body(state)
    }

    @GetMapping("/getprogress")
    @ResponseBody
    fun getScanProgressPercentage(request: HttpServletRequest): ResponseEntity<Int> {
        startTime = System.currentTimeMillis()
        logger.info {
            "New request received from ip ${request.remoteAddr} " +
                    "port ${request.remotePort} " +
                    "on endpoint ${request.requestURI}, " +
                    "method ${request.method} "
        }

        val progress = duplicateService.getProgressInt()

        logger.info {
            "Returning progress: $progress"
        }

        logger.info {
            "Request took ${System.currentTimeMillis() - startTime} milliseconds"
        }

        return ResponseEntity.ok().body(progress)
    }
}