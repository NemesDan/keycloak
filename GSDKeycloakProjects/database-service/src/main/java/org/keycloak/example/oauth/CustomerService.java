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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.KeycloakPrincipal;

@Path("customers")
@Stateless
public class CustomerService {

	@Context
	private HttpServletRequest httpRequest;

	@GET
	@Produces("application/json")
	@NoCache
	@RolesAllowed({ "admin" })
	public List<String> getCustomers(@Context HttpServletRequest request) throws ServletException {
		Principal principal = request.getUserPrincipal();
		String clientName = null;
		if (principal instanceof KeycloakPrincipal) {
			KeycloakPrincipal<?> keycloakPrincipal = (KeycloakPrincipal<?>) principal;
			System.out.println(keycloakPrincipal.getKeycloakSecurityContext().getToken().getIssuedFor());
			clientName = keycloakPrincipal.getKeycloakSecurityContext().getToken().getIssuedFor();
		}
		
		request.logout();
		request.getSession().invalidate();
		
		httpRequest.logout();
		httpRequest.getSession().invalidate();
		
		ArrayList<String> rtn = new ArrayList<String>();
		rtn.add("Bill Burke");
		rtn.add("Stian Thorgersen");
		rtn.add("ADMIN access");
		rtn.add("Used client = " + clientName);
		return rtn;
	}

}
