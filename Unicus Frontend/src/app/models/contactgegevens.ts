import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject("Contactgegevens")
export class Contactgegevens {

  @JsonProperty("type", String, true) private type: string = undefined;
  @JsonProperty("zakelijkprive", String, true) private zakelijkprive: string = undefined;
  @JsonProperty("nummeradres", String, true) private nummeradres: string = undefined;

  getType = (): string => !this.type ? '-' : this.type;
  getZakelijkPrive = (): string => !this.zakelijkprive ? '-' : this.zakelijkprive;
  getNummeradres = (): string => !this.nummeradres ? '-' : this.nummeradres;
}
