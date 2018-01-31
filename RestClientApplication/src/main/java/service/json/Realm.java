package service.json;

import java.math.BigDecimal;

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

	@JsonProperty("notBefore")
	private BigDecimal notBefore;

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

	public BigDecimal getNotBefore() {
		return notBefore;
	}

	public void setNotBefore(BigDecimal notBefore) {
		this.notBefore = notBefore;
	}

	@Override
	public String toString() {
		return "Realm [id=" + id + ", realm=" + realm + "]";
	}

}
