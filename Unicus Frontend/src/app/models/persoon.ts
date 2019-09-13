import {JsonObject, JsonProperty} from "json2typescript";
import {Adres} from "./adres";
import {Betaalgegevens} from "./betaalgegevens";
import {BrpZegel} from "./brpzegel";
import {Contactgegevens} from "./contactgegevens";
import {Contactmoment} from "./contactmoment";
import {Dossier} from "./dossier";
import {IdentiteitsDocument} from "./identiteitsDocument";
import {Label} from "./label";
import {Partner} from "./partner";

@JsonObject("Persoon")
export class Persoon {

  @JsonProperty("_id", String, true) private _id: string;
  @JsonProperty("bsn", String, true) private bsn: string = undefined;
  @JsonProperty("relatienummer", String, true) private relatienummer: string = undefined;
  @JsonProperty("geslachtsnaam", String, true) private geslachtsnaam: string = undefined;
  @JsonProperty("voornamen", String, true) private voornamen: string = undefined;
  @JsonProperty("voorletters", String, true) private voorletters: string = undefined;
  @JsonProperty("tussenvoegsels", String, true) private tussenvoegsels: string = undefined;
  @JsonProperty("geslacht", String, true) private geslacht: string = undefined;
  @JsonProperty("geboortedatum", String, true) private geboortedatum: string = undefined;
  @JsonProperty("geboorteplaats", String, true) private geboorteplaats: string = undefined;
  @JsonProperty("geboorteland", String, true) private geboorteland: string = undefined;
  @JsonProperty("nationaliteit", String, true) private nationaliteit: string = undefined;
  @JsonProperty("opmerking", String, true) private opmerking: string = undefined;
  @JsonProperty("adressen", [Adres], true) private adressen: Array<Adres> = undefined;
  @JsonProperty("partner", [Partner], true) private partner: Array<Partner> = undefined;
  @JsonProperty("contactgegevens", [Contactgegevens], true) private contactgegevens: Array<Contactgegevens> = undefined;
  @JsonProperty("betaalgegevens", [Betaalgegevens], true) private betaalgegevens: Array<Betaalgegevens> = undefined;
  @JsonProperty("labels", [Label], true) private labels: Array<Label> = undefined;
  @JsonProperty("contactmomenten", [Contactmoment], true) private contactmomenten: Array<Contactmoment> = undefined;
  @JsonProperty("dossiers", [Dossier], true) private dossiers: Array<Dossier> = undefined;
  @JsonProperty("identiteitsdocumenten", [IdentiteitsDocument], true) private identiteitsdocumenten: Array<IdentiteitsDocument> = undefined;
  @JsonProperty("brpZegel", BrpZegel, true) private brpZegel: BrpZegel = undefined;

  private nullString: string = "-";

  getBsn = (): string => !this.bsn ? this.nullString : this.bsn;
  getRelatienummer = (): string => !this.relatienummer ? this.nullString : this.relatienummer;
  getGeslachtsnaam = (): string => !this.geslachtsnaam ? this.nullString : this.geslachtsnaam;
  getVoornamen = (nullString: string = this.nullString): string => !this.voornamen ? nullString : this.voornamen;
  getVoorletters = (nullString: string = this.nullString): string => !this.voorletters ? nullString : this.voorletters;
  getTussenvoegsels = (nullString = ""): string => !this.tussenvoegsels ? nullString : this.tussenvoegsels;
  getGeslacht = (): string => !this.geslacht ? this.nullString : this.geslacht;

  getGeboortedatum = (): string => !this.geboortedatum ? this.nullString :
    this.geboortedatum.substring(8, 10) + "-" + this.geboortedatum.substring(5, 7) + "-" + this.geboortedatum.substring(0, 4);

  getGeboorteplaats = (): string => !this.geboorteplaats ? this.nullString : this.geboorteplaats;
  getGeboorteland = (): string => !this.geboorteland ? this.nullString : this.geboorteland;
  getNationaliteit = (): string => !this.nationaliteit ? this.nullString : this.nationaliteit;
  getOpmerking = (): string => !this.opmerking ? this.nullString : this.opmerking;

  getAdressen = () => !this.adressen || this.adressen.length <= 0 ? null : this.adressen;
  getPartner = () => !this.partner ? null : this.partner;
  getContactgegevens = () => !this.contactgegevens ? null : this.contactgegevens;
  getBetaalgegevens = () => !this.betaalgegevens ? null : this.betaalgegevens;
  getLabels = () => !this.labels ? null : this.labels;
  getContactmomenten = () => !this.contactmomenten ? null : this.contactmomenten;
  getDossiers = () => !this.dossiers || this.dossiers.length <= 0 ? null : this.dossiers;
  getIdentiteitsdocumenten = () => !this.identiteitsdocumenten ? null : this.identiteitsdocumenten;
  getBrpZegel = () => !this.brpZegel ? null : this.brpZegel;
}
