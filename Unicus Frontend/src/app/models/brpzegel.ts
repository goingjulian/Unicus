import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject("BrpZegel")
export class BrpZegel {

  @JsonProperty("zegel", String, true) private zegel: string = undefined;

  getZegel = (): string => !this.zegel ? '-' : this.zegel;

}
