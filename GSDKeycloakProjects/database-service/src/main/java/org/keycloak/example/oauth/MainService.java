/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.example.oauth;

import java.net.URI;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.http.client.utils.URIBuilder;
import org.jboss.resteasy.spi.HttpRequest;

@Path("main")
@Stateless
public class MainService {

    @Context
    private HttpRequest httpRequest;

    @GET
    @Path("/logout")
    public Response doLogout(@QueryParam("redirect_uri") String redirect_uri) throws Exception {
	System.out.println("trying to logout the user");
	String uriString = "http://localhost:8180/auth/realms/demo/protocol/openid-connect/logout";
	
	
	URI uri = new URIBuilder(uriString)
		.addParameter("redirect_uri", redirect_uri)
		.addParameter("client_id", "rest_service")
		.build();

	return Response.temporaryRedirect(uri).build();
	
	/*
	HttpClient client = HttpClientBuilder.create().build();
	HttpGet request = new HttpGet(uriString);
	
	URI uri = new URIBuilder(request.getURI())
		.addParameter("redirect_uri","http://localhost:8080/TestRestProject/")
		.build();
	((HttpRequestBase) request).setURI(uri);
	
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
	
	return Response.status(200).entity(result.toString()).build();
	*/
    }
}
