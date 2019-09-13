import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientChooseComponent } from './client-choose.component';

describe('ClientChooseComponent', () => {
  let component: ClientChooseComponent;
  let fixture: ComponentFixture<ClientChooseComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClientChooseComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientChooseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
