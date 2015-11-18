/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
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
package org.everit.authentication.http.form.ecm.tests;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.eclipse.jetty.server.Server;
import org.everit.authentication.context.AuthenticationContext;
import org.everit.authentication.simple.SimpleSubject;
import org.everit.authentication.simple.SimpleSubjectManager;
import org.everit.osgi.dev.testrunner.TestDuringDevelopment;
import org.everit.osgi.dev.testrunner.TestRunnerConstants;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.resource.ResourceService;
import org.junit.Assert;
import org.junit.Test;
import org.osgi.framework.BundleContext;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * Test for FormAuthenticationServletComponent.
 */
@Component(configurationPolicy = ConfigurationPolicy.OPTIONAL)
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TESTRUNNER_ENGINE_TYPE,
        defaultValue = "junit4"),
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TEST_ID,
        defaultValue = "FormAuthenticationServletTest") })
@Service(value = FormAuthenticationServletTestComponent.class)
public class FormAuthenticationServletTestComponent {

  private static final String HELLO_SERVLET_ALIAS = "/hello";

  private static final String LOGIN_ACTION = "/login-action";

  private static final String LOGIN_FAILED_ALIAS = "/login-failed.html";

  private static final String LOGIN_SUCCESS_ALIAS = "/login-success.html";

  private static final String PASSWORD = "open sesame";

  private static final String USERNAME = "Aladdin";

  private static final String WRONG_PASSWORD = PASSWORD + PASSWORD;

  private long authenticatedResourceId;

  private AuthenticationContext authenticationContext;

  private long defaultResourceId;

  private String helloUrl;

  private String loginActionUrl;

  private String loginFailedUrl;

  private String loginSuccessUrl;

  private ResourceService resourceService;

  private Server server;

  private SimpleSubjectManager simpleSubjectManager;

  /**
   * Test component activate method.
   */
  @Activate
  public void activate(final BundleContext context, final Map<String, Object> componentProperties)
      throws Exception {
    String testServerURI = server.getURI().toString();
    String testServerURL = testServerURI.substring(0, testServerURI.length() - 1);

    helloUrl = testServerURL + HELLO_SERVLET_ALIAS;
    loginActionUrl = testServerURL + LOGIN_ACTION;
    loginFailedUrl = testServerURL + LOGIN_FAILED_ALIAS;
    loginSuccessUrl = testServerURL + LOGIN_SUCCESS_ALIAS;

    long resourceId = resourceService.createResource();
    simpleSubjectManager.delete(USERNAME);
    SimpleSubject simpleSubject = simpleSubjectManager.create(resourceId, USERNAME, PASSWORD);
    authenticatedResourceId = simpleSubject.resourceId;
    defaultResourceId = authenticationContext.getDefaultResourceId();
  }

  private void hello(final HttpContext httpContext, final long expectedResourceId)
      throws IOException {
    HttpClient httpClient = new DefaultHttpClient();
    HttpGet httpGet = new HttpGet(helloUrl);
    HttpResponse httpResponse = httpClient.execute(httpGet, httpContext);
    Assert.assertEquals(HttpServletResponse.SC_OK, httpResponse.getStatusLine().getStatusCode());
    HttpEntity responseEntity = httpResponse.getEntity();
    InputStream inputStream = responseEntity.getContent();
    StringWriter writer = new StringWriter();
    IOUtils.copy(inputStream, writer);
    String responseBodyAsString = writer.toString();
    Assert.assertEquals(expectedResourceId, Long.parseLong(responseBodyAsString));
  }

  private void login(final HttpContext httpContext, final String username, final String password,
      final String expectedLocation)
          throws Exception {
    HttpClient httpClient = new DefaultHttpClient();
    HttpPost httpPost = new HttpPost(loginActionUrl);
    List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    parameters.add(new BasicNameValuePair("username", username));
    parameters.add(new BasicNameValuePair("password", password));
    parameters.add(new BasicNameValuePair("successUrl", LOGIN_SUCCESS_ALIAS));
    parameters.add(new BasicNameValuePair("failedUrl", LOGIN_FAILED_ALIAS));
    HttpEntity entity = new UrlEncodedFormEntity(parameters);
    httpPost.setEntity(entity);
    HttpResponse httpResponse = httpClient.execute(httpPost, httpContext);
    Assert.assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY,
        httpResponse.getStatusLine().getStatusCode());
    Header locationHeader = httpResponse.getFirstHeader("Location");
    Assert.assertEquals(expectedLocation, locationHeader.getValue());
  }

  @ServiceRef(defaultValue = "")
  public void setAuthenticationContext(final AuthenticationContext authenticationContext) {
    this.authenticationContext = authenticationContext;
  }

  @ServiceRef(defaultValue = "")
  public void setResourceService(final ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  @ServiceRef(defaultValue = "")
  public void setServer(final Server server) {
    this.server = server;
  }

  @ServiceRef(defaultValue = "")
  public void setSimpleSubjectManager(final SimpleSubjectManager simpleSubjectManager) {
    this.simpleSubjectManager = simpleSubjectManager;
  }

  @Test
  @TestDuringDevelopment
  public void testAccessHelloPage() throws Exception {
    CookieStore cookieStore = new BasicCookieStore();
    HttpContext httpContext = new BasicHttpContext();
    httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

    hello(httpContext, defaultResourceId);
    login(httpContext, USERNAME, WRONG_PASSWORD, loginFailedUrl);
    login(httpContext, USERNAME, PASSWORD, loginSuccessUrl);
    hello(httpContext, authenticatedResourceId);
  }

}
