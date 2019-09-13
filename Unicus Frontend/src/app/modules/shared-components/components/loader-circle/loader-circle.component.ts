import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'loader-circle',
  templateUrl: './loader-circle.component.html',
  styleUrls: ['./loader-circle.component.scss']
})
export class LoaderCircleComponent {

  @Input() colorLoader: string = 'rgb(255, 255, 255)';

  constructor() { }

}
