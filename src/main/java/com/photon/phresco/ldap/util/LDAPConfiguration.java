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
package com.photon.phresco.ldap.util;

import java.io.InputStream;
import java.util.List;

import com.photon.phresco.configuration.ConfigReader;
import com.photon.phresco.configuration.Configuration;
import com.photon.phresco.exception.PhrescoException;

public class LDAPConfiguration implements LDAPConstants {
	
    private static final String LDAP = "LDAP";
    private static LDAPConfiguration ldapConfig;
    
	private String ldapUrl;
	private String ldaphost;
	private String ldapPort;
	private String ldapProtocol;
	private String ldapBaseDn;
    private List<Configuration> configurations;
	
    private LDAPConfiguration() throws PhrescoException {
        InputStream is = null;
        try {
            is = SpringLDAPConfig.class.getClassLoader().getResourceAsStream(
                    LDAPConstants.CONFIGFILE);
            ConfigReader configReader = new ConfigReader(is);
            String environment = System.getProperty(LDAPConstants.ENVIRONMENT);
            if (environment == null || environment.isEmpty()) {
                environment = configReader.getDefaultEnvName();
            }
            configurations = configReader.getConfigurations(environment, LDAP);
        } catch (Exception e) {
            throw new PhrescoException(e);
        }
        configurationList(configurations);
    }
    
    public static LDAPConfiguration getInstance() throws PhrescoException {
        if (ldapConfig == null) {
            ldapConfig = new LDAPConfiguration();
        }
        return ldapConfig;
    }

    private void configurationList(List<Configuration> configurations)
            throws PhrescoException {
        try {
            if (configurations != null) {
                for (Configuration configuration : configurations) {
                    ldapProtocol = configuration.getProperties().getProperty(LDAP_PROTOCOL);
                    ldaphost = configuration.getProperties().getProperty(LDAP_HOST);
                    ldapPort = configuration.getProperties().getProperty(LDAP_PORT);
                    ldapBaseDn = configuration.getProperties().getProperty(LDAP_BASEDN);
                }
            }

        } catch (Exception e) {
            throw new PhrescoException(e);
        }
    }


	/**
	 * @return the ldapUrl
	 */
	public String getLdapUrl()throws PhrescoException {
		ldapUrl = ldapProtocol + LDAPConstants.COLON + DOUBLE_SLASH + ldaphost + LDAPConstants.COLON + ldapPort;
		return ldapUrl;
	}

	/**
	 * @return the ldapBaseDn
	 */
	public String getLdapBaseDn()throws PhrescoException {
		return ldapBaseDn;
	}	
	
}
