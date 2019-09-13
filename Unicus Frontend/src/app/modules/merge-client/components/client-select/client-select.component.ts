import {Component, OnInit} from '@angular/core';
import {ClientTrackService} from "../../services/client-track.service";
import {Persoon} from "../../../../models/persoon";
import {FieldsToKeepDTO} from "../../../../models/dto/fieldsToKeepDTO";
import {ClientMergeService} from "../../services/client-merge.service";

enum CustomerDataType {
  VOORNAMEN,
  GESLACHTSNAAM,
  TUSSENVOEGSEL,
  GESLACHT,
  GEBOORTEDATUM,
  GEBOORTEPLAATS,
  PARTNER,
  NATIONALITEIT,
  BSN,
  ADRES
}


@Component({
  selector: 'un-client-select',
  templateUrl: './client-select.component.html',
  styleUrls: ['./client-select.component.scss']
})

export class ClientSelectComponent implements OnInit {

  public fieldsToKeepDTO: FieldsToKeepDTO = new FieldsToKeepDTO();
  public customerDataTypes = CustomerDataType;

  public clientToDelete: Persoon;
  public clientToKeep: Persoon;
  public loadIndicator: boolean = false;

  constructor(private clientTrackService: ClientTrackService, private clientMergeService: ClientMergeService) {}

  ngOnInit() {
    this.clientTrackService.observableClientsSelected$.subscribe(client => this.openClients(client));
    this.clientMergeService.observableLoadIndicator$.subscribe(status => this.loadIndicator = status);
  }

  private openClients(clientToKeep: Persoon) {
    this.clientToKeep = clientToKeep;
    this.clientToDelete = this.clientTrackService.getClientToDelete();
  }

  private changeToKeep(attribute: CustomerDataType) {
    switch (attribute) {
      case CustomerDataType.VOORNAMEN:
        this.fieldsToKeepDTO.voornamen = !this.fieldsToKeepDTO.voornamen;
        break;
      case CustomerDataType.TUSSENVOEGSEL:
        this.fieldsToKeepDTO.tussenvoegsels = !this.fieldsToKeepDTO.tussenvoegsels;
        break;
      case CustomerDataType.GESLACHTSNAAM :
        this.fieldsToKeepDTO.geslachtsnaam = !this.fieldsToKeepDTO.geslachtsnaam;
        break;
      case CustomerDataType.GESLACHT:
        this.fieldsToKeepDTO.geslacht = !this.fieldsToKeepDTO.geslacht;
        break;
      case CustomerDataType.GEBOORTEDATUM:
        this.fieldsToKeepDTO.geboortedatum = !this.fieldsToKeepDTO.geboortedatum;
        break;
      case CustomerDataType.GEBOORTEPLAATS:
        this.fieldsToKeepDTO.geboorteplaats = !this.fieldsToKeepDTO.geboorteplaats;
        break;
      case CustomerDataType.PARTNER:
        this.fieldsToKeepDTO.partner = !this.fieldsToKeepDTO.partner;
        break;
      case CustomerDataType.NATIONALITEIT:
        this.fieldsToKeepDTO.nationaliteit = !this.fieldsToKeepDTO.nationaliteit;
        break;
      case CustomerDataType.BSN:
        this.fieldsToKeepDTO.bsn = !this.fieldsToKeepDTO.bsn;
        break;
      case CustomerDataType.ADRES:
        this.fieldsToKeepDTO.adressen = !this.fieldsToKeepDTO.adressen;
        break;
    }
  }

  private changeClassSelected(tablecell: HTMLElement) {
    let parentClasses = tablecell.parentElement.classList;
    (!parentClasses.contains('_selected')) ? parentClasses.add('_selected') : parentClasses.remove('_selected');
  }

  private getValue(attribute: CustomerDataType) {
    switch (attribute) {
      case CustomerDataType.GESLACHTSNAAM :
        return !this.fieldsToKeepDTO.geslachtsnaam ? this.clientToKeep.getGeslachtsnaam() : this.clientToDelete.getGeslachtsnaam();
      case CustomerDataType.TUSSENVOEGSEL :
        return !this.fieldsToKeepDTO.tussenvoegsels ? this.clientToKeep.getTussenvoegsels() : this.clientToDelete.getTussenvoegsels();
      case CustomerDataType.VOORNAMEN:
        return !this.fieldsToKeepDTO.voornamen ? this.clientToKeep.getVoornamen() : this.clientToDelete.getVoornamen();
      case CustomerDataType.GESLACHT:
        return !this.fieldsToKeepDTO.geslacht ? this.clientToKeep.getGeslacht() : this.clientToDelete.getGeslacht();
      case CustomerDataType.GEBOORTEDATUM:
        return !this.fieldsToKeepDTO.geboortedatum ? this.clientToKeep.getGeboortedatum() : this.clientToDelete.getGeboortedatum();
      case CustomerDataType.GEBOORTEPLAATS:
        return !this.fieldsToKeepDTO.geboorteplaats ? this.clientToKeep.getGeboorteplaats() : this.clientToDelete.getGeboorteplaats();
      case CustomerDataType.PARTNER:
        return !this.fieldsToKeepDTO.partner ? this.clientToKeep.getPartner() : this.clientToDelete.getPartner();
      case CustomerDataType.NATIONALITEIT:
        return !this.fieldsToKeepDTO.nationaliteit ? this.clientToKeep.getNationaliteit() : this.clientToDelete.getNationaliteit();
      case CustomerDataType.BSN:
        return !this.fieldsToKeepDTO.bsn ? this.clientToKeep.getBsn() : this.clientToDelete.getBsn();
      case CustomerDataType.ADRES:
        return !this.fieldsToKeepDTO.adressen ? this.clientToKeep.getAdressen() : this.clientToDelete.getAdressen();
    }
  }

  private mergeFinalClients() {
    this.clientMergeService.mergeClients(this.fieldsToKeepDTO);
  }
  
}
