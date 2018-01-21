package gsd.rest_service.KeycloakAccess.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Client {

    @JsonProperty("id")
    private String id;

    @JsonProperty("clientId")
    private String clientId;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getClientId() {
	return clientId;
    }

    public void setClientId(String clientId) {
	this.clientId = clientId;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("Client [id=");
	builder.append(id);
	builder.append(", clientId=");
	builder.append(clientId);
	builder.append("]");
	return builder.toString();
    }

}
