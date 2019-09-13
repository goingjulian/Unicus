import {Component, Input, OnInit} from '@angular/core';
import {ClientSearchService} from '../../services/client-search.service';
import {ClientContainerService} from "../../services/client-container.service";
import {Persoon} from "../../../../models/persoon";

@Component({
  selector: 'un-client-container',
  templateUrl: './client-container.component.html',
  styleUrls: ['./client-container.component.scss'],
  providers: [ClientContainerService, ClientSearchService]
})
export class ClientContainerComponent implements OnInit {

  @Input() autofocusInput: boolean;
  @Input() containerName: string;
  @Input() containerId: number;
  @Input() duplicateClient: Persoon;

  private client: Persoon;

  constructor(private clientContainerService: ClientContainerService) {}

  ngOnInit() {
    this.clientContainerService.observableClientData$.subscribe(client => this.openClient(client));
    if (this.duplicateClient != null) {
      console.log(this.duplicateClient.getGeslachtsnaam());
      this.openClient(this.duplicateClient);
      this.openDuplicateClient(this.duplicateClient);
    }
  }

  private openClient(client: Persoon) {
    this.client = client;
  }

  private openDuplicateClient(client: Persoon) {
    if (!this.clientContainerService.isClientOpen(client)) {
      this.clientContainerService.addClient(client);
    }
  }

}
