package gsd.rest_service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.ObjectMapper;

import gsd.rest_service.json.Client;
import gsd.rest_service.json.Endpoints;
import gsd.rest_service.json.Group;
import gsd.rest_service.json.Realm;
import gsd.rest_service.json.Role;
import gsd.rest_service.json.Token;
import gsd.rest_service.json.User;

public class Test {

	private static final String CUSTOMER_PORTAL_SERVICE_SECRET = "8039581c-33a6-43ef-8ad3-4ec82f2c80e7";

	private static Token accessToken = null;
	private static Token adminAccessToken = null;

	private static String realmName = null;

	private static Role[] roleArray = null;
	private static Group[] groupArray = null;

	private static Endpoints endpoints = null;

	public static void main(String[] args) throws Exception {
		String userName = null;
		String password = null;

		final Scanner scanner = new Scanner(System.in);

		boolean done = false;
		while (!done) {
			System.out.println("waiting command: ");
			String command = scanner.nextLine();
			CLICommandsEnum enumDef = CLICommandsEnum.getEnum(command);

			String groupId = null;
			String userId = null;
			String roleId = null;
			String localRealmName = null;

			switch (enumDef) {
			case HELP:
				CLICommandsEnum.printAll();
				break;

			case ENDPOINTS:
				getEndpoints();
				break;

			case EXIT:
				done = true;
				break;

			case SET_REALM:
				realmName = readRealm(scanner);
				break;

			case CREDENTIALS:
				System.out.println("user: ");
				userName = scanner.nextLine();
				System.out.println("password: ");
				password = scanner.nextLine();
				break;

			case LOGIN:
				loginToCustomerPortal(userName, password);
				break;

			case PRODUCTS:
			case CUSTOMERS:
				accessDatabaseRestService(command);
				break;

			case NEW_USER:
				System.out.println("username: ");
				String username = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				createNewUser(localRealmName, username);
				break;

			case NEW_ROLE:
				System.out.println("role name: ");
				String roleName = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				createNewRole(localRealmName, roleName);
				break;

			case NEW_GROUP:
				System.out.println("group name: ");
				String groupName = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				createNewGroup(localRealmName, groupName);
				break;

			case GET_USERS:
				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}
				getAllUsers(localRealmName);
				break;

			case GET_CLIENTS:
				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}
				getAllClients(localRealmName);
				break;

			case GET_GROUPS:
				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}
				getAllGroups(localRealmName);
				break;

			case NEW_CLIENT:
				System.out.println("client name: ");
				String clientName = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				createNewClient(localRealmName, clientName);
				break;

			case GET_REALMS:
				getAllRealms();
				break;

			case GET_ALL_ROLES:
				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				getAllRealmRoles(localRealmName);
				break;

			case GET_ASSIGNED_USER_GROUPS:
				System.out.println("userId: ");
				userId = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				getAssignedUserGroups(localRealmName, userId);
				break;

			case GET_ASSIGNED_USER_ROLES:
				System.out.println("userId: ");
				userId = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				getAssignedUserRoles(localRealmName, userId);
				break;

			case GET_AVAILABLE_USER_ROLES:
				System.out.println("userId: ");
				userId = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				getAvailableUserRoles(localRealmName, userId);
				break;

			case GET_EFFECTIVE_USER_ROLES:
				System.out.println("userId: ");
				userId = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				getEffectiveUserRoles(localRealmName, userId);
				break;

			case GET_ASSIGNED_GROUP_ROLES:
				System.out.println("groupId: ");
				groupId = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				getAssignedGroupRoles(localRealmName, groupId);
				break;

			case GET_AVAILABLE_GROUP_ROLES:
				System.out.println("groupId: ");
				groupId = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				getAvailableGroupRoles(localRealmName, groupId);
				break;

			case GET_EFFECTIVE_GROUP_ROLES:
				System.out.println("groupId: ");
				groupId = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				getEffectiveGroupRoles(localRealmName, groupId);
				break;

			case ASSIGN_ROLE_TO_USER:
				System.out.println("userId: ");
				userId = scanner.nextLine();

				System.out.println("roleId: ");
				roleId = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				assignRoleToUser(localRealmName, userId, roleId);
				break;

			case ASSIGN_USER_TO_GROUP:
				System.out.println("userId: ");
				userId = scanner.nextLine();

				System.out.println("groupId: ");
				groupId = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				assignUserToGroup(localRealmName, userId, groupId);
				break;

			case ASSIGN_ROLE_TO_GROUP:
				System.out.println("groupId: ");
				groupId = scanner.nextLine();

				System.out.println("roleId: ");
				roleId = scanner.nextLine();

				if (realmName == null) {
					localRealmName = readRealm(scanner);
				} else {
					localRealmName = realmName;
				}

				assignRoleToGroup(localRealmName, groupId, roleId);
				break;

			case AUTH:
				keycloakAuthenticate();
				break;
			}
		}

		scanner.close();
	}

	private static String readRealm(Scanner scanner) {
		System.out.println("realm name: ");
		return scanner.nextLine();
	}

	private static void accessDatabaseRestService(String command) throws Exception {
		System.out.println("trying to access database rest service");
		String uri = "http://localhost:8080/database/" + command;

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet post = new HttpGet(uri);
		if (accessToken != null) {
			post.addHeader("Authorization", "Bearer " + accessToken.getAccessToken());
		}
		HttpResponse response = client.execute(post);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}
		System.out.println(result);
		System.out.println();
	}

	private static void loginToCustomerPortal(String userName, String password) throws Exception {
		System.out.println("trying to login to customer-portal");
		String uri = "http://localhost:8180/auth/realms/demo/protocol/openid-connect/token";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(uri);
		List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
		urlParameters.add(new BasicNameValuePair("grant_type", "password"));
		urlParameters.add(new BasicNameValuePair("client_id", "customer-portal"));
		urlParameters.add(new BasicNameValuePair("username", userName));
		urlParameters.add(new BasicNameValuePair("password", password));
		urlParameters.add(new BasicNameValuePair("client_secret", CUSTOMER_PORTAL_SERVICE_SECRET));
		post.setEntity(new UrlEncodedFormEntity(urlParameters));
		HttpResponse response = client.execute(post);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}
		System.out.println(result);
		System.out.println();

		try {
			ObjectMapper mapper = new ObjectMapper();
			accessToken = mapper.readValue(result.toString(), Token.class);
		} catch (Exception ex) {
		}
	}

	private static void getEndpoints() throws Exception {
		System.out.println("trying to login to customer-portal");
		String uri = "http://localhost:8180/auth/realms/demo/.well-known/openid-configuration";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			endpoints = mapper.readValue(result.toString(), Endpoints.class);
		} catch (Exception ex) {
		}
		System.out.println(endpoints);
	}

	private static void createNewUser(String realmName, String username) throws Exception {
		System.out.println("trying to create a new user");
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/users";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		String userRepresentation = null;
		User user = new User();
		user.setUsername(username == null || username.trim().isEmpty() ? UUID.randomUUID().toString() : username);
		user.setEnabled(true);
		user.setRealmRoles(new String[] { "user" });
		try {
			ObjectMapper mapper = new ObjectMapper();
			userRepresentation = mapper.writeValueAsString(user);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		HttpEntity entity = new ByteArrayEntity(userRepresentation.getBytes("UTF-8"));
		request.setEntity(entity);

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}
		System.out.println(result);
		System.out.println();
	}

	private static void createNewClient(String realmName, String clientName) throws Exception {
		System.out.println("trying to create a new user");
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/clients";

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		String clientRepresentation = null;
		Client client = new Client();
		client.setClientId(clientName);
		try {
			ObjectMapper mapper = new ObjectMapper();
			clientRepresentation = mapper.writeValueAsString(client);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		HttpEntity entity = new ByteArrayEntity(clientRepresentation.getBytes("UTF-8"));
		request.setEntity(entity);

		HttpResponse response = httpClient.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}
		System.out.println(result);
		System.out.println();
	}

	private static void createNewRole(String realmName, String roleName) throws Exception {
		System.out.println("trying to create a new role");
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/roles";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		String roleRepresentation = null;
		Role role = new Role();
		role.setName(roleName);
		try {
			ObjectMapper mapper = new ObjectMapper();
			roleRepresentation = mapper.writeValueAsString(role);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		HttpEntity entity = new ByteArrayEntity(roleRepresentation.getBytes("UTF-8"));
		request.setEntity(entity);

		HttpResponse response = client.execute(request);

		int responseCode = response.getStatusLine().getStatusCode();
		System.out.println("Response Code : " + responseCode);

		// response code 204 means NO_CONTENT
		if (responseCode != 204) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line1 = "";
			while ((line1 = rd.readLine()) != null) {
				result.append(line1);
			}
			System.out.println(result);
			System.out.println();
		}
	}

	private static void createNewGroup(String realmName, String groupName) throws Exception {
		System.out.println("trying to create a new group");
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/groups";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		String groupRepresentation = null;
		Group group = new Group(groupName);
		try {
			ObjectMapper mapper = new ObjectMapper();
			groupRepresentation = mapper.writeValueAsString(group);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		HttpEntity entity = new ByteArrayEntity(groupRepresentation.getBytes("UTF-8"));
		request.setEntity(entity);

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}
		System.out.println(result);
		System.out.println();
	}

	private static void assignRoleToUser(String realmName, String userId, String roleId) throws Exception {
		System.out.println(MessageFormat.format("trying to assign roleId {0} to userID {1}", roleId, userId));
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/users/" + userId + "/role-mappings/realm/";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		if (roleArray == null) {
			getAllRealmRoles(realmName);
		}

		String roleRepresentation = null;
		Role[] jsonRoleArray = new Role[1];
		for (Role role : roleArray) {
			if (role.getId().equals(roleId)) {
				jsonRoleArray[0] = role;
				break;
			}
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			roleRepresentation = mapper.writeValueAsString(jsonRoleArray);
		} catch (Exception ex) {
		}

		HttpEntity entity = new ByteArrayEntity(roleRepresentation.getBytes("UTF-8"));
		request.setEntity(entity);

		HttpResponse response = client.execute(request);

		int responseCode = response.getStatusLine().getStatusCode();
		System.out.println("Response Code : " + responseCode);

		// response code 204 means NO_CONTENT
		if (responseCode != 204) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line1 = "";
			while ((line1 = rd.readLine()) != null) {
				result.append(line1);
			}
			System.out.println(result);
			System.out.println();
		}
	}

	private static void assignUserToGroup(String realmName, String userId, String groupId) throws Exception {
		System.out.println(MessageFormat.format("trying to assign userID {0} to groupID {1}", userId, groupId));
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/users/" + userId + "/groups/" + groupId;

		HttpClient client = HttpClientBuilder.create().build();
		HttpPut request = new HttpPut(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		if (groupArray == null) {
			getAllGroups(realmName);
		}

		String roleRepresentation = null;
		Group[] jsonGroupArray = new Group[1];
		for (Group group : groupArray) {
			if (group.getId().equals(groupId)) {
				jsonGroupArray[0] = group;
				break;
			}
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			roleRepresentation = mapper.writeValueAsString(jsonGroupArray);
		} catch (Exception ex) {
		}

		HttpEntity entity = new ByteArrayEntity(roleRepresentation.getBytes("UTF-8"));
		request.setEntity(entity);

		HttpResponse response = client.execute(request);

		int responseCode = response.getStatusLine().getStatusCode();
		System.out.println("Response Code : " + responseCode);

		// response code 204 means NO_CONTENT
		if (responseCode != 204) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line1 = "";
			while ((line1 = rd.readLine()) != null) {
				result.append(line1);
			}
			System.out.println(result);
			System.out.println();
		}
	}

	private static void assignRoleToGroup(String realmName, String groupId, String roleId) throws Exception {
		System.out.println(MessageFormat.format("trying to assign roleId {0} to groupID {1}", roleId, groupId));
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/groups/" + groupId + "/role-mappings/realm/";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		if (roleArray == null) {
			getAllRealmRoles(realmName);
		}

		String roleRepresentation = null;
		Role[] jsonRoleArray = new Role[1];
		for (Role role : roleArray) {
			if (role.getId().equals(roleId)) {
				jsonRoleArray[0] = role;
				break;
			}
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			roleRepresentation = mapper.writeValueAsString(jsonRoleArray);
		} catch (Exception ex) {
		}

		HttpEntity entity = new ByteArrayEntity(roleRepresentation.getBytes("UTF-8"));
		request.setEntity(entity);

		HttpResponse response = client.execute(request);

		int responseCode = response.getStatusLine().getStatusCode();
		System.out.println("Response Code : " + responseCode);

		// response code 204 means NO_CONTENT
		if (responseCode != 204) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line1 = "";
			while ((line1 = rd.readLine()) != null) {
				result.append(line1);
			}
			System.out.println(result);
			System.out.println();
		}
	}

	private static void getAssignedUserRoles(String realmName, String userId) throws Exception {
		System.out.println(MessageFormat.format("trying to get roles for userID {0} and realm name {1}", userId, realmName));
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/users/" + userId + "/role-mappings/realm/";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}

		Role[] roleArray = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			roleArray = mapper.readValue(result.toString(), Role[].class);
		} catch (Exception ex) {
		}

		if (roleArray != null) {
			for (Role role : roleArray) {
				System.out.println(role);
			}
		}
	}

	private static void getAssignedUserGroups(String realmName, String userId) throws Exception {
		System.out.println(MessageFormat.format("trying to get groups to which userID {0} is assigned to", userId));
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/users/" + userId + "/groups";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}

		Group[] groupArray = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			groupArray = mapper.readValue(result.toString(), Group[].class);
		} catch (Exception ex) {
		}

		if (groupArray != null) {
			for (Group group : groupArray) {
				System.out.println(group);
			}
		}
	}

	private static void getAvailableUserRoles(String realmName, String userId) throws Exception {
		System.out.println(MessageFormat.format("trying to get roles for userID {0} and realm name {1}", userId, realmName));
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/users/" + userId + "/role-mappings/realm/available";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}

		// print all the roles
		printRoles(result.toString());
	}

	private static void getEffectiveUserRoles(String realmName, String userId) throws Exception {
		System.out.println(MessageFormat.format("trying to get roles for userID {0} and realm name {1}", userId, realmName));
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/users/" + userId + "/role-mappings/realm/composite";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}

		// print all the roles
		printRoles(result.toString());
	}

	private static void getAssignedGroupRoles(String realmName, String groupId) throws Exception {
		System.out.println(MessageFormat.format("trying to get roles for groupID {0} and realm name {1}", groupId, realmName));
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/groups/" + groupId + "/role-mappings/realm/";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}

		// print all the roles
		printRoles(result.toString());
	}

	private static void getAvailableGroupRoles(String realmName, String groupId) throws Exception {
		System.out.println(MessageFormat.format("trying to get roles for groupID {0} and realm name {1}", groupId, realmName));
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/groups/" + groupId + "/role-mappings/realm/available";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}

		// print all the roles
		printRoles(result.toString());
	}

	private static void getEffectiveGroupRoles(String realmName, String groupId) throws Exception {
		System.out.println(MessageFormat.format("trying to get roles for groupID {0} and realm name {1}", groupId, realmName));
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/groups/" + groupId + "/role-mappings/realm/composite";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}
		// print all the roles
		printRoles(result.toString());
	}

	private static void getAllUsers(String realmName) throws Exception {
		System.out.println("trying to retrieve the list of all users");
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/users";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}

		User[] userArray = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			userArray = mapper.readValue(result.toString(), User[].class);
		} catch (Exception ex) {
		}

		if (userArray != null) {
			for (User user : userArray) {
				System.out.println(user);
			}
		}
	}

	private static void getAllClients(String realmName) throws Exception {
		System.out.println("trying to retrieve the list of all users");
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/clients";

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}

		HttpResponse response = httpClient.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}

		Client[] clientArray = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			clientArray = mapper.readValue(result.toString(), Client[].class);
		} catch (Exception ex) {
		}

		if (clientArray != null) {
			for (Client client : clientArray) {
				System.out.println(client);
			}
		}
	}

	private static void getAllRealms() throws Exception {
		System.out.println("trying to retrieve the list of realms");
		String uri = "http://localhost:8180/auth/admin/realms";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}

		Realm[] realmArray = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			realmArray = mapper.readValue(result.toString(), Realm[].class);
		} catch (Exception ex) {
		}

		if (realmArray != null) {
			for (Realm realm : realmArray) {
				System.out.println(realm);
			}
		}

	}

	private static void getAllRealmRoles(String realmName) throws Exception {
		System.out.println("trying to retrieve then entire list of defined realm roles");
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/roles";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			roleArray = mapper.readValue(result.toString(), Role[].class);
		} catch (Exception ex) {
		}

		if (roleArray != null) {
			for (Role role : roleArray) {
				System.out.println(role);
			}
		}
	}

	private static void getAllGroups(String realmName) throws Exception {
		System.out.println("trying to retrieve the list of realms");
		String uri = "http://localhost:8180/auth/admin/realms/" + realmName + "/groups";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);
		if (adminAccessToken != null) {
			request.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}

		HttpResponse response = client.execute(request);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			groupArray = mapper.readValue(result.toString(), Group[].class);
		} catch (Exception ex) {
		}

		if (groupArray != null) {
			for (Group group : groupArray) {
				System.out.println(group);
			}
		}

	}

	private static void keycloakAuthenticate() throws Exception {
		System.out.println("trying to authenticate into keycloak");
		String uri = "http://localhost:8180/auth/realms/master/protocol/openid-connect/token";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(uri);

		List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
		urlParameters.add(new BasicNameValuePair("username", "admin"));
		urlParameters.add(new BasicNameValuePair("password", "admin"));
		urlParameters.add(new BasicNameValuePair("grant_type", "password"));
		urlParameters.add(new BasicNameValuePair("client_id", "admin-cli"));
		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = client.execute(post);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line1 = "";
		while ((line1 = rd.readLine()) != null) {
			result.append(line1);
		}
		System.out.println(result);
		System.out.println();

		try {
			ObjectMapper mapper = new ObjectMapper();
			adminAccessToken = mapper.readValue(result.toString(), Token.class);
		} catch (Exception ex) {
		}
	}

	private static void printRoles(String rolesJson) {
		Role[] roleArray = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			roleArray = mapper.readValue(rolesJson, Role[].class);
		} catch (Exception ex) {
		}

		if (roleArray != null) {
			for (Role role : roleArray) {
				System.out.println(role);
			}
		}
	}

}
