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
package org.everit.osgi.authentication.http.form.ecm;

/**
 * Constants of the Form Authentication Servlet component.
 */
public final class FormAuthenticationServletConstants {

  public static final String ATTR_AUTHENTICATION_PROPAGATOR = "authenticationPropagator.target";

  public static final String ATTR_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES =
      "authenticationSessionAttributeNames.target";

  public static final String ATTR_AUTHENTICATOR = "authenticator.target";

  public static final String ATTR_FAILED_URL = "failed.url";

  public static final String ATTR_FORM_PARAM_NAME_FAILED_URL = "form.param.name.failed.url";

  public static final String ATTR_FORM_PARAM_NAME_PASSWORD = "form.param.name.password";

  public static final String ATTR_FORM_PARAM_NAME_SUCCESS_URL = "form.param.name.success.url";

  public static final String ATTR_FORM_PARAM_NAME_USERNAME = "form.param.name.username";

  public static final String ATTR_RESOURCE_ID_RESOLVER = "resourceIdResolver.target";

  public static final String ATTR_SUCCESS_URL = "success.url";

  public static final String DEFAULT_FAILED_URL = "/failed.html";

  public static final String DEFAULT_FORM_PARAM_NAME_FAILED_URL = "failedUrl";

  public static final String DEFAULT_FORM_PARAM_NAME_PASSWORD = "password";

  public static final String DEFAULT_FORM_PARAM_NAME_SUCCESS_URL = "successUrl";

  public static final String DEFAULT_FORM_PARAM_NAME_USERNAME = "username";

  public static final String DEFAULT_SERVICE_DESCRIPTION =
      "Default Form Authentication Servlet";

  public static final String DEFAULT_SUCCESS_URL = "/logged-in.html";

  /**
   * The service factory PID of the Form Authentication Servlet component.
   */
  public static final String SERVICE_FACTORYPID_FORM_AUTHENTICATION_SERVLET =
      "org.everit.authentication.http.form.ecm.FormAuthenticationServlet";

  private FormAuthenticationServletConstants() {
  }

}
