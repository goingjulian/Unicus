import {JsonObject, JsonProperty} from "json2typescript";

@JsonObject("Label")
export class Label {

  @JsonProperty("label", String, true) private label: string = undefined;

  getLabel = (): string => !this.label ? '-' : this.label;

}
