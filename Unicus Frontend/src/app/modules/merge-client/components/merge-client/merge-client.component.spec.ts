import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MergeClientComponent } from './merge-client.component';

describe('MergeClientComponent', () => {
  let component: MergeClientComponent;
  let fixture: ComponentFixture<MergeClientComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MergeClientComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MergeClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
