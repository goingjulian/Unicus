import {Injectable} from '@angular/core';
import {ReplaySubject} from 'rxjs';
import {Persoon} from '../../../models/persoon';
import {JsonConvert, ValueCheckingMode} from 'json2typescript';

@Injectable({
  providedIn: 'root'
})
export class ClientTrackService {

  private registeredClients: Persoon[] = [null, null];
  private clientToKeep: Persoon;
  private clientToDelete: Persoon;

  private observableClientsSelected = new ReplaySubject<Persoon>();
  public observableClientsSelected$ = this.observableClientsSelected.asObservable();

  constructor() {
  }

  getClientToKeep(): Persoon {
    return this.clientToKeep;
  }

  getClientToDelete(): Persoon {
    return this.clientToDelete;
  }

  addClient(client: Persoon) {
    if (!this.checkIfClientIsAlreadyOpened(client)) {
      const emptyIndex = this.registeredClients.findIndex(saved => saved == null);
      this.registeredClients[emptyIndex] = client;
    }
  }

  removeClient(client: Persoon) {
    this.registeredClients.forEach((it, index, array) => {
      if (it != null && it.getRelatienummer() == client.getRelatienummer()) {
        this.registeredClients[index] = null;
      }
    });
  }

  selectClient(clientToKeep: Persoon) {
    this.clientToKeep = clientToKeep;
    this.clientToDelete = this.findClientToDelete(clientToKeep);
    this.observableClientsSelected.next(clientToKeep);
  }

  allClientsSelected(): boolean {
    let clientsSelected: number = 0;
    this.registeredClients.forEach((element) => {
      if (element) {
        clientsSelected++;
      }
    });
    return clientsSelected == this.registeredClients.length;
  }

  convertDataToClient(data: any) {
    let jsonObj = JSON.parse(JSON.stringify(data));
    let jsonConvert: JsonConvert = new JsonConvert();
    jsonConvert.valueCheckingMode = ValueCheckingMode.ALLOW_NULL;
    return jsonConvert.deserialize(jsonObj, Persoon);
  }

  private findClientToDelete(clientToKeep: Persoon) {
    return this.registeredClients.find(client => client.getRelatienummer() !== clientToKeep.getRelatienummer());
  }

  checkIfClientIsAlreadyOpened(client: Persoon): boolean {
    let clientInArray = function (element: Persoon, index, array) {
      return element !== null && element.getRelatienummer() == client.getRelatienummer();
    };
    return this.registeredClients.some(clientInArray);
  }

  getCurrentClient(containerId: number): Persoon {
    return this.registeredClients[containerId];
  }
}
