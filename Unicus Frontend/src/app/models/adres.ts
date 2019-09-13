import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject('Adres')
export class Adres {

  @JsonProperty("begindatum", String, true) private begindatum: string = undefined;
  @JsonProperty("gemeente", String, true) private gemeente: string = undefined;
  @JsonProperty("huisnummer", Number, true) private huisnummer: number = undefined;
  @JsonProperty("land", String, true) private land: string = undefined;
  @JsonProperty("type:", String, true) private type: string = undefined;
  @JsonProperty("straatnaam", String, true) private straatnaam: string = undefined;
  @JsonProperty("postcode", String, true) private postcode: string = undefined;

  getHuisnummer = () => !this.huisnummer ? '' : this.huisnummer;
  getType = (): string => !this.type ? '-' : this.type;
  getBegindatum = (): string => !this.begindatum ? '-' : this.begindatum;
  getStraatnaam = (): string => !this.straatnaam ? '-' : this.straatnaam;
  getGemeente = (): string => !this.gemeente ? '' : this.gemeente;
  getPostcode = (): string => !this.postcode ? '' : this.postcode;

  getShortAddressFormat(): string {
    if (this.getHuisnummer()) {
      return this.getStraatnaam() + ' ' + this.getHuisnummer();
    } else if (this.getStraatnaam()) {
      return this.getStraatnaam();
    } else if (this.getGemeente()) {
      return this.getGemeente();
    } else {
      return ''
    }
  }

  getLongAddressFormat(): string {
    let string;
    if (this.getStraatnaam() != null) {
      string = this.getStraatnaam();
      if (this.getHuisnummer() != null) {
        string += ' ' + this.getHuisnummer();
      }
    }
    if (this.getPostcode() != null) {
      string += ', ' + this.getPostcode();
    }
    if (this.getGemeente() != null) {
      string += ', ' + this.getGemeente();
    }
    if (string != null) {
      return string;
    } else {
      return '-';
    }
  }

}
