import {Component, OnDestroy, OnInit} from '@angular/core';
import {DetectDuplicateClientsService} from "../../services/detect-duplicate-clients.service";
import {ScanState} from "../../models/ScanState";
import {interval} from "rxjs";
import {DuplicateScreenStates} from "../../models/DuplicateScreenStates";

@Component({
  selector: 'detect-duplicate-clients-scan',
  templateUrl: './detect-duplicate-clients-scan.component.html',
  styleUrls: ['./detect-duplicate-clients-scan.component.scss']
})
export class DetectDuplicateClientsScanComponent implements OnInit, OnDestroy {

  private scanState = ScanState;
  private currentScanState: ScanState = ScanState.SCAN_IDLE;
  private currentScanProgress: number = 0;
  private maxScanProgress: number = 99;
  private minScanProgress: number = 0;

  private scanInterval = interval(500);
  private fetchSubscription;

  constructor(private detectDuplicateClientsService: DetectDuplicateClientsService) {
  }

  ngOnInit() {
    this.startSubscription();
  }

  ngOnDestroy() {
    this.toResults();
  }

  private startSubscription() {
    this.fetchSubscription = this.scanInterval.subscribe(() => this.controlScanProcess());
  }

  private controlScanProcess() {
    this.getScanState();
    if (this.currentScanState == ScanState.SCAN_DETECTING) {
      this.getScanProgress();
      if (this.currentScanProgress >= this.maxScanProgress) {
        this.toResults();
      }
    } else if (this.currentScanState == ScanState.SCAN_IDLE) {
      if (this.currentScanProgress >= this.maxScanProgress) {
        this.toResults();
      } else if (this.currentScanProgress > this.minScanProgress) {
        this.getScanProgress();
      }
    }
  }

  private getScanState() {
    this.detectDuplicateClientsService.fetchScanState().subscribe(
      data => {
        this.currentScanState = this.convertScanStateToEnum(JSON.parse(JSON.stringify(data)));
      },
      err => {
        this.currentScanState = ScanState.SCAN_ERROR;
      }
    );
  }

  private getScanProgress() {
    this.detectDuplicateClientsService.fetchScanProgress().subscribe(
      data => {
        this.currentScanProgress = parseInt(JSON.parse(JSON.stringify(data)));
      },
      err => {
        console.log(err);
      }
    );
  }

  private convertScanStateToEnum(scanStateString: string): ScanState {
    switch (scanStateString) {
      case 'FETCHING' :
        return ScanState.SCAN_FETCHING;
      case 'DETECTING' :
        return ScanState.SCAN_DETECTING;
      case 'ERROR' :
        return ScanState.SCAN_ERROR;
      default :
        return ScanState.SCAN_IDLE;
    }
  }

  private getScanMessage(): string {
    switch (this.currentScanState) {
      case ScanState.SCAN_FETCHING :
        return 'Clienten ophalen';
      case ScanState.SCAN_DETECTING :
        return 'Detecteren van dubbele clienten';
      case ScanState.SCAN_ERROR :
        return 'Er is een probleem opgetreden bij het scannen naar dubbele clienten';
      default :
        return 'Geen scan bezig';
    }
  }

  private startScan() {
    this.detectDuplicateClientsService.startScan();
    this.startSubscription();
  }

  private toResults() {
    this.currentScanProgress = 0;
    this.fetchSubscription.unsubscribe();
    this.detectDuplicateClientsService.setActiveScreen(DuplicateScreenStates.DETECT_DUPLICATE_RESULTS_SCREEN);
  }
}
