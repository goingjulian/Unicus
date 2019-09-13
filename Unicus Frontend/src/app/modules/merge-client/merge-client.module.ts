import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ClientInfoComponent } from './components/client-info/client-info.component';
import { ClientSearchComponent } from './components/client-search/client-search.component';
import { ClientContainerComponent } from './components/client-container/client-container.component';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import { ClientSelectComponent } from './components/client-select/client-select.component';
import { MergeClientComponent } from './components/merge-client/merge-client.component';
import { ClientResultComponent } from './components/client-result/client-result.component';
import {ClientChooseComponent} from "./components/client-choose/client-choose.component";
import {SharedComponentsModule} from "../shared-components/shared-components.module";
import {AppRoutingModule} from "../../app-routing.module";

@NgModule({
  providers: [],
  declarations: [ClientInfoComponent, ClientSearchComponent, ClientContainerComponent, ClientSelectComponent, MergeClientComponent, ClientResultComponent, ClientChooseComponent],
  imports: [
    CommonModule,
    BrowserModule,
    HttpClientModule,
    SharedComponentsModule,
    AppRoutingModule
  ],
  exports: [MergeClientComponent]
})
export class MergeClientModule { }
