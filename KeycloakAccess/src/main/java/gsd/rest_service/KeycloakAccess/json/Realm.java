package gsd.rest_service.KeycloakAccess.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Realm {

    @JsonProperty("id")
    private String id;

    @JsonProperty("realm")
    private String realm;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getRealm() {
	return realm;
    }

    public void setRealm(String realm) {
	this.realm = realm;
    }

    @Override
    public String toString() {
	return "Realm [id=" + id + ", realm=" + realm + "]";
    }

}
