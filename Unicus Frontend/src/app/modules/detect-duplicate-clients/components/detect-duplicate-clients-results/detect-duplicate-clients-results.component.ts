import {AfterViewInit, Component, HostListener, OnInit} from '@angular/core';
import {DetectDuplicateClientsService} from "../../services/detect-duplicate-clients.service";
import {DuplicateScreenStates} from "../../models/DuplicateScreenStates";
import {JsonConvert, ValueCheckingMode} from "json2typescript";
import {DuplicateClientsDTO} from "../../../../models/dto/duplicateClientsDTO";
import {MergeDuplicateClientService} from "../../../shared-components/services/merge-duplicate-client.service";
import {Router} from "@angular/router";
import {ClientSearchService} from '../../../merge-client/services/client-search.service';

@Component({
  selector: 'detect-duplicate-clients-results',
  templateUrl: './detect-duplicate-clients-results.component.html',
  styleUrls: ['./detect-duplicate-clients-results.component.scss']
})
export class DetectDuplicateClientsResultsComponent implements OnInit, AfterViewInit {

  private duplicateClients;
  private loadingDuplicateClients: boolean = true;
  private stopLoadingResults: boolean = false;
  private nResults: number = 0;
  private resultsAddedOnScroll: number = 20;

  constructor(public router: Router, private detectDuplicateClientsService: DetectDuplicateClientsService, private mergeDuplicateClientService: MergeDuplicateClientService) {
  }

  ngOnInit(): void {
    this.mergeDuplicateClientService.observableBothClientsSet$.subscribe(data => this.switchToMergeClient())
  }

  ngAfterViewInit(): void {
    this.fetchResults();
  }

  private fetchResults() {
    this.loadingDuplicateClients = true;
    this.detectDuplicateClientsService.fetchDuplicateClients(this.nResults, this.nResults + this.resultsAddedOnScroll).subscribe(
      data => {
        this.setDuplicateResults(this.convertResponseToDuplicatesDTO(data));
      },
      err => {
        console.log(err);
        this.loadingDuplicateClients = false;
        this.stopLoadingResults = true;
      }
    );
  }

  private setDuplicateResults(duplicateClients: DuplicateClientsDTO[]) {
    this.nResults += this.resultsAddedOnScroll;
    if (duplicateClients.length == 0) {
      this.stopLoadingResults = true;
    } else if (!this.duplicateClients) {
      this.duplicateClients = duplicateClients;
    } else {
      this.duplicateClients = this.duplicateClients.concat(duplicateClients);
    }
    this.loadingDuplicateClients = false;
    this.checkNewResults();
  }

  @HostListener('window:scroll') onScroll() {
    this.checkNewResults();
  }

  private checkNewResults() {
    let windowHeight = window.innerHeight;
    if ((windowHeight > this.getDocumentHeight() || window.pageYOffset > (this.getDocumentHeight() - windowHeight * 2)) && !this.loadingDuplicateClients && !this.stopLoadingResults) {
      this.fetchResults();
    }
  }

  private getDocumentHeight() {
    return document.body.scrollHeight;
  }

  private convertResponseToDuplicatesDTO(data) {
    let jsonObj = JSON.parse(JSON.stringify(data));
    let jsonConvert: JsonConvert = new JsonConvert();
    jsonConvert.valueCheckingMode = ValueCheckingMode.ALLOW_NULL;
    return jsonConvert.deserialize(jsonObj, DuplicateClientsDTO);
  }

  private startDuplicateScan() {
    this.detectDuplicateClientsService.setActiveScreen(DuplicateScreenStates.DETECT_DUPLICATE_SCAN_SCREEN);
    this.detectDuplicateClientsService.startScan();
  }

  private openDuplicateClients(client: DuplicateClientsDTO) {
    this.mergeDuplicateClientService.setDuplicateClients(client.getMainRecord(), client.getDoubleRecord());
  }

  private switchToMergeClient() {
    this.router.navigateByUrl('/merge-client');
  }

  private reloadResults() {
    this.stopLoadingResults = false;
    this.nResults = 0;
    this.duplicateClients = null;
    this.fetchResults();
  }

  private getSimilarityString(score: number): string {
    score = Math.round(score);
    if (score <= 0) {
      return 'Hoog';
    } else if (score <= 3) {
      return 'Gemiddeld';
    } else {
      return 'Laag';
    }
  }

}
