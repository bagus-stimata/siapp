package com.desgreen.education.siapp.security_config;

import com.desgreen.education.siapp.AppPublicService;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.*;

/**
 * Rubah pada 
 * # ConfigureUIServiceInitListener.java dan
 * # SecurityUtils.java
 */
// @Route

@Route("login")
@PageTitle("Login | " + AppPublicService.APP_NAME)
@JsModule("./styles/shared-styles.js")
public class LoginView extends LoginOverlay
	implements AfterNavigationObserver, BeforeEnterObserver {

	public LoginView() {
		LoginI18n i18n = LoginI18n.createDefault();
		i18n.setHeader(new LoginI18n.Header());
		i18n.getHeader().setTitle(AppPublicService.APP_NAME);
		i18n.getHeader().setDescription(AppPublicService.APP_DESC1 );

		i18n.setAdditionalInformation(AppPublicService.APP_DESC2+ "\n" + AppPublicService.APP_DESC3);

		i18n.setForm(new LoginI18n.Form());
		i18n.getForm().setSubmit("MASUK");
		i18n.getForm().setTitle("Masuk");
		i18n.getForm().setUsername("User Name");
		i18n.getForm().setPassword("Password");
		setI18n(i18n);
		setForgotPasswordButtonVisible(true);
		setAction("login");
	}
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (SecurityUtils.isUserLoggedIn()) {
//			event.forwardTo(StorefrontView.class);
//			event.forwardTo(StorefrontView.class);
		} else {
			setOpened(true);
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		setError(
			event.getLocation().getQueryParameters().getParameters().containsKey(
				"error"));
	}

}
