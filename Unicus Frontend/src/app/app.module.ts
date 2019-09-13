import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {MergeClientModule} from './modules/merge-client/merge-client.module';
import {DetectDuplicateClientsModule} from './modules/detect-duplicate-clients/detect-duplicate-clients.module';
import {SharedComponentsModule} from './modules/shared-components/shared-components.module';
import {RouterModule} from "@angular/router";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    SharedComponentsModule,
    BrowserModule,
    AppRoutingModule,
    MergeClientModule,
    DetectDuplicateClientsModule
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {
}
