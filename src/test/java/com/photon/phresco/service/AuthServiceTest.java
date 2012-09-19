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

import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import com.photon.phresco.commons.model.User;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.ldap.api.LDAPManager;
import com.photon.phresco.ldap.api.LDAPServerFactory;
import com.photon.phresco.util.Credentials;

public class AuthServiceTest {
    
    private LDAPManager ldapManager = null;
    
    @Before
    public void init() throws PhrescoException {
        LDAPServerFactory.initialize();
        ldapManager = LDAPServerFactory.getLDAPManager();
    }
    
    @Test
    public void testAuthenticate() throws PhrescoException {
        String userName = "demouser";
        String password = "phresco";
        byte[] encodeBase64 = Base64.encodeBase64(password.getBytes());
        String encodedPassword = new String(encodeBase64);
        Credentials credentials = new Credentials(userName, encodedPassword);
        User user = ldapManager.authenticate(credentials);
        assertNotNull(user);
    }

    @Test
    public void testGetUsers() throws PhrescoException {
        User user = new User();
        user.setName("demouser");
        List<User> users = ldapManager.getAllUsers(user);
        assertNotNull(users);
    }

}
