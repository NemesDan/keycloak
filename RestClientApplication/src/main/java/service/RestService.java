package service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.databind.ObjectMapper;

import service.json.Token;

@Path("/service")
public class RestService {

	private static Token accessToken = null;
	private static final String REST_SERVICE_SECRET = "cb5e1be8-486d-480e-9d11-b202d7bf586b";
	// private static final String CUSTOMER_PORTAL_SECRET = "8039581c-33a6-43ef-8ad3-4ec82f2c80e7";

	@GET
	@Path("/login")
	public Response doLoginWorking() throws Exception {
		System.out.println("trying to call the authorization endpoint");
		String uriString = "http://localhost:8180/auth/realms/demo/protocol/openid-connect/auth";

		HttpGet request = new HttpGet(uriString);

		String stateParameter = UUID.randomUUID().toString();

		URI uri = new URIBuilder(uriString).addParameter("response_type", "code")
				// .addParameter("client_id", "customer-portal")
				.addParameter("client_id", "rest_service")
				.addParameter("redirect_uri", "http://localhost:8080/TestRestProject/rest/service/user_logged_in")
				.addParameter("state", stateParameter)
				.addParameter("scope", "openid").build();

		((HttpRequestBase) request).setURI(uri);

		return Response.temporaryRedirect(uri).build();
	}
	
	@GET
	@Path("/iread_redirect")
	public String ireadRedirect(@QueryParam("code") String code, @QueryParam("state") String state,
			@QueryParam("client_id") String clientId) throws Exception {

		final List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
		urlParameters.add(new BasicNameValuePair("grant_type", "authorization_code"));
		urlParameters.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/TestRestProject/rest/service/iread_redirect"));
		urlParameters.add(new BasicNameValuePair("code", code.trim()));
		urlParameters.add(new BasicNameValuePair("client_id", "rest_service"));
		urlParameters.add(new BasicNameValuePair("client_secret", REST_SERVICE_SECRET));
		urlParameters.add(new BasicNameValuePair("username", null));
		urlParameters.add(new BasicNameValuePair("password", null));
		
		final HttpPost request = new HttpPost("http://localhost:8080/user-management-server/auth/token");
		request.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED);
		request.setEntity(new UrlEncodedFormEntity(urlParameters));
		
		final HttpClient client = HttpClientBuilder.create().build();
		final HttpResponse response = client.execute(request);

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
		
		return result.toString();
	}
	
	@GET
	@Path("/iread_refresh_token")
	public String ireadRefreshToken(@QueryParam("client_id") String clientId) throws Exception {
		
		final List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
		urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
		urlParameters.add(new BasicNameValuePair("client_id", "rest_service"));
		urlParameters.add(new BasicNameValuePair("refresh_token", "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJOMWtmNWhnZHdSWnhHa1pVeFFXRV96X1R5ay1GMGFGQVNTYWhzYzRaUmJzIn0.eyJqdGkiOiIxODdhODcyYi05MDM2LTRmOWUtYTY3MC01YjJkOWFmNjczNzUiLCJleHAiOjE1MjQ2MDc4NTMsIm5iZiI6MCwiaWF0IjoxNTI0NDk5ODUzLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgxODAvYXV0aC9yZWFsbXMvaXJlYWQiLCJhdWQiOiJyZXN0X3NlcnZpY2UiLCJzdWIiOiIxMzIwY2ZhZS01YTZkLTQwOWQtODczNS1mMmY5OWFmNWY5YjYiLCJ0eXAiOiJSZWZyZXNoIiwiYXpwIjoicmVzdF9zZXJ2aWNlIiwiYXV0aF90aW1lIjowLCJzZXNzaW9uX3N0YXRlIjoiNzM1OTdmZTctZjA2Ni00NzQyLWJmNjQtZDg1YmFjY2U2OTM2IiwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImFkbWluIiwidGVzdCByb2xlcyIsInVtYV9hdXRob3JpemF0aW9uIiwidXNlciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX19.gXlEIDgIYfM6_JIIJHRu9jVEMZd_MKblH6v2nw0P5W-7AvgULNDg6p5Ur59bTy-o-5Z-q7Ecae8Dx2O2wVeQqaRYYQBVHo2sBro6XQoyV9nXN1ZX91-IQ-AqaHLUB_NCTkU-7-iaymOb1Ev_p9Y7NYOj3JkNrPjsqV8n2AIBj4h_jTTMd52Fur8PiUFgEpmKAxZyvN3eKBAYhlZy5Liby2uZ3ZgDWlXuBXH4Fjb5c6oF5tlRRJ5xusQOsGup9OaNQxGVTITERCuU2F8RaGNtnCmqK5oUJSUwl8fJGm9YPkjwUFn13SwLCckGd7-knQjts_NWcskP5A20K3UlUwm6sg"));
		urlParameters.add(new BasicNameValuePair("client_secret", REST_SERVICE_SECRET));
		
		final HttpPost request = new HttpPost("http://localhost:8080/user-management-server/auth/token");
		request.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED);
		request.setEntity(new UrlEncodedFormEntity(urlParameters));
		
		final HttpClient client = HttpClientBuilder.create().build();
		final HttpResponse response = client.execute(request);
		
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
		
		return result.toString();
	}

	@GET
	@Path("/user_logged_in")
	public Response printMessage(@QueryParam("code") String code, @QueryParam("state") String state,
			@QueryParam("client_id") String clientId, @Context HttpHeaders headers) throws Exception {

		System.out.println("----------------------");
		System.out.println("----------------------");
		System.out.println();
		System.out.println();
		System.out.println();

		StringBuilder parameters = new StringBuilder("code = ").append(code);
		parameters.append("\nstate = ").append(state);
		parameters.append("\nclient_id = ").append(clientId);

		System.out.println(parameters);

		System.out.println();
		for (String header : headers.getRequestHeaders().keySet()) {
			System.out.println("Key : " + header + " ,Value : " + headers.getRequestHeader(header));
		}

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

		// urlParameters.add(new BasicNameValuePair("client_id",
		// "customer-portal"));
		// urlParameters.add(new BasicNameValuePair("client_secret",
		// CUSTOMER_PORTAL_SECRET));
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
	public Response printMessage(@QueryParam("code") String code, @QueryParam("state") String state,
			@QueryParam("client_id") String clientId, @PathParam("param") String param) throws Exception {

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

	// TODO the logout isn't working correctly because the REST services can
	// still be accessed with the token
	@GET
	@Path("/logout")
	public Response doLogout() throws Exception {
		System.out.println("trying to logout the user");
		String uriString = "http://localhost:8180/auth/realms/demo/protocol/openid-connect/logout";

		URI uri = new URIBuilder(uriString)
				.addParameter("redirect_uri", "http://localhost:8080/TestRestProject/")
				.addParameter("client_id", "rest_service")
				.build();

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
		urlParameters.add(new BasicNameValuePair("refresh_token", accessToken.getRefreshToken()));
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
		return Response.status(204).build();
	}

}
