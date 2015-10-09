/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.biz)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.osgi.authentication.http.form.ecm.internal;

/**
 * Component attribute priorities.
 */
public final class FormAuthServletAttrPriority {

  public static final int P01_SERVICE_DESCRIPTION = 1;

  public static final int P02_SUCCESS_URL = 2;

  public static final int P03_FORM_PARAM_NAME_SUCCESS_URL = 3;

  public static final int P04_FAILED_URL = 4;

  public static final int P05_FORM_PARAM_NAME_FAILED_URL = 5;

  public static final int P06_FORM_PARAM_NAME_USERNAME = 6;

  public static final int P07_FORM_PARAM_NAME_PASSWORD = 7;

  public static final int P08_AUTHENTICATOR = 8;

  public static final int P09_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES = 9;

  public static final int P10_RESOURCE_ID_RESOLVER = 10;

  private FormAuthServletAttrPriority() {
  }

}
