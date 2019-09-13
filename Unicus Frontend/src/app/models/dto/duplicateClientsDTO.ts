import {Persoon} from '../persoon';
import {JsonObject, JsonProperty} from 'json2typescript';

@JsonObject("DuplicateClientsDTO")
export class DuplicateClientsDTO {

  constructor() {
  }

  @JsonProperty("mainRecord", Persoon, true) private mainRecord: Persoon = undefined;
  @JsonProperty("doubleRecord", Persoon, true) private doubleRecord: Persoon = undefined;
  @JsonProperty("similarityScore", Number, true) private similarityScore: number = undefined;

  getMainRecord = (): Persoon => !this.mainRecord ? new Persoon() : this.mainRecord;
  getDoubleRecord = (): Persoon => !this.doubleRecord ? new Persoon() : this.doubleRecord;
  getSimilarityScore = () => {
    if (this.similarityScore <= 0 || this.similarityScore > 0) {
      return this.similarityScore;
    } else {
      return '-';
    }
  }
}
