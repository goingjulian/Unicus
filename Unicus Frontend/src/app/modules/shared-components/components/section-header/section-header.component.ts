import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'section-header',
  templateUrl: './section-header.component.html',
  styleUrls: ['./section-header.component.scss']
})
export class SectionHeaderComponent {

  @Input() headerText: string;
  @Input() headerSubText: string;

  constructor() {
  }

}
