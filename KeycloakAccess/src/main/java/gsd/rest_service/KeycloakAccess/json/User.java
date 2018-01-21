package gsd.rest_service.KeycloakAccess.json;

import java.util.Arrays;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class User {

    @JsonProperty("id")
    private String id;

    @JsonProperty("createdTimestamp")
    private Date createdTimestamp;

    @JsonProperty("username")
    private String username;

    @JsonProperty("enabled")
    private boolean enabled;

    @JsonProperty("totp")
    private boolean totp;

    @JsonProperty("emailVerified")
    private boolean emailVerified;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("disableableCredentialTypes")
    private String[] disableableCredentialTypes;

    @JsonProperty("requiredActions")
    private String[] requiredActions;

    @JsonProperty("notBefore")
    private int notBefore;

    @JsonProperty("credentials")
    private UserCredentials credentials;

    @JsonProperty("realmRoles")
    private String[] realmRoles;

    @JsonProperty("access")
    private UserAccess access;

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public boolean isEnabled() {
	return enabled;
    }

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    public UserCredentials getCredentials() {
	return credentials;
    }

    public void setCredentials(UserCredentials credentials) {
	this.credentials = credentials;
    }

    public String[] getRealmRoles() {
	return realmRoles;
    }

    public void setRealmRoles(String[] realmRoles) {
	this.realmRoles = realmRoles;
    }

    @Override
    public String toString() {
	return "User [id=" + id + ", username=" + username + ", realmRoles=" + Arrays.toString(realmRoles) + "]";
    }


}
