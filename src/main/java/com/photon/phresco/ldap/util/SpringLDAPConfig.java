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

package com.photon.phresco.ldap.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

import com.photon.phresco.exception.PhrescoException;

@Configuration
public class SpringLDAPConfig {

    private static LDAPConfiguration ldapConfig = null;

    public SpringLDAPConfig() throws PhrescoException {
        ldapConfig = LDAPConfiguration.getInstance();
    }
    @Bean
    public  LdapContextSource ldapContextSource() throws PhrescoException {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapConfig.getLdapUrl());
        contextSource.setBase(ldapConfig.getLdapBaseDn());
        return contextSource;
    }
    @Bean
    public  LdapTemplate ldapTemplate() throws PhrescoException {
        return new LdapTemplate(ldapContextSource());
        
    }
}
