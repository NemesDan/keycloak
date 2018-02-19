package service.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class PushRevocation {

	public PushRevocation() {
		super();
	}

	public PushRevocation(String client) {
		this.client = client;
	}

	@JsonProperty("client")
	private String client;

	@JsonProperty("realm")
	private String realm;

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getRealm() {
		return realm;
	}

	public void setRealm(String realm) {
		this.realm = realm;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PushRevocation [client=");
		builder.append(client);
		builder.append(", realm=");
		builder.append(realm);
		builder.append("]");
		return builder.toString();
	}

}
