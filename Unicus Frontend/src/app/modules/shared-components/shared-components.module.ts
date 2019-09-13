import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {LoaderCircleComponent} from "./components/loader-circle/loader-circle.component";
import { SectionHeaderComponent } from './components/section-header/section-header.component';
import { SectionMainComponent } from './components/section-main/section-main.component';
import {MergeDuplicateClientService} from "./services/merge-duplicate-client.service";

@NgModule({
  declarations: [LoaderCircleComponent, SectionHeaderComponent, SectionMainComponent],
  imports: [
    CommonModule
  ],
  exports: [LoaderCircleComponent, SectionHeaderComponent, SectionMainComponent],
  providers: [MergeDuplicateClientService]
})
export class SharedComponentsModule { }
