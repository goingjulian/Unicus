import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Subject} from 'rxjs';
import {Persoon} from '../../../models/persoon';
import {ClientTrackService} from './client-track.service';
import {environment} from '../../../../environments/environment';


@Injectable({
  providedIn: 'root'
})

export class ClientSearchService {

  private observableClient = new Subject<Persoon>();
  public observableClient$ = this.observableClient.asObservable();

  private observableClientSuggestions = new Subject<Persoon[]>();
  public observableClientSuggestions$ = this.observableClientSuggestions.asObservable();

  private observableClientSuggestionsResponse = new Subject<Response>();
  public observableClientSuggestionsResponse$ = this.observableClientSuggestionsResponse.asObservable();

  private observableLoadIndicator = new Subject<boolean>();
  public observableLoadIndicator$ = this.observableLoadIndicator.asObservable();

  private observableClientById = new Subject<Persoon>();
  public observableClientById$ = this.observableClientById.asObservable();

  constructor(private httpClient: HttpClient, private clientTrackService: ClientTrackService) {
  }

  getCustomerSuggestions(searchQuery: string) {
    if (searchQuery.length >= 1) {
      const endpointUrl = this.getEndpointSearch(searchQuery);
      this.observableLoadIndicator.next(true);
      this.httpClient.get(endpointUrl).subscribe(
        data => this.onCustomerSuggestionReceived(this.clientTrackService.convertDataToClient(data)),
        err => this.onCustomerSuggestionError(err)
      );
    }
  }

  getClientById(clientId: string) {
    this.httpClient.get(this.getEndpointClientId(clientId)).subscribe(
      data => this.onClientByIdReceived(this.clientTrackService.convertDataToClient(data)),
      err => console.log(err)
    );
  }

  private getEndpointClientId(clientId: string): string {
    return environment.baseUrl + environment.clientUrl + '/' + clientId;
  }

  private onCustomerSuggestionReceived(client: Persoon[]) {
    this.observableLoadIndicator.next(false);
    this.observableClientSuggestions.next(client);
  }

  private onCustomerSuggestionError(err: Response) {
    this.observableLoadIndicator.next(false);
    this.observableClientSuggestionsResponse.next(err);
  }

  private getEndpointSearch(searchQuery: string): string {
    return environment.baseUrl + environment.clientUrl + '/search?searchcriteria=' + searchQuery;
  }

  private onClientByIdReceived(client: Persoon) {
    this.observableClientById.next(client);
  }
}
