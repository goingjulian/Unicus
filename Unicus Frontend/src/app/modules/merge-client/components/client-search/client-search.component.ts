import {Component, Input, OnInit} from '@angular/core';
import {Persoon} from "../../../../models/persoon";
import {ClientContainerService} from "../../services/client-container.service";
import {ClientSearchService} from "../../services/client-search.service";
import {MergeDuplicateClientService} from "../../../shared-components/services/merge-duplicate-client.service";

@Component({
  selector: 'un-client-search',
  templateUrl: './client-search.component.html',
  styleUrls: ['./client-search.component.scss']
})

export class ClientSearchComponent implements OnInit {

  @Input() autofocusInput: boolean;
  @Input() containerName: string;
  @Input() containerId: string;

  public clientSuggestions: Persoon[];
  public clientSuggestionsResponse: Response;
  public showLoadIndicator: boolean;

  constructor(private clientSearchService: ClientSearchService, private clientContainerService: ClientContainerService) {
  }

  ngOnInit(): void {
    this.clientSearchService.observableClient$.subscribe(data => this.clientContainerService.addClient(data));
    this.clientSearchService.observableClientSuggestions$.subscribe(data => this.saveSuggestions(data));
    this.clientSearchService.observableClientSuggestionsResponse$.subscribe(data => this.saveResponse(data));
    this.clientSearchService.observableLoadIndicator$.subscribe(data => this.onLoadIndicatorChange(data));
  }

  private searchTime: number = 500;
  private timeLeft: number;
  private inputTimer;

  private resetTimer() {
    this.timeLeft = this.searchTime;
    clearInterval(this.inputTimer);
  }

  private searchClients(searchQuery: string) {
    this.clientSuggestions = null;
    this.clientSuggestionsResponse = null;

    this.resetTimer();
    this.inputTimer = setInterval(() => {
      if (this.timeLeft > 0) {
        this.timeLeft -= 10;
      } else if (this.timeLeft == 0) {
        this.resetTimer();
        this.clientSearchService.getCustomerSuggestions(searchQuery);
      }
    }, 10)
  }

  private saveSuggestions(client: Persoon[]) {
    this.clientSuggestions = client;
    this.clientSuggestionsResponse = null;
  }

  private saveResponse(res: Response) {
    this.clientSuggestions = null;
    this.clientSuggestionsResponse = res;
  }

  private openClient(client: Persoon) {
    if (!this.clientContainerService.isClientOpen(client)) {
      this.clientContainerService.addClient(client);
    }
  }

  private onLoadIndicatorChange(data: boolean) {
    this.showLoadIndicator = data;
  }

  private showMessage(suggestions: Response) {
    switch (suggestions.status) {
      case 404:
        return "Geen resultaten.";
      case 400:
        return "Ongeldige invoer.";
      case 504:
        return "Het zoeken duurt te lang.";
      default :
        return "Er ging iets mis, probeer het later opnieuw.";
    }
  }

  private isAllowed(client: Persoon): string {
    if (this.clientContainerService.isClientOpen(client)) {
      return 'optionNotAllowed';
    }
  }

  private getClientNameString(client: Persoon): string {
    let name: string = '';
    if (client.getVoornamen() !== '-') {
      name += client.getVoornamen() + ' ';
    }
    if (client.getTussenvoegsels() !== '-') {
      name += client.getTussenvoegsels() + ' ';
    }
    if (client.getGeslachtsnaam() !== '-') {
      name += client.getGeslachtsnaam();
    }
    return name;
  }

  private getClientInfoString(client: Persoon): string {
    if (client.getGeslacht() !== '-' && client.getGeboortedatum() !== '-') {
      return client.getGeslacht() + ' - ' + client.getGeboortedatum();
    } else if (client.getGeslacht() !== '-') {
      return client.getGeslacht();
    } else if (client.getGeboortedatum() !== '-') {
      return client.getGeboortedatum();
    } else {
      return '-';
    }
  }

}
