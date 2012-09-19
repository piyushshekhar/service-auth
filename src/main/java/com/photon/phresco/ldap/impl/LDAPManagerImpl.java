/*
 * ###
 * Phresco Service Auth
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
/*******************************************************************************
 * Copyright (c) 2011 Photon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Photon Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.photon.in/legal/ppl-v10.html
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     Photon - initial API and implementation
 ******************************************************************************/
package com.photon.phresco.ldap.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.directory.SearchControls;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.ldap.SizeLimitExceededException;
import org.springframework.ldap.core.CollectingNameClassPairCallbackHandler;
import org.springframework.ldap.core.ContextMapperCallbackHandler;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

import com.photon.phresco.commons.model.User;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.ldap.api.LDAPManager;
import com.photon.phresco.ldap.model.UserAttributesMapper;
import com.photon.phresco.ldap.util.LDAPConstants;
import com.photon.phresco.ldap.util.SpringLDAPConfig;
import com.photon.phresco.util.Credentials;

public class LDAPManagerImpl implements LDAPManager, LDAPConstants {
	
	private static final Logger S_LOGGER = Logger.getLogger(LDAPManagerImpl.class);
	private static Boolean isDebugEnabled = S_LOGGER.isDebugEnabled();
	private static Map<String, LdapTemplate> ldapTemplateMap = new HashMap<String, LdapTemplate>();
	
	
	@Override
	public User authenticate(Credentials credentials) throws PhrescoException {
		if (isDebugEnabled) {
			S_LOGGER.debug("Entering Method LDAPManagerImpl.authenticate(Credentials credentials)");
		}
		User user = null;
		ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringLDAPConfig.class);
		LdapTemplate ldapTemplate = (LdapTemplate) ctx.getBean(LDAP_LDAPTEMPLATE);
		AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(LDAP_OBJECTCLASS, LDAP_OBJECTCLASS_VALUE));
        filter.and(new EqualsFilter(LDAP_FILTER, credentials.getUsername()));
        
        byte[] decodedBytes = Base64.decodeBase64(credentials.getPassword());
        String password = new String(decodedBytes);
        
        boolean authenticate = ldapTemplate.authenticate(DistinguishedName.EMPTY_PATH, filter.toString(), password);
        if(authenticate) {
            user = getUserById(credentials.getUsername(), ldapTemplate);
            ldapTemplateMap.put(credentials.getUsername(), ldapTemplate);
        }
        
        return user;
	}
	
	@SuppressWarnings("unchecked")
    public List<User> getAllUsers(User user) throws PhrescoException {
	    LdapTemplate ldapTemplate = ldapTemplateMap.get(user.getName());
        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter(LDAP_OBJECTCLASS, LDAP_OBJECTCLASS_VALUE));
        
        
        CollectingNameClassPairCallbackHandler handler = new ContextMapperCallbackHandler(new UserAttributesMapper());
        try {
            ldapTemplate.search(StringUtils.EMPTY, andFilter.encode(), handler);
        } catch (SizeLimitExceededException ex) {
            // safely ignore. Spring LDAP handles this exception different to SUNs implementation.
            S_LOGGER.error(ex.getMessage(), ex);
        }
        
        return handler.getList();
	}
	
	private User getUserById(String userId, LdapTemplate ldapTemplate) {
	    SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(LDAP_OBJECTCLASS, LDAP_OBJECTCLASS_VALUE)).and(new EqualsFilter(LDAP_FILTER, userId));
        List<User> users = ldapTemplate.search(StringUtils.EMPTY, filter.toString(), controls, new UserAttributesMapper());
        return users.get(0);
	}
}