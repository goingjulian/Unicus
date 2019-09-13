import {Injectable, OnInit} from '@angular/core';
import {Persoon} from '../../../models/persoon';
import {environment} from '../../../../environments/environment';
import {ClientSearchService} from '../../merge-client/services/client-search.service';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MergeDuplicateClientService {

  private client1: Persoon;
  private client2: Persoon;

  private observableBothClientsSet = new Subject<true>();
  public observableBothClientsSet$ = this.observableBothClientsSet.asObservable();

  constructor(private clientSearchService: ClientSearchService) {
    this.clientSearchService.observableClientById$.subscribe(
      data => this.onClientFetched(data),
      err => console.log(err)
    );
  }

  setDuplicateClients(client1: Persoon, client2: Persoon) {
    this.clientSearchService.getClientById(client1.getRelatienummer());
    this.clientSearchService.getClientById(client2.getRelatienummer());
  }

  getClient1(): Persoon {
    let client = this.client1;
    this.client1 = null;
    return client;
  }

  getClient2(): Persoon {
    let client = this.client2;
    this.client2 = null;
    return client;
  }

  onClientFetched(client: Persoon) {
    if (!this.client1) {
      this.client1 = client;
    } else if (!this.client2) {
      this.client2 = client;
    }

    if(this.client1 != undefined && this.client2 != undefined) {
      this.observableBothClientsSet.next(true);
    }

  }
}
