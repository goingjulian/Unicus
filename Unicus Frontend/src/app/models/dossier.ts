import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject("Dossier")
export class Dossier {

  @JsonProperty("_id", String, true) private dossiernummer: string = undefined;
  @JsonProperty("soort", Number, true) private soort: number = undefined;
  @JsonProperty("naam", String, true) private naam: string = undefined;

  getDossiernummer = (): string => !this.dossiernummer ? '-' : this.dossiernummer;
  getSoort = () => !this.soort ? '-' : this.soort;
  getNaam = (): string => !this.naam ? '-' : this.naam;
}
