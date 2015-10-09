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

import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Servlet;

import org.everit.authentication.http.form.FormAuthConfig;
import org.everit.authentication.http.form.FormAuthenticationServlet;
import org.everit.authentication.http.session.AuthenticationSessionAttributeNames;
import org.everit.authenticator.Authenticator;
import org.everit.osgi.authentication.http.form.ecm.FormAuthenticationServletConstants;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ECMExtenderConstants;
import org.everit.resource.resolver.ResourceIdResolver;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

import aQute.bnd.annotation.headers.ProvideCapability;

/**
 * ECM component for FormAuthenticationServlet.
 */
@Component(
    componentId = FormAuthenticationServletConstants.SERVICE_FACTORYPID_FORM_AUTHENTICATION_SERVLET,
    configurationPolicy = ConfigurationPolicy.FACTORY,
    label = "Everit HTTP Form Authentication Servlet Component",
    description = "The component that implements HTTP form-based authentication mechanism as a "
        + "Servlet.")
@ProvideCapability(ns = ECMExtenderConstants.CAPABILITY_NS_COMPONENT,
    value = ECMExtenderConstants.CAPABILITY_ATTR_CLASS + "=${@class}")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = FormAuthenticationServletConstants.DEFAULT_SERVICE_DESCRIPTION,
        priority = FormAuthServletAttrPriority.P01_SERVICE_DESCRIPTION,
        label = "Service Description",
        description = "The description of this component configuration. It is used to easily "
            + "identify the service registered by this component.") })
@ManualService(Servlet.class)
public class FormAuthenticationServletComponent {

  private AuthenticationSessionAttributeNames authenticationSessionAttributeNames;

  private Authenticator authenticator;

  private String failedUrl;

  private String formParamNameFailedUrl;

  private String formParamNamePassword;

  private String formParamNameSuccessUrl;

  private String formParamNameUsername;

  private ResourceIdResolver resourceIdResolver;

  private ServiceRegistration<Servlet> serviceRegistration;

  private String successUrl;

  /**
   * Component activator method.
   */
  @Activate
  public void activate(final ComponentContext<FormAuthenticationServletComponent> componentContext)
      throws Exception {
    FormAuthConfig formAuthConfig = new FormAuthConfig(formParamNameUsername, formParamNamePassword,
        successUrl, formParamNameSuccessUrl, failedUrl, formParamNameFailedUrl);

    FormAuthenticationServlet formAuthenticationServlet =
        new FormAuthenticationServlet(authenticationSessionAttributeNames, authenticator,
            resourceIdResolver, formAuthConfig);

    Dictionary<String, Object> serviceProperties =
        new Hashtable<>(componentContext.getProperties());
    serviceRegistration =
        componentContext.registerService(Servlet.class, formAuthenticationServlet,
            serviceProperties);
  }

  /**
   * Component deactivate method.
   */
  @Deactivate
  public void deactivate() {
    if (serviceRegistration != null) {
      serviceRegistration.unregister();
    }
  }

  @ServiceRef(
      attributeId = FormAuthenticationServletConstants.ATTR_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES,
      defaultValue = "",
      attributePriority = FormAuthServletAttrPriority.P09_AUTHENTICATION_SESSION_ATTRIBUTE_NAMES,
      label = "AuthenticationSessionAttributeNames",
      description = "OSGi Service filter expression for AuthenticationSessionAttributeNames "
          + "instance")
  public void setAuthenticationSessionAttributeNames(
      final AuthenticationSessionAttributeNames authenticationSessionAttributeNames) {
    this.authenticationSessionAttributeNames = authenticationSessionAttributeNames;
  }

  @ServiceRef(attributeId = FormAuthenticationServletConstants.ATTR_AUTHENTICATOR,
      defaultValue = "",
      attributePriority = FormAuthServletAttrPriority.P08_AUTHENTICATOR,
      label = "Authenticator",
      description = "OSGi Service filter expression for Authenticator instance")
  public void setAuthenticator(final Authenticator authenticator) {
    this.authenticator = authenticator;
  }

  @StringAttribute(attributeId = FormAuthenticationServletConstants.ATTR_FAILED_URL,
      defaultValue = FormAuthenticationServletConstants.DEFAULT_FAILED_URL,
      priority = FormAuthServletAttrPriority.P04_FAILED_URL,
      label = "Failed URL",
      description = "The URL where the user will be redirected by default in case of a failed "
          + "authentication.")
  public void setFailedUrl(final String failedUrl) {
    this.failedUrl = failedUrl;
  }

  @StringAttribute(attributeId = FormAuthenticationServletConstants.ATTR_FORM_PARAM_NAME_FAILED_URL,
      defaultValue = FormAuthenticationServletConstants.DEFAULT_FORM_PARAM_NAME_FAILED_URL,
      priority = FormAuthServletAttrPriority.P05_FORM_PARAM_NAME_FAILED_URL,
      label = "Failed URL parameter name",
      description = "The name of the parameter that stores the URL where the user will be "
          + "redirected in case of a failed authentication. If the request contains this "
          + "parameter, it overrides the \"Failed URL\" configuration.")

  public void setFormParamNameFailedUrl(final String formParamNameFailedUrl) {
    this.formParamNameFailedUrl = formParamNameFailedUrl;
  }

  @StringAttribute(attributeId = FormAuthenticationServletConstants.ATTR_FORM_PARAM_NAME_PASSWORD,
      defaultValue = FormAuthenticationServletConstants.DEFAULT_FORM_PARAM_NAME_PASSWORD,
      priority = FormAuthServletAttrPriority.P07_FORM_PARAM_NAME_PASSWORD,
      label = "Password parameter name",
      description = "The name of the form parameter that stores the password.")
  public void setFormParamNamePassword(final String formParamNamePassword) {
    this.formParamNamePassword = formParamNamePassword;
  }

  @StringAttribute(
      attributeId = FormAuthenticationServletConstants.ATTR_FORM_PARAM_NAME_SUCCESS_URL,
      defaultValue = FormAuthenticationServletConstants.DEFAULT_FORM_PARAM_NAME_SUCCESS_URL,
      priority = FormAuthServletAttrPriority.P03_FORM_PARAM_NAME_SUCCESS_URL,
      label = "Success URL parameter name",
      description = "The name of the parameter that stores the URL where the user will be "
          + "redirected in case of a successful authentication. If the request contains this "
          + "parameter, it overrides the \"Success URL\" configuration.")
  public void setFormParamNameSuccessUrl(final String formParamNameSuccessUrl) {
    this.formParamNameSuccessUrl = formParamNameSuccessUrl;
  }

  @StringAttribute(attributeId = FormAuthenticationServletConstants.ATTR_FORM_PARAM_NAME_USERNAME,
      defaultValue = FormAuthenticationServletConstants.DEFAULT_FORM_PARAM_NAME_USERNAME,
      priority = FormAuthServletAttrPriority.P06_FORM_PARAM_NAME_USERNAME,
      label = "Username parameter name",
      description = "The name of the form parameter that stores the username.")
  public void setFormParamNameUsername(final String formParamNameUsername) {
    this.formParamNameUsername = formParamNameUsername;
  }

  @ServiceRef(attributeId = FormAuthenticationServletConstants.ATTR_RESOURCE_ID_RESOLVER,
      defaultValue = "",
      attributePriority = FormAuthServletAttrPriority.P10_RESOURCE_ID_RESOLVER,
      label = "ResourceIdResolver OSGi filter",
      description = "OSGi Service filter expression for ResourceIdResolver instance")
  public void setResourceIdResolver(final ResourceIdResolver resourceIdResolver) {
    this.resourceIdResolver = resourceIdResolver;
  }

  @StringAttribute(attributeId = FormAuthenticationServletConstants.ATTR_SUCCESS_URL,
      defaultValue = FormAuthenticationServletConstants.DEFAULT_SUCCESS_URL,
      priority = FormAuthServletAttrPriority.P02_SUCCESS_URL,
      label = "Success URL",
      description = "The URL where the user will be redirected by default in case of a successful "
          + "authentication.")
  public void setSuccessUrl(final String successUrl) {
    this.successUrl = successUrl;
  }

}
