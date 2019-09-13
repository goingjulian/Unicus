package unicus.scheduled

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import unicus.service.DuplicateService
import java.util.concurrent.atomic.AtomicBoolean

const val scanningInterval: Long = 600000
const val checkInterval: Long = 1000

@Component
class ScheduledScanningTasks {

    @Autowired
    private lateinit var duplicateService: DuplicateService

    private var enabled = AtomicBoolean(false)

    @Scheduled(fixedRate = scanningInterval)
    fun startDuplicateScan() {
        KotlinLogging.logger { }.info { "Scheduled scanning of duplicates has started" }

        val results = duplicateService.detectDuplicateClients()
        KotlinLogging.logger { }.info { "${results.size} results found after scheduled scanning" }
        KotlinLogging.logger { }.info { "Scheduled scanning of duplicates has finished" }
    }

    @Scheduled(fixedRate = checkInterval)
    fun checkIfScanShouldStart() {
        if (enabled.get()) {
            toggle()
            startDuplicateScan()
        }
    }

    fun toggle() {
        DuplicateService.progressPercentage = 0.0
        enabled.set(!enabled.get())
    }

}