package gsd.rest_service.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserCredentials {

    @JsonProperty("type")
    private String type;

    @JsonProperty("value")
    private String value;

    public UserCredentials(String type, String value) {
	this.type = type;
	this.value = value;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

}
