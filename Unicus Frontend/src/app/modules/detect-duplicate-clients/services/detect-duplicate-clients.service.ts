import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../../../environments/environment";
import {ReplaySubject} from "rxjs";
import {DuplicateScreenStates} from "../models/DuplicateScreenStates";

@Injectable({
  providedIn: 'root'
})

export class DetectDuplicateClientsService {

  private observableActiveScreen = new ReplaySubject<DuplicateScreenStates>();
  public observableActiveScreen$ = this.observableActiveScreen.asObservable();

  constructor(private http: HttpClient) { }

  fetchScanState() {
    const url = environment.baseUrl + environment.duplicateScanStateUrl;
    return this.http.get(url);
  }

  fetchScanProgress() {
    const url = environment.baseUrl + environment.duplicateScanProgressUrl;
    return this.http.get(url);
  }

  fetchDuplicateClients(min: number, max: number) {
    const url = environment.baseUrl + environment.duplicateGetResultsUrl + '?min=' + min + '&max=' + max;
    return this.http.get(url);
  }

  startScan() {
    const url = environment.baseUrl + environment.duplicateStartScanUrl;
    this.http.get(url).subscribe(data => console.log(data), err => console.log(err));
  }

  setActiveScreen(activeScreen: DuplicateScreenStates) {
    this.observableActiveScreen.next(activeScreen);
  }


}
