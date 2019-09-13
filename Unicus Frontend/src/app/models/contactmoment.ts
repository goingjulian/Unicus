import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject("Contactmoment")
export class Contactmoment {

  @JsonProperty("soort", String, true) private soort: string = undefined;
  @JsonProperty("richting", String, true) private richting: string = undefined;
  @JsonProperty("medewerker", String, true) private medewerker: string = undefined;
  @JsonProperty("datumtijd", String, true) private datumtijd: string = undefined;
  @JsonProperty("onderwerp", String, true) private onderwerp: string = undefined;
  @JsonProperty("inhoud", String, true) private inhoud: string = undefined;

  getSoort = (): string => !this.soort ? '-' : this.soort;
  getRichting = (): string => !this.richting ? '-' : this.richting;
  getMedewerker = (): string => !this.medewerker ? '-' : this.medewerker;
  getDatumtijd = (): string => !this.datumtijd ? '-' : this.datumtijd;
  getOnderwerp = (): string => !this.onderwerp ? '-' : this.onderwerp;
  getInhoud = (): string => !this.inhoud ? '-' : this.inhoud;
}
