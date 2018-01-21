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
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.keycloak.KeycloakPrincipal;

@Path("products")
@Stateless
public class ProductService {

	@GET
	@Produces("application/json")
	@NoCache
	@RolesAllowed({ "user" })
	public List<String> getProducts(@Context HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();

		String clientName = null;
		if (principal instanceof KeycloakPrincipal) {
			KeycloakPrincipal<?> keycloakPrincipal = (KeycloakPrincipal<?>) principal;
			clientName = keycloakPrincipal.getKeycloakSecurityContext().getToken().getIssuedFor();
		}

		ArrayList<String> rtn = new ArrayList<String>();
		rtn.add("iphone");
		rtn.add("ipad");
		rtn.add("ipod");
		rtn.add("USER access");
		rtn.add("Used client = " + clientName);
		return rtn;
	}
}