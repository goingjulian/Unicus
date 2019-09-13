import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DetectDuplicateClientsContainerComponent } from './detect-duplicate-clients-container.component';

describe('DetectDuplicateClientsContainerComponent', () => {
  let component: DetectDuplicateClientsContainerComponent;
  let fixture: ComponentFixture<DetectDuplicateClientsContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetectDuplicateClientsContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetectDuplicateClientsContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
