import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetectDuplicateClientsResultsComponent } from './detect-duplicate-clients-results.component';

describe('DetectDuplicateClientsResultsComponent', () => {
  let component: DetectDuplicateClientsResultsComponent;
  let fixture: ComponentFixture<DetectDuplicateClientsResultsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetectDuplicateClientsResultsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetectDuplicateClientsResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
