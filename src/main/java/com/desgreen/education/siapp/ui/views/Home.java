package com.desgreen.education.siapp.ui.views;

import com.desgreen.education.siapp.ui.MainLayout;
import com.desgreen.education.siapp.ui.components.FlexBoxLayout;
import com.desgreen.education.siapp.ui.layout.size.Horizontal;
import com.desgreen.education.siapp.ui.layout.size.Right;
import com.desgreen.education.siapp.ui.layout.size.Uniform;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Welcome")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "Home")
public class Home extends ViewFrame {

	public Home() {
		setId("home");
//		setViewContent(createContent());

	}

	private Component createContent() {
		Html intro = new Html("<p>A responsive application template with some dummy data. Loosely based on " +
				"the <b>responsive layout grid</b> guidelines set by " +
				"<a href=\"https://material.io/design/layout/responsive-layout-grid.html\">Material Design</a>. " +
				"Utilises the <a href=\"https://vaadin.com/themes/lumo\">Lumo</a> theme.</p>");

		Html productivity = new Html("<p>The starter gives you a productivity boost and a head start. " +
				"You get an app shell with a typical hierarchical left-hand menu. The shell, the views and the " +
				"components are all responsive and touch friendly, which makes them great for desktop and mobile" +
				"use. The views are built with Java, which enhances Java developers' productivity by allowing them to" +
				"do all in one language.</p>");


		Anchor documentation = new Anchor("https://vaadin.com/docs/business-app/overview.html", UIUtils.createButton("Read the documentation", VaadinIcon.EXTERNAL_LINK));
		Anchor starter = new Anchor("https://vaadin.com/start/latest/business-app", UIUtils.createButton("Start a new project with Business App", VaadinIcon.EXTERNAL_LINK));

		FlexBoxLayout links = new FlexBoxLayout(documentation, starter);
		links.setFlexWrap(FlexWrap.WRAP);
		links.setSpacing(Right.S);

		FlexBoxLayout content = new FlexBoxLayout(intro, productivity, links);
		content.setFlexDirection(FlexDirection.COLUMN);
		content.setMargin(Horizontal.AUTO);
		content.setMaxWidth("840px");
		content.setPadding(Uniform.RESPONSIVE_L);

		return content;
	}

}
