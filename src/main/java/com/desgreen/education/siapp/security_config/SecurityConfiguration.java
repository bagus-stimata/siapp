package com.desgreen.education.siapp.security_config;

import com.desgreen.education.siapp.AppPublicService;
import com.desgreen.education.siapp.security_model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures spring security, doing the following:
 * <li>Bypass security checks for static resources,</li>
 * <li>Restrict access to the application, allowing only logged in users,</li>
 * <li>Set up the login form,</li>
 * <li>Configures the {@link AuthUserDetailsService}.</li>

 */
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	private static final String LOGIN_PROCESSING_URL = "/login";
	private static final String LOGIN_FAILURE_URL = "/login?error";
	private static final String LOGIN_URL = "/login";
	private static final String LOGOUT_SUCCESS_URL = "/" ;

	// private final UserDetailsService userDetailsService;
	// @Autowired
	// public SecurityConfiguration(UserDetailsService userDetailsService) {
	// 	this.userDetailsService = userDetailsService;
	// }
    @Autowired
	private UserDetailsService userDetailsService;
	
	/**
	 * Khusus untuk Vaadin Rahasianya ada pada
	 * ## ConfigureUIServiceInitListener.java
	 * 
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}	
	/**
	 * Registers our UserDetailsService and the password encoder to be used on login attempts.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	/**
	 * Annotation @Secure(Role.ADMIN) tidak dapat berjalan jika hanya menggunakan ini
	 */
	// @Autowired
    // public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    // 	auth.inMemoryAuthentication()
    //       .withUser("user")
    //         .password(passwordEncoder().encode("user"))
    //         // .roles(Role.USER, "USER")
    //         .roles("USER")
    //       .and()
    //       .withUser("admin")
    //         .password(passwordEncoder().encode("admin"))
    //         // .roles(Role.ADMIN, "ADMIN");
    //         .roles("ADMIN");
    // }

    // @Bean
    // public PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
	// }
		

	/**
	 * Require login to access internal pages and configure login form.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Not using Spring CSRF here to be able to use plain HTML for the login page
		http.csrf().disable()

				// Register our CustomRequestCache, that saves unauthorized access attempts, so
				// the user is redirected after login.
				.requestCache().requestCache(new CustomRequestCache())

				.and()
				.authorizeRequests()
				// .antMatchers( "/**" ).permitAll() // Untuk pMelakukan Permit kepada semua dan tidak perlu otorisasi: Mengacu pada contoh diatas
				// // .antMatchers( "/", "/home", "resources/**", "/registration" ).permitAll() // daftar un-secure page

				/**
				 * For Public View /Unsecure Page
				 * Menggunakan Ini dan ConfigureUIServiceInitListener
				 */
				.antMatchers(
						"/PpdbListView/**", "/PpdbListView",
						"/PpdbSelectUserView/**", "/PpdbSelectUserView",
						"/PpdbOnlineView/**", "/PpdbOnlineView",
						"/Home",
						"/registration")
				.permitAll()

				// Restrict access to our application.
				.and().authorizeRequests()

				// Allow all flow internal requests.
				.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

				// Allow all requests by logged in users.
				.anyRequest().hasAnyAuthority(Role.getAuthRoles())

				// Configure the login page.
				.and().formLogin().loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
				.failureUrl(LOGIN_FAILURE_URL)

				// Register the success handler that redirects users to the page they last tried
				// to access
				.successHandler(new SavedRequestAwareAuthenticationSuccessHandler())

				// Configure logout
				.and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
	}

	/**
	 * Allows access to static resources, bypassing Spring security.
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				// Vaadin Flow static resources
				"/VAADIN/**",

				// the standard favicon URI
				"/favicon.ico",

				// the robots exclusion standard
				"/robots.txt",

				// web application manifest
				"/manifest.webmanifest",
				"/sw.js",
				"/offline-page.html",

				// icons and images
				"/icons/**",
				"/images/**",

				// (development mode) static resources
				"/frontend/**",

				// (development mode) webjars
				"/webjars/**",

				// (development mode) H2 debugging console
				"/h2-console/**",

				// (production mode) static resources
				"/frontend-es5/**", "/frontend-es6/**"
		);
	}


	// public static String uploadDirectory= System.getProperty("user.home") + "\\images";
	//  public static String uploadDirectory = "/Users/yhawin/gambarnya";
	public static String uploadDirectory = AppPublicService.FILE_PATH;

	/**
	 * THIS DOESN'T WORK FOR VAADIN (work just in springboot leaflet)
	 * gambarnya --> /images
	 */
	//  @Override
	//  public void addResourceHandlers(ResourceHandlerRegistry registry) {
	//      registry.addResourceHandler("/images/**").addResourceLocations("file:" + uploadDirectory+"/");
	//  }

//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//
//		registry.addResourceHandler("/images/**", "/css/**", "/img/**", "/js/**")
//				.addResourceLocations(
//						"file:" + uploadDirectory + "/"
//						,"classpath:/static/home"
//						,"classpath:/static/js/"
//						,"classpath:/static/css/"
//						,"classpath:/static/src/"
//						,"classpath:/static/img/"
//						,"classpath:/static/assets/"
//						,"classpath:/static/template/"
//						,"classpath:/static/webjars/");
//	}


}
