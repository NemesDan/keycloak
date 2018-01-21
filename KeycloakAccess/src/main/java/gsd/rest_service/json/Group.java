package gsd.rest_service.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Group {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("subGroups")
    private Group subGroups[];

    public Group() {
	super();
    }

    public Group(String groupName) {
	this.name = groupName;
    }

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

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("Group [id=");
	builder.append(id);
	builder.append(", name=");
	builder.append(name);
	builder.append(", subGroups=");
	builder.append(getChildGroups().toString());
	builder.append("]");
	return builder.toString();
    }

    private StringBuilder getChildGroups() {
	StringBuilder childGroups = new StringBuilder("");
	if (subGroups != null && subGroups.length > 0) {
	    for (Group group : subGroups) {
		childGroups.append(group.toString());
	    }
	}
	return childGroups;
    }

}
