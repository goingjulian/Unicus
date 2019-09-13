import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ClientInfoComponent} from './client-info.component';
import {ClientContainerService} from "../../services/client-container.service";
import {ClientTrackService} from "../../services/client-track.service";


describe('ClientInfoComponent', () => {

  let component: ClientInfoComponent;
  let fixture: ComponentFixture<ClientInfoComponent>;
  let service: ClientContainerService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ClientInfoComponent],
    })
      .compileComponents();
  }));

  beforeEach(function () {
    service = new ClientContainerService(new ClientTrackService());
    component = new ClientInfoComponent(service);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

});
