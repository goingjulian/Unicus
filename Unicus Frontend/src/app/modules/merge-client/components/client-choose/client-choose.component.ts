import { Component, OnInit } from '@angular/core';
import {ClientContainerService} from '../../services/client-container.service';
import {Persoon} from '../../../../models/persoon';

@Component({
  selector: 'un-client-choose',
  templateUrl: './client-choose.component.html',
  styleUrls: ['./client-choose.component.scss']
})

export class ClientChooseComponent implements OnInit {

  private client: Persoon;

  constructor(private clientContainerService: ClientContainerService) {}

  ngOnInit() {
    this.clientContainerService.observableClientData$.subscribe(client => this.openClient(client));
  }

  private openClient(client: Persoon) {
    this.client = client;
  }

  private selectClient() {
    this.clientContainerService.selectClient(this.client);
  }

}
