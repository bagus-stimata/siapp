package com.desgreen.education.siapp.security_utils_ui;

import com.vaadin.flow.component.dependency.JsModule;

public class AccessDeniedException extends RuntimeException {
	public AccessDeniedException() {
	}

	public AccessDeniedException(String message) {
		super(message);
	}
}
