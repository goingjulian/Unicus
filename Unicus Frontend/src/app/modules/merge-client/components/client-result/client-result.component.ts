import {AfterViewInit, Component, OnInit} from '@angular/core';
import {ClientMergeService} from '../../services/client-merge.service';
import {Persoon} from '../../../../models/persoon';
import {Router} from '@angular/router';

@Component({
  selector: 'un-client-result',
  templateUrl: './client-result.component.html',
  styleUrls: ['./client-result.component.scss']
})
export class ClientResultComponent implements OnInit, AfterViewInit {

  private mergedClient: Persoon;

  constructor(private clientMergeService: ClientMergeService, private router: Router) {
  }

  ngOnInit() {
    this.clientMergeService.observableMergedClient$.subscribe(mergedClient => this.openClient(mergedClient));
  }

  ngAfterViewInit(): void {
    window.scroll(0, 0);
  }

  private openClient(client: Persoon) {
    this.mergedClient = client;
  }

  private backToHome() {
    this.router.navigateByUrl('/');
  }

}
