package com.desgreen.education.siapp.security_config;

import com.desgreen.education.siapp.security_utils_ui.AccessDeniedException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.getSource().addUIInitListener(uiEvent -> {
			final UI ui = uiEvent.getUI();
			ui.addBeforeEnterListener(this::beforeEnter);
		});
	}

	/**
	 * Reroutes the user if they're not authorized to access the view.
	 *
	 */
	// private void beforeEnter(BeforeEnterEvent event) {
	// 	if (!LoginView.class.equals(event.getNavigationTarget())
	// 	    && !SecurityUtils.isUserLoggedIn()) {
	// 		event.rerouteTo(LoginView.class);
	// 	}
	// }

	
	/**
	 * Rahasianya ada disini
	 * Setap Route yang akan dimasuki, di cek dahulu apakah user tersebut mempunya otorisasi terhada route tersebut
	 * Reroutes the user if she is not authorized to access the view. 
	 */
	private void beforeEnter(BeforeEnterEvent event) {
		boolean accessGranted = SecurityUtils.isAccessGranted(event.getNavigationTarget()); //definisikan disini

		 if (!accessGranted) {
		 	if (SecurityUtils.isUserLoggedIn()) {
		 		event.rerouteToError(AccessDeniedException.class);
		 	} else {
		 		event.rerouteTo(LoginView.class);
		 	}
		 }


	}	
}