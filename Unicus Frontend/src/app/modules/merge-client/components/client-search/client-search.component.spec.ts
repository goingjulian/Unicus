import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ClientSearchComponent} from './client-search.component';
import {ClientContainerService} from "../../services/client-container.service";
import {LoadindicatorComponent} from "../loadindicator/loadindicator.component";
import {HttpClient, HttpHandler} from "@angular/common/http";
import {ClientSearchService} from "../../services/client-search.service";
import {Persoon} from "../../../../models/persoon";

describe('ClientSearchComponent', () => {
  let component: ClientSearchComponent;
  let fixture: ComponentFixture<ClientSearchComponent>;
  let clientContainerService: ClientContainerService;
  let clientSearchService: ClientSearchService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ClientSearchComponent, LoadindicatorComponent],
      providers: [ClientContainerService, HttpHandler, HttpClient, ClientSearchService]
    });
    fixture = TestBed.createComponent(ClientSearchComponent);

    component = fixture.componentInstance;
    clientContainerService = TestBed.get(ClientContainerService);
    clientSearchService = TestBed.get(ClientSearchService);
  }));

  it('searchClients will be called and run', function () {
    spyOn(component, 'searchClients');
    component.searchClients('testing');
    expect(component.searchClients).toHaveBeenCalled();
  });

  it('isAllowed will return optionNotAllowed which is important for the styling of the element', function () {
    spyOn(clientContainerService, 'isClientOpen').and.returnValue(true);
    expect(component.isAllowed).toMatch('optionNotAllowed');
  });

  it('openClient wont open a result if isClientOpen returns true which means that that result is already opened', function () {
    spyOn(clientContainerService, 'isClientOpen').and.returnValue(true);
    spyOn(clientSearchService, 'getCustomer');
    component.openClient(new Persoon());
    expect(clientSearchService.getCustomer).not.toHaveBeenCalled();
  });

  it('openClient will open a result if isClientOpen returns false which means that that result is not already opened', function () {
    spyOn(clientContainerService, 'isClientOpen').and.returnValue(false);
    spyOn(clientSearchService, 'getCustomer');
    component.openClient(new Persoon());
    expect(clientSearchService.getCustomer).toHaveBeenCalled();
  });
});
