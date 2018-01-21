package gsd.rest_service.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Endpoints {

    @JsonProperty("issuer")
    private String issuerEndpoint;

    @JsonProperty("authorization_endpoint")
    private String authorizationEndpoint;

    @JsonProperty("token_endpoint")
    private String tokenEndpoint;

    @JsonProperty("token_introspection_endpoint")
    private String tokenIntrospectionEndpoint;

    @JsonProperty("userinfo_endpoint")
    private String userInfoEnpoint;

    @JsonProperty("end_session_endpoint")
    private String endSessionEndpoint;

    public String getIssuerEndpoint() {
	return issuerEndpoint;
    }

    public void setIssuerEndpoint(String issuerEndpoint) {
	this.issuerEndpoint = issuerEndpoint;
    }

    public String getAuthorizationEndpoint() {
	return authorizationEndpoint;
    }

    public void setAuthorizationEndpoint(String authorizationEndpoint) {
	this.authorizationEndpoint = authorizationEndpoint;
    }

    public String getTokenEndpoint() {
	return tokenEndpoint;
    }

    public void setTokenEndpoint(String tokenEndpoint) {
	this.tokenEndpoint = tokenEndpoint;
    }

    public String getTokenIntrospectionEndpoint() {
	return tokenIntrospectionEndpoint;
    }

    public void setTokenIntrospectionEndpoint(String tokenIntrospectionEndpoint) {
	this.tokenIntrospectionEndpoint = tokenIntrospectionEndpoint;
    }

    public String getUserInfoEnpoint() {
	return userInfoEnpoint;
    }

    public void setUserInfoEnpoint(String userInfoEnpoint) {
	this.userInfoEnpoint = userInfoEnpoint;
    }

    public String getEndSessionEndpoint() {
	return endSessionEndpoint;
    }

    public void setEndSessionEndpoint(String endSessionEndpoint) {
	this.endSessionEndpoint = endSessionEndpoint;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("Endpoints [issuerEndpoint=");
	builder.append(issuerEndpoint);
	builder.append(", \n");
	builder.append("authorizationEndpoint=");
	builder.append(authorizationEndpoint);
	builder.append(", \n");
	builder.append("tokenEndpoint=");
	builder.append(tokenEndpoint);
	builder.append(", \n");
	builder.append("tokenIntrospectionEndpoint=");
	builder.append(tokenIntrospectionEndpoint);
	builder.append(", \n");
	builder.append("userInfoEnpoint=");
	builder.append(userInfoEnpoint);
	builder.append(", \n");
	builder.append("endSessionEndpoint=");
	builder.append(endSessionEndpoint);
	builder.append("]");
	return builder.toString();
    }


}
