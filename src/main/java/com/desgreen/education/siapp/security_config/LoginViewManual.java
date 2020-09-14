package com.desgreen.education.siapp.security_config;

import com.desgreen.education.siapp.AppPublicService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * Rubah pada 
 * # ConfigureUIServiceInitListener.java dan
 * # SecurityUtils.java
 */
@Route("login_")
@PageTitle("Login_ | " + AppPublicService.APP_NAME)
public class LoginViewManual extends VerticalLayout implements BeforeEnterObserver {

    LoginForm login = new LoginForm();

    public LoginViewManual() {
        addClassName("login-view");
        setSizeFull();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        login.setAction("login");

        add(
            new H1(AppPublicService.APP_NAME),
            login
        );
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
        .getQueryParameters()
        .getParameters()
        .containsKey("error")) {
            login.setError(true);
        }
    }
}
