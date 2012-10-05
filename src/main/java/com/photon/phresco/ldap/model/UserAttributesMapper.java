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

package com.photon.phresco.ldap.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.AbstractContextMapper;

import com.photon.phresco.commons.model.User;
import com.photon.phresco.exception.PhrescoException;
import com.photon.phresco.ldap.util.LDAPConstants;

public class UserAttributesMapper extends AbstractContextMapper implements LDAPConstants {

	@Override
    protected Object doMapFromContext(DirContextOperations context) {
        User user = new User();
        try {
        	user.setId(context.getStringAttribute(LDAP_UID_ATTRIBUTE));
            user.setName(context.getStringAttribute(LDAP_UID_ATTRIBUTE));
            user.setEmail(context.getStringAttribute(LDAP_MAIL_ATTRIBUTE));
            user.setDisplayName(context.getStringAttribute(LDAP_DISPLAY_NAME_ATTRIBUTE));
            String phrescoEnabled = "true";
            String stringAttribute = context.getStringAttribute(LDAP_PHRESCO_ENABLED);
            if(StringUtils.isNotEmpty(stringAttribute)) {
                phrescoEnabled = "true";
            }
            user.setPhrescoEnabled(Boolean.parseBoolean(phrescoEnabled));
            user.setValidLogin(true);
        } catch (Exception e) {
            try {
                throw new PhrescoException();
            } catch (PhrescoException ex){
            }
        }
        return user;
    }

}