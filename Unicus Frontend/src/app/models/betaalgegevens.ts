import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject('Betaalgegevens')
export class Betaalgegevens {

  @JsonProperty("iban", String) private iban: string = undefined;
  @JsonProperty("rekening_tnv", String) private rekening_tnv: string = undefined;

  getIban = (): string => !this.iban ? '-' : this.iban;
  getRekening_tnv = (): string => !this.rekening_tnv ? '-' : this.rekening_tnv;
}
