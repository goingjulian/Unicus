import {Injectable} from '@angular/core';
import {Persoon} from "../../../models/persoon";
import {FieldsToKeepDTO} from "../../../models/dto/fieldsToKeepDTO";
import {ClientTrackService} from "./client-track.service";
import {HttpClient} from "@angular/common/http";
import {ReplaySubject, Subject} from 'rxjs';
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ClientMergeService {

  private clientToKeep: Persoon;
  private clientToDelete: Persoon;
  private fieldsToKeep: FieldsToKeepDTO;

  private observableMergedClient = new ReplaySubject<Persoon>();
  public observableMergedClient$ = this.observableMergedClient.asObservable();

  private observableLoadIndicator = new Subject<boolean>();
  public observableLoadIndicator$ = this.observableLoadIndicator.asObservable();

  constructor(private httpClient: HttpClient, private clientTrackService: ClientTrackService) {}

  mergeClients(fieldsToKeep: FieldsToKeepDTO) {
    this.fieldsToKeep = fieldsToKeep;
    this.clientToKeep = this.clientTrackService.getClientToKeep();
    this.clientToDelete = this.clientTrackService.getClientToDelete();
    const endpointUrl = this.getMergeEndpoint(this.clientToKeep, this.clientToDelete);
    this.setLoadindicator(true);

    this.httpClient.put<Persoon>(endpointUrl, this.fieldsToKeep).subscribe(
      data => this.receiveMergedClient(this.clientTrackService.convertDataToClient(data)),
      err => this.receivedMergedClientError(err)
    );
  }

  private setLoadindicator(status: boolean) {
    this.observableLoadIndicator.next(status);
  }

  private getMergeEndpoint(clientToKeep: Persoon, clientToDelete: Persoon): string {
    return environment.baseUrl + environment.clientUrl + '/' + clientToKeep.getRelatienummer() + '?clienttocopyfrom=' + clientToDelete.getRelatienummer();
  }

  private receiveMergedClient(client: Persoon) {
    this.setLoadindicator(false);
    this.observableMergedClient.next(client);
  }

  private receivedMergedClientError(err: Response) {
    console.log(err);
  }
}
