import {Injectable } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { Persoon } from '../../../models/persoon';
import { ClientTrackService } from './client-track.service';

@Injectable({
  providedIn: 'root'
})
export class ClientContainerService {

  private observableClientData = new ReplaySubject<Persoon>();
  public observableClientData$ = this.observableClientData.asObservable();

  constructor(private clientTrackService: ClientTrackService) {}

  addClient(client: Persoon) {
    this.clientTrackService.addClient(client);
    this.observableClientData.next(client);
  }

  removeClient(client: Persoon) {
    this.clientTrackService.removeClient(client);
    this.observableClientData.next(null);
  }

  isClientOpen(client: Persoon): boolean {
    return this.clientTrackService.checkIfClientIsAlreadyOpened(client);
  }

  selectClient(clientSelected: Persoon) {
    this.clientTrackService.selectClient(clientSelected);
  }

  allClientsSelected(): boolean {
    return this.clientTrackService.allClientsSelected();
  }

  getCurrentClient(containerId: number): Persoon {
    return this.clientTrackService.getCurrentClient(containerId);
  }

}
