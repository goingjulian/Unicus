import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {MergeClientComponent} from "./modules/merge-client/components/merge-client/merge-client.component";
import {DetectDuplicateClientsContainerComponent} from "./modules/detect-duplicate-clients/components/detect-duplicate-clients-container/detect-duplicate-clients-container.component";

const routes: Routes = [
  {path: 'merge-client', component: MergeClientComponent},
  {path: 'duplicates', component: DetectDuplicateClientsContainerComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {scrollPositionRestoration: 'enabled'})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
