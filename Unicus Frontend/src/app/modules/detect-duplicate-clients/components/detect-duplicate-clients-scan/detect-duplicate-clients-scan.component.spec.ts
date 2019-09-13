import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetectDuplicateClientsScanComponent } from './detect-duplicate-clients-scan.component';

describe('DetectDuplicateClientsScanComponent', () => {
  let component: DetectDuplicateClientsScanComponent;
  let fixture: ComponentFixture<DetectDuplicateClientsScanComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetectDuplicateClientsScanComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetectDuplicateClientsScanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
