package gsd.rest_service.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserAccess {

    @JsonProperty("manageGroupMembership")
    private boolean manageGroupMembership;

    @JsonProperty("view")
    private boolean view;

    @JsonProperty("mapRoles")
    private boolean mapRoles;

    @JsonProperty("impersonate")
    private boolean impersonate;

    @JsonProperty("manage")
    private boolean manage;

    public boolean isManageGroupMembership() {
	return manageGroupMembership;
    }

    public void setManageGroupMembership(boolean manageGroupMembership) {
	this.manageGroupMembership = manageGroupMembership;
    }

    public boolean isView() {
	return view;
    }

    public void setView(boolean view) {
	this.view = view;
    }

    public boolean isMapRoles() {
	return mapRoles;
    }

    public void setMapRoles(boolean mapRoles) {
	this.mapRoles = mapRoles;
    }

    public boolean isImpersonate() {
	return impersonate;
    }

    public void setImpersonate(boolean impersonate) {
	this.impersonate = impersonate;
    }

    public boolean isManage() {
	return manage;
    }

    public void setManage(boolean manage) {
	this.manage = manage;
    }

    @Override
    public String toString() {
	return "UserAccess [manageGroupMembership=" + manageGroupMembership + ", view=" + view + ", mapRoles=" + mapRoles + ", impersonate="
		+ impersonate + ", manage=" + manage + "]";
    }

}
