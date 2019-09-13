import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject("Vis")
export class Vis {

  @JsonProperty("status", String, true) private status: string = undefined;
  @JsonProperty("verificatiedatum", String, true) private verificatiedatum: string = undefined;
  @JsonProperty("document", String, true) private document: string = undefined;

  getStatus = (): string => !this.status ? '-' : this.status;
  getVerificatiedatum = (): string => !this.verificatiedatum ? '-' : this.verificatiedatum;
  getDocument = (): string => !this.document ? '-' : this.document;
}
