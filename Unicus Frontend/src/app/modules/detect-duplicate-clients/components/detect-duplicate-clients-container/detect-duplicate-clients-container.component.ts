import { Component, OnInit } from '@angular/core';
import {DetectDuplicateClientsService} from "../../services/detect-duplicate-clients.service";
import {DuplicateScreenStates} from "../../models/DuplicateScreenStates";

@Component({
  selector: 'duplicate-clients-container',
  templateUrl: './detect-duplicate-clients-container.component.html',
  styleUrls: ['./detect-duplicate-clients-container.component.scss']
})

export class DetectDuplicateClientsContainerComponent implements OnInit {

  private duplicateScreenStates = DuplicateScreenStates;
  private activeScreen = DuplicateScreenStates.DETECT_DUPLICATE_RESULTS_SCREEN;

  constructor(private detectDuplicateClientsService: DetectDuplicateClientsService) { }

  ngOnInit() {
    this.detectDuplicateClientsService.observableActiveScreen$.subscribe(
      data => {
        this.activeScreen = data;
      },
      err => {
        console.log(err);
      }
    );
  }

}
