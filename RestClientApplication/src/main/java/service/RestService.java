package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.json.Realm;
import service.json.Token;

@Path("/service")
public class RestService {

	private static Token accessToken = null;
	private static Token adminAccessToken = null;
	private static final String REST_SERVICE_SECRET = "cb5e1be8-486d-480e-9d11-b202d7bf586b";
	
	@GET
	@Path("/login")
	public Response doLoginWorking() throws Exception {
		System.out.println("trying to call the authorization endpoint");
		String uriString = "http://localhost:8180/auth/realms/demo/protocol/openid-connect/auth";

		HttpGet request = new HttpGet(uriString);

		String stateParameter = UUID.randomUUID().toString();

		URI uri = new URIBuilder(uriString).addParameter("response_type", "code")
				.addParameter("client_id", "rest_service")
				.addParameter("redirect_uri", "http://localhost:8080/TestRestProject/rest/service/user_logged_in")
				.addParameter("state", stateParameter).addParameter("scope", "openid").build();

		((HttpRequestBase) request).setURI(uri);

		return Response.temporaryRedirect(uri).build();
	}

	@GET
	@Path("/user_logged_in")
	public Response printMessage(@QueryParam("code") String code, @QueryParam("state") String state) throws Exception {

		System.out.println("----------------------");
		System.out.println("----------------------");
		System.out.println();
		System.out.println();
		System.out.println();

		StringBuilder parameters = new StringBuilder("code = ").append(code);
		parameters.append("\nstate = ").append(state);

		System.out.println(parameters);

		System.out.println();
		System.out.println();
		System.out.println("----------------------");
		System.out.println("----------------------");

		if (code != null) {
			this.exchangeCodeForToken(code);
		}

		return Response.status(200).entity("User has been logged in. Hello World!!!! <br>" + parameters).build();
	}

	private void exchangeCodeForToken(String code) throws Exception {
		System.out.println("----------------------");
		System.out.println("----------------------");
		System.out.println();
		System.out.println();
		System.out.println();

		System.out.println("trying to exchange code for token");
		String uriString = "http://localhost:8180/auth/realms/demo/protocol/openid-connect/token";

		HttpClient client = HttpClientBuilder.create().build();

		HttpPost request = new HttpPost(uriString);

		List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
		urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
		urlParameters.add(new BasicNameValuePair("code", code.trim()));
		urlParameters.add(new BasicNameValuePair("redirect_uri",
				"http://localhost:8080/TestRestProject/rest/service/user_logged_in"));

		urlParameters.add(new BasicNameValuePair("client_id", "rest_service"));
		urlParameters.add(new BasicNameValuePair("client_secret", REST_SERVICE_SECRET));

		request.setEntity(new UrlEncodedFormEntity(urlParameters));

		URI uri = new URIBuilder(uriString).build();

		((HttpRequestBase) request).setURI(uri);

		System.out.println(uri);

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

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("----------------------");
		System.out.println("----------------------");

		try {
			ObjectMapper mapper = new ObjectMapper();
			accessToken = mapper.readValue(result.toString(), Token.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@GET
	@Path("/call_database/{param}")
	public Response printMessage(@QueryParam("code") String code, 
			@QueryParam("state") String state,
			@QueryParam("client_id") String clientId,
			@PathParam("param") String param) throws Exception {

		String output = this.accessDatabaseRestService(param);

		return Response.status(200).entity(output).build();
	}

	private String accessDatabaseRestService(String command) throws Exception {
		System.out.println("trying to access database rest service");
		String uri = "http://localhost:8080/database/" + command;

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(uri);
		if (accessToken != null) {
			request.addHeader("Authorization", "Bearer " + accessToken.getAccessToken());
		}

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

		return result.toString();
	}

	// still be accessed with the token
	@GET
	@Path("/logout")
	public Response doLogout() throws Exception {
		System.out.println("trying to logout the user");
		String uriString = "http://localhost:8180/auth/realms/demo/protocol/openid-connect/logout";

		URI uri = new URIBuilder(uriString)
				.addParameter("redirect_uri", "http://localhost:8080/TestRestProject/rest/service")
				.addParameter("client_id", "rest_service").build();

		return Response.temporaryRedirect(uri).build();
	}

	@GET
	@Path("/logout_2")
	public Response doLogout2() throws Exception {
		System.out.println("trying to logout the user");
		String uri = "http://localhost:8180/auth/realms/demo/protocol/openid-connect/logout";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost(uri);
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

		if (accessToken != null) {
			request.addHeader("Authorization", "Bearer " + accessToken.getAccessToken());
		}

		List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
		urlParameters.add(new BasicNameValuePair("client_id", "rest_service"));
		if (accessToken != null) {
			urlParameters.add(new BasicNameValuePair("refresh_token", accessToken.getRefreshToken()));
		}
		urlParameters.add(new BasicNameValuePair("client_secret", REST_SERVICE_SECRET));
		urlParameters.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/TestRestProject/"));

		request.setEntity(new UrlEncodedFormEntity(urlParameters));

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
			return Response.status(200).entity(result.toString()).build();
		}
		
		//
		//
		//
		keycloakAuthenticate();
		
		putNotBefore();
		
		pushRevocation();
		
		return Response.status(204).build();
	}
	
	private static void keycloakAuthenticate() throws Exception {
		System.out.println("trying to authenticate into keycloak to retrieve the admin access token");
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
	
	private static void putNotBefore() throws Exception {
		System.out.println("trying to put the not before value");
		String uri = "http://localhost:8180/auth/admin/realms/demo";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPut putRequest = new HttpPut(uri);
		if (adminAccessToken != null) {
			putRequest.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		putRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		
		// the not before value MUST HAVE a dot on the 3'rd last position (or somewhere there, couldn't find out WHY???????)
		long notBefore = System.currentTimeMillis();
		String notBeforeString = Long.toString(notBefore);
		notBeforeString = notBeforeString.substring(0, 10) + "." + notBeforeString.substring(10, notBeforeString.length());
		
		String realmRepresentation = null;
		Realm realm = new Realm();
		realm.setNotBefore(new BigDecimal(notBeforeString));
		realm.setRealm("demo");
		try {
			ObjectMapper mapper = new ObjectMapper();
			realmRepresentation = mapper.writeValueAsString(realm);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		HttpEntity entity = new ByteArrayEntity(realmRepresentation.getBytes("UTF-8"));
		putRequest.setEntity(entity);
		HttpResponse response = client.execute(putRequest);

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
	
	private static void pushRevocation() throws Exception {
		System.out.println("trying to put the not before value");
		String uri = "http://localhost:8180/auth/admin/realms/demo/push-revocation";

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost(uri);
		if (adminAccessToken != null) {
			postRequest.addHeader("Authorization", "Bearer " + adminAccessToken.getAccessToken());
		}
		// both content type and accept are MANDATORY!!!!!!!!!!!!!!!!!
		postRequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		postRequest.setHeader(HttpHeaders.ACCEPT, "application/json");
		
		String realmRepresentation = null;
		Realm realm = new Realm();
		realm.setRealm("demo");
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			realmRepresentation = mapper.writeValueAsString(realm);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		HttpEntity entity = new ByteArrayEntity(realmRepresentation.getBytes("UTF-8"));
		postRequest.setEntity(entity);
		HttpResponse response = client.execute(postRequest);

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

}
