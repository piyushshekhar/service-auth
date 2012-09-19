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

package com.photon.phresco.ldap.api;

import com.photon.phresco.exception.PhrescoException;

public class LDAPServerFactory {
    
    private static final String LDAP_MANAGER_IMPL_CLASS = "com.photon.phresco.ldap.impl.LDAPManagerImpl";
    private static LDAPManager ldapManager = null;
    
    public static synchronized void initialize() throws PhrescoException {
        ldapManager = (LDAPManager) constructClass(LDAP_MANAGER_IMPL_CLASS);
    }
    
    private static Object constructClass(String className) throws PhrescoException {
        try {
            @SuppressWarnings("rawtypes")
            Class clazz = Class.forName(className);
            return clazz.newInstance();
        } catch (Exception e) {
            throw new PhrescoException(e);
        }
    }
    
    public static LDAPManager getLDAPManager(){
        return ldapManager;
    }
}
