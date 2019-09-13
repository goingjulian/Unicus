import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject("Partner")
export class Partner {

  @JsonProperty("persoon", String, true) private persoon: string = undefined;
  @JsonProperty("soort", String, true) private soort: string = undefined;
  @JsonProperty("datumstart", String, true) private datumstart: string = undefined;
  @JsonProperty("plaatssluiting", String, true) private plaatssluiting: string = undefined;
  @JsonProperty("landsluiting", String, true) private landsluiting: string = undefined;

  getPersoon = (): string => !this.persoon ? '-' : this.persoon;
  getSoort = (): string => !this.soort ? '-' : this.soort;
  getDatumstart = (): string => !this.datumstart ? '-' : this.datumstart;
  getPlaatssluiting = (): string => !this.plaatssluiting ? '-' : this.plaatssluiting;
  getLandsluiting = (): string => !this.landsluiting ? '-' : this.landsluiting;
}
