import {AfterViewChecked, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Persoon} from "../../../../models/persoon";
import {ClientMergeService} from "../../services/client-merge.service";
import {ClientTrackService} from "../../services/client-track.service";
import {MergeDuplicateClientService} from "../../../shared-components/services/merge-duplicate-client.service";

@Component({
  selector: 'un-merge-client',
  templateUrl: './merge-client.component.html',
  styleUrls: ['./merge-client.component.scss'],
  providers: [ClientTrackService, ClientMergeService]
})

export class MergeClientComponent implements OnInit, AfterViewChecked {

  @Input('boxName') boxName: string;

  private clientMerged: Persoon;
  private clientToKeep: Persoon;

  private client1: Persoon;
  private client2: Persoon;

  constructor(private clientTrackService: ClientTrackService, private clientMergeService: ClientMergeService, private mergeDuplicateClientService: MergeDuplicateClientService) {
  }

  ngOnInit() {
    this.clientTrackService.observableClientsSelected$.subscribe(client => this.clientToKeep = client);
    this.clientMergeService.observableMergedClient$.subscribe(client => this.clientMerged = client);
    this.client1 = this.mergeDuplicateClientService.getClient1();
    this.client2 = this.mergeDuplicateClientService.getClient2();
  }

  ngAfterViewChecked() {
    window.scrollTo(0, 0);
  }

}
