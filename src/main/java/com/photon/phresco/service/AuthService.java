/*
 * ###
 * Service Auth
 * 
 * Copyright (C) 1999 - 2012 Photon Infotech Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ###
 */
package com.photon.phresco.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.photon.phresco.commons.model.User;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.ldap.api.LDAPManager;
import com.photon.phresco.ldap.api.LDAPServerFactory;
import com.photon.phresco.ldap.util.LDAPConstants;
import com.photon.phresco.util.Credentials;

@Path(LDAPConstants.LDAP_SERVICE_PATH)
public class AuthService {
	
    private LDAPManager ldapManager = null;
    
    public AuthService() throws PhrescoException {
        LDAPServerFactory.initialize();
        ldapManager = LDAPServerFactory.getLDAPManager();
    }
    
	@POST
	@Path(LDAPConstants.LDAP_SERVICE_AUTHENTICATE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public User authenticate(Credentials credentials) throws PhrescoException {
		return ldapManager.authenticate(credentials);
    }
	
	@POST
	@Path (LDAPConstants.LDAP_SERVICE_IMPORT)
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public List<User> getUsers(User user) throws PhrescoException {
		return ldapManager.getAllUsers(user);
        
    }
}
