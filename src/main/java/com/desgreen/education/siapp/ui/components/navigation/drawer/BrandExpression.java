package com.desgreen.education.siapp.ui.components.navigation.drawer;

import com.desgreen.education.siapp.security_model.FUser;
import com.desgreen.education.siapp.ui.util.UIUtils;
import com.desgreen.education.siapp.ui.utils.common.CommonFileFactory;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;

@CssImport("./styles/components/brand-expression.css")
public class BrandExpression extends Div {

	private String CLASS_NAME = "brand-expression";

	protected Image logo;
	protected Label title;

	protected FUser userActive = new FUser();

	public BrandExpression(String text) {
		setClassName(CLASS_NAME);

//		logo = new Image(UIUtils.IMG_PATH + "logos/18.png", "");
		logo = new Image(UIUtils.IMG_PATH + "logos/app_logo.png", "");
		logo.setAlt(text + " logo");
		logo.setClassName(CLASS_NAME + "__logo");

		title = UIUtils.createH3Label(text);
		title.addClassName(CLASS_NAME + "__title");

		add(logo, title);
	}

	public BrandExpression(String text, String logoPath) {
		setClassName(CLASS_NAME);

		try {
			logo = CommonFileFactory.generateImage(logoPath);
		}catch (Exception ex){}
//		logo = new Image( logoPath, "");

		logo.setAlt(text + " logo");
		logo.setClassName(CLASS_NAME + "__logo");

		title = UIUtils.createH3Label(text);
		title.addClassName(CLASS_NAME + "__title");

		add(logo, title);
	}




}
