import {Component, Input, OnInit} from '@angular/core';
import {Persoon} from "../../../../models/persoon";
import {ClientContainerService} from '../../services/client-container.service';

@Component({
  selector: 'un-client-info',
  templateUrl: './client-info.component.html',
  styleUrls: ['./client-info.component.scss']
})
export class ClientInfoComponent {

  @Input() public client: Persoon;
  @Input() public showClose: boolean;

  constructor(private clientContainerService: ClientContainerService) {}

  private closeClient() {
    this.clientContainerService.removeClient(this.client);
  }
}
