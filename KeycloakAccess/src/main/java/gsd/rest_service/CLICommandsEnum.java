package gsd.rest_service;

public enum CLICommandsEnum {
    
    HELP("help", "available CLI commands"),
    EXIT("exit", "close application"),
    
    ENDPOINTS("endpoints", "retrieves the available endpoints"),
    
    SET_REALM("set realm", "set the realm for all the following requests"),
    
    CREDENTIALS("credentials", "set the user's credentials"),
    LOGIN("login", "login into application as a user"),
    PRODUCTS("products", "returns the products list"),
    CUSTOMERS("customers", "return the customers list"),
    
    NEW_USER("new user", "create a new user"),
    NEW_ROLE("new role", "create a new role"),
    NEW_CLIENT("new client", "Create a new client, client_id must be unique!"),

    //
    // http://localhost:8180/auth/admin/realms/{REALM_NAME}/groups
    NEW_GROUP("new group", "create a new top level group"),
    
    //
    // http://localhost:8180/auth/admin/realms/{REALM_NAME}/roles
    GET_ALL_ROLES("get roles", "get all roles defined for a specific realm"),
    GET_USERS("get users", "get all registered users"),
    GET_REALMS("get realms", "get all realms"),
    GET_CLIENTS("get clients", "Get clients belonging to the realm, Returns a list of clients belonging to the realm"),

    //
    // http://localhost:8180/auth/admin/realms/{REALM_NAME}/groups
    GET_GROUPS("get groups", "Get groups belonging to the realm, Returns a list of groups belonging to the realm"),
    
    // --------
    //
    // http://localhost:8180/auth/admin/realms/{REALM_NAME}/users/{USER_ID}/role-mappings/realm/
    ASSIGN_ROLE_TO_USER("assign user role", "assign a role to a user"),

    //
    // http://localhost:8180/auth/admin/realms/{REALM_NAME}/users/{USER_ID}/groups/{GROUP_ID}
    ASSIGN_USER_TO_GROUP("assign user group", "assign a user to a group"),
    
    //
    // http://localhost:8180/auth/admin/realms/{REALM_NAME}/groups/{GROUP_ID}/role-mappings/realm/
    ASSIGN_ROLE_TO_GROUP("assign group role", "assign a role to a group"),
    // --------
    
    // --------
    //
    // http://localhost:8180/auth/admin/realms/{REALM_NAME}/users/{USER_ID}/groups
    GET_ASSIGNED_USER_GROUPS("get assigned user groups", "get all the groups in which a user is assigned to"),
    // --------
    
    
    // --------
    //
    // http://localhost:8180/auth/admin/realms/{REALM_NAME}/users/{USER_ID}/role-mappings/realm
    GET_ASSIGNED_USER_ROLES("get assigned user roles", "get all assigned realm-level roles for a specific user"),

    //
    // http://localhost:8180/auth/admin/realms/{REALM_NAME}/users/{USER_ID}/role-mappings/realm/available
    GET_AVAILABLE_USER_ROLES("get available user roles", "get all available realm-level roles for a specific user that can be mapped"),

    //
    // http://localhost:8180/auth/admin/realms/{REALM_NAME}/users/{USER_ID}/role-mappings/realm/composite
    GET_EFFECTIVE_USER_ROLES("get effective user roles", "get all effective realm-level roles for a specific user, This will recurse all composite roles to get the result. (includes roles that come from groups)"),
    // --------
    
    // --------
    //
    // http://localhost:8180/auth/admin/realms/{REALM_NAME}/groups/{GROUP_ID}/role-mappings/realm
    GET_ASSIGNED_GROUP_ROLES("get assigned group roles", "get all assigned realm-level roles for a specific group"),

    //
    // http://localhost:8180/auth/admin/realms/{REALM_NAME}/groups/{GROUP_ID}/role-mappings/realm/available
    GET_AVAILABLE_GROUP_ROLES("get available group roles", "get all available realm-level roles for a specific group that can be mapped"),
    
    //
    // http://localhost:8180/auth/admin/realms/{REALM_NAME}/users/{GROUP_ID}/role-mappings/realm/composite
    GET_EFFECTIVE_GROUP_ROLES("get effective group roles", "get all effective realm-level roles for a specific group, This will recurse all composite roles to get the result. (includes roles that come composite roles)"),
    // --------
    
    AUTH("auth", "authenticate in keycloak");
    
	private String command;
	private String description;

	private CLICommandsEnum(String command, String description) {
		this.command = command;
		this.description = description;
	}

	public String getCommand() {
		return command;
	}

	public String getDescription() {
		return description;
	}

	public static CLICommandsEnum getEnum(String value) {
		CLICommandsEnum output = HELP;
		for (CLICommandsEnum def : values()) {
			if (def.getCommand().equalsIgnoreCase(value)) {
				output = def;
				break;
			}
		}
		return output;
	}

	public static void printAll() {
		for (CLICommandsEnum def : values()) {
			System.out.println(def.getCommand() + " -> " + def.getDescription());
		}
	}
    
}
