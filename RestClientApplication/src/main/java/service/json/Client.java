package service.json;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Client {

	public Client() {
		super();
	}

	public Client(String id) {
		this.id = id;
	}

	@JsonProperty("id")
	private String id;

	@JsonProperty("clientId")
	private String clientId;

	@JsonProperty("notBefore")
	private BigDecimal notBefore;

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

	public BigDecimal getNotBefore() {
		return notBefore;
	}

	public void setNotBefore(BigDecimal notBefore) {
		this.notBefore = notBefore;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Client [id=");
		builder.append(id);
		builder.append(", clientId=");
		builder.append(clientId);
		builder.append(", notBefore=");
		builder.append(notBefore);
		builder.append("]");
		return builder.toString();
	}

}
