package com.desgreen.education.siapp.security_config;

import com.desgreen.education.siapp.security_utils_ui.AccessDeniedView;
import com.desgreen.education.siapp.security_utils_ui.CustomRouteNotFoundError;
import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * SecurityUtils takes care of all such static operations that have to do with
 * security and querying rights from different beans of the UI.
 *
 */
public final class SecurityUtils {

	private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);

	private SecurityUtils() {
	}

	/**
	 * Gets the user name of the currently signed in user.
	 *
	 * @return the user name of the current user or <code>null</code> if the user
	 *         has not signed in
	 */
	public static String getUsername() {
		SecurityContext context = SecurityContextHolder.getContext();
		Object principal = context.getAuthentication().getPrincipal();
		if(principal instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
			return userDetails.getUsername();
		}
		// Anonymous or no authentication.
		return null;
	}


//	public static FUser getFUser() {
//		SecurityContext context = SecurityContextHolder.getContext();
//		Object principal = context.getAuthentication().getPrincipal();
//		if(principal instanceof UserDetails) {
//			UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
//			return userDetails.getUsername();
//		}
//		// Anonymous or no authentication.
//		return null;
//	}


	/**
	 * Checks if access is granted for the current user for the given secured view,
	 * defined by the view class.
	 *
	 * @param securedClass View class
	 * @return true if access is granted, false otherwise.
	 */
	public static boolean isAccessGranted(Class<?> securedClass) {
		final boolean publicView = LoginViewManual.class.equals(securedClass)
			|| AccessDeniedView.class.equals(securedClass)
			|| CustomRouteNotFoundError.class.equals(securedClass)
//			|| PpdbOnlineView.class.equals(securedClass)
//			|| PpdbListView.class.equals(securedClass)
//			|| PpdbSelectMatkulView.class.equals(securedClass)
//			|| KrsView.class.equals(securedClass)
//			|| Home.class.equals(securedClass)
			;


		// Always allow access to public views
		if (publicView) {
			return true;
		}

		Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

		// All other views require authentication
		/**
		 * Jika tidak ada @Secure maka dianggap public
		 */
		if (!isUserLoggedIn(userAuthentication)) {
			return false;
		}

		// Allow if no roles are required.
		Secured secured = AnnotationUtils.findAnnotation(securedClass, Secured.class);
		if (secured == null) {
			return true;
		}

		List<String> allowedRoles = Arrays.asList(secured.value());
//		for (String allowedRole: allowedRoles) {
//			System.out.println(allowedRole + " is In List");
//		}
		return userAuthentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.anyMatch(allowedRoles::contains);
	}

	/**
	 * Checks if the user is logged in.
	 *
	 * @return true if the user is logged in. False otherwise.
	 */
	public static boolean isUserLoggedIn() {
		return isUserLoggedIn(SecurityContextHolder.getContext().getAuthentication());
	}

	private static boolean isUserLoggedIn(Authentication authentication) {
		return authentication != null
			&& !(authentication instanceof AnonymousAuthenticationToken);
	}

	/**
	 * Tests if the request is an internal framework request. The test consists of
	 * checking if the request parameter is present and if its value is consistent
	 * with any of the request types know.
	 *
	 * @param request
	 *            {@link HttpServletRequest}
	 * @return true if is an internal framework request. False otherwise.
	 */
	static boolean isFrameworkInternalRequest(HttpServletRequest request) {
		final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
		return parameterValue != null
				&& Stream.of(HandlerHelper.RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
	}

}
