package gsd.rest_service.KeycloakAccess.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Role {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("scopeParamRequired")
    private Boolean scopeParamRequired;

    @JsonProperty("composite")
    private Boolean composite;

    @JsonProperty("clientRole")
    private Boolean clientRole;

    @JsonProperty("containerId")
    private String containerId;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Boolean isScopeParamRequired() {
	return scopeParamRequired;
    }

    public void setScopeParamRequired(Boolean scopeParamRequired) {
	this.scopeParamRequired = scopeParamRequired;
    }

    public Boolean isComposite() {
	return composite;
    }

    public void setComposite(Boolean composite) {
	this.composite = composite;
    }

    public Boolean isClientRole() {
	return clientRole;
    }

    public void setClientRole(Boolean clientRole) {
	this.clientRole = clientRole;
    }

    public String getContainerId() {
	return containerId;
    }

    public void setContainerId(String containerId) {
	this.containerId = containerId;
    }

    @Override
    public String toString() {
	return "Role [id=" + id + ", name=" + name + ", description=" + description + ", containerId=" + containerId + "]";
    }

}
