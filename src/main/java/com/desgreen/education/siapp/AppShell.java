package com.desgreen.education.siapp;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.server.PWA;

/**
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 */
//@PWA(name = "SIAPP Dahlan Ikhsan", shortName = "SIAPP", iconPath = "images/logos/app_logo.png", backgroundColor = "#233348", themeColor = "#233348")
@Viewport("width=device-width, minimum-scale=1.0, initial-scale=1.0, user-scalable=yes")
@PWA(name = "siapp", shortName = "siapp")
public class AppShell implements AppShellConfigurator {

}
