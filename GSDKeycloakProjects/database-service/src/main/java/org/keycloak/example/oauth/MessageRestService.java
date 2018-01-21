package org.keycloak.example.oauth;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/message")
@Stateless
public class MessageRestService {

	@GET
	public Response printMessage() {
		return Response.status(200).entity("Hello World!!!!!!!").build();
	}

	@GET
	@Path("/{param}")
	public Response printMessage(@PathParam("param") String msg) {
		String result = "Restful example : " + msg;
		return Response.status(200).entity(result).build();
	}

}
