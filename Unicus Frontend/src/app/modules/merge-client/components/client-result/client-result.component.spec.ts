import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientResultComponent } from './client-result.component';

describe('ClientResultComponent', () => {
  let component: ClientResultComponent;
  let fixture: ComponentFixture<ClientResultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClientResultComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
