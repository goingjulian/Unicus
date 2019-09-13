import {JsonObject, JsonProperty} from "json2typescript";
import {Vis} from "./vis";

@JsonObject("Persoon")
export class IdentiteitsDocument {

  @JsonProperty("type", String, true) private type: string = undefined;
  @JsonProperty("nummer", String, true) private nummer: string = undefined;
  @JsonProperty("land", String, true) private land: string = undefined;
  @JsonProperty("afgiftedatum", String, true) private afgiftedatum: string = undefined;
  @JsonProperty("afgifteplaats", String, true) private afgifteplaats: string = undefined;
  @JsonProperty("geldigtot", String, true) private geldigtot: string = undefined;
  @JsonProperty("scandocument", String, true) private scandocument: string = undefined;
  @JsonProperty("vis", [Vis], true) private vis: Array<Vis> = undefined;

  getType = (): string => !this.type ? '-' : this.type;
  getNummer = (): string => !this.nummer ? '-' : this.nummer;
  getLand = (): string => !this.land ? '-' : this.land;
  getAfgifteDatum = (): string => !this.afgiftedatum ? '-' : this.afgiftedatum;
  getafgifteplaats = (): string => !this.afgifteplaats ? '-' : this.afgifteplaats;
  getGeldigtot = (): string => !this.geldigtot ? '-' : this.geldigtot;
  getScandocument = (): string => !this.scandocument ? '-' : this.scandocument;

  getVis = () => !this.vis ? null : this.vis;
}
