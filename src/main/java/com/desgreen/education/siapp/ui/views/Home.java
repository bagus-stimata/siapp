package com.desgreen.education.siapp.ui.views;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.security_config.AuthUserDetailsService;
import com.desgreen.education.siapp.security_config.SecurityUtils;
import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.ui.MainLayout;
import com.desgreen.education.siapp.ui.components.FlexBoxLayout;
import com.desgreen.education.siapp.ui.layout.size.Horizontal;
import com.desgreen.education.siapp.ui.layout.size.Right;
import com.desgreen.education.siapp.ui.layout.size.Uniform;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.desgreen.education.siapp.ui.utils.common.CommonImageFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexDirection;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@PageTitle("Welcome")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "Home")
public class Home extends ViewFrame {

	@Autowired
	public AuthUserDetailsService authUserDetailsService;
	protected FUser userActive = new FUser();

	public Home() {
		setId("home");
	}

	@PostConstruct
	private void init() {
		setViewContent(createContent());
	}

	private Component createContent() {
		String companyLogoPath = "";
		String brandTitle = "SIAPP";
		try {
			userActive = authUserDetailsService.getUserDetail(SecurityUtils.getUsername());
			companyLogoPath = AppPublicService.FILE_PATH + userActive.getFdivisionBean().getFcompanyBean().getLogoImage();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		try {
			brandTitle = userActive.getFdivisionBean().getFcompanyBean().getKode2();
		}catch (Exception ex){}

//		Label title = new Label(brandTitle);

		Image imageLogo = new Image();
		try {
			imageLogo = CommonFileFactory.generateImage(companyLogoPath);
		}catch (Exception ex){}
//		imageOuput.setMaxHeight("400px");
//		imageOuput.setMaxWidth("300px");
//		int newWidth = 300;
//		int newHeight = 400;
//		try {
//			BufferedImage buffImage = ImageIO.read(buffer.getInputStream());
//			buffImage = CommonImageFactory.autoRotateImage(buffImage,
//					CommonImageFactory.getImageRotationSuggestion(buffer.getInputStream()));
//			newHeight = CommonImageFactory.getMaxScaleHeight(buffImage, newWidth);
//		}catch (Exception ex){}
//		imageLogo.setWidth(newWidth, Unit.PIXELS);
//		imageLogo.setHeight(newHeight, Unit.PIXELS);

		FlexBoxLayout content = new FlexBoxLayout(imageLogo);
//		content.setFlexDirection(FlexDirection.COLUMN);
		content.setMargin(Horizontal.AUTO);
//		content.setMaxWidth("840px");
		content.setPadding(Uniform.RESPONSIVE_L);

		content.setAlignContent(FlexLayout.ContentAlignment.CENTER);


		return content;
	}
	private Component createContentX() {
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
