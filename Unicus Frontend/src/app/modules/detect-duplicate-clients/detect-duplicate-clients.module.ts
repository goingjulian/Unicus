import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DetectDuplicateClientsResultsComponent } from './components/detect-duplicate-clients-results/detect-duplicate-clients-results.component';
import { DetectDuplicateClientsScanComponent } from "./components/detect-duplicate-clients-scan/detect-duplicate-clients-scan.component";
import { DetectDuplicateClientsContainerComponent } from "./components/detect-duplicate-clients-container/detect-duplicate-clients-container.component";
import {SharedComponentsModule} from "../shared-components/shared-components.module";
import {AppRoutingModule} from "../../app-routing.module";

@NgModule({
  declarations: [DetectDuplicateClientsResultsComponent, DetectDuplicateClientsScanComponent, DetectDuplicateClientsContainerComponent],
  imports: [
    CommonModule,
    SharedComponentsModule,
    AppRoutingModule
  ],
  exports: [DetectDuplicateClientsContainerComponent]
})

export class DetectDuplicateClientsModule { }
