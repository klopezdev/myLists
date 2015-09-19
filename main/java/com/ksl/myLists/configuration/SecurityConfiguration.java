package com.ksl.myLists.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

/**
 * Ehrmagod! This is sooo much nicer than xml configuration!
 * 
 * @author Keith Lopez
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	/**
	 * Expose the userDetailsService created in config(AuthenticationManagerBuilder)
	 * for use in the TokenBasedRememberMeServices
	 */
	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return super.userDetailsServiceBean();
	}
	
	/**
	 * Make sure those passwords are not readable
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * Provides custom sql for retrieving user information from the data source
	 */
	@Override
	@Order(Ordered.HIGHEST_PRECEDENCE)
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		.jdbcAuthentication()
		.dataSource(dataSource)
		.usersByUsernameQuery("SELECT emailAddress AS 'username', password, activationCode IS NULL AS 'enabled' FROM User WHERE emailAddress = ?")
		.authoritiesByUsernameQuery("SELECT emailAddress AS 'username', role FROM User WHERE emailAddress = ?")
		.passwordEncoder(passwordEncoder);
	}
	
	/**
	 * Get CSRF up for production!
	 * 
	 * Allows basic authentication and form authentication.
	 * 
	 * Allows unauthorized access to all landing resources including the account activation RESTful method.
	 * All other requests must be authenticated. The RESTful method handling inbound requests from the
	 * external Twilio service will require a user with the role of TWILIO or ADMIN. 
	 * 
	 * Remembers user sessions.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		TokenBasedRememberMeServices rememberMeService = new TokenBasedRememberMeServices("1", userDetailsService);
		rememberMeService.setParameter("rememberMePlease");
		rememberMeService.setAlwaysRemember(false);
		
		http
			.rememberMe()
				.rememberMeServices(rememberMeService)
				.key("1")
				.and()
			.csrf()
				.disable()
			.formLogin()
				.defaultSuccessUrl("/dashboard/dashboard.html")
				.loginPage("/landing/landing.html#/registration?showLoginForm")
				.loginProcessingUrl("/landing/login")
				.failureUrl("/landing/landing.html#/registration?loginError")
				.usernameParameter("emailAddress")
				.passwordParameter("password")
				.and()
			.httpBasic()
				.and()
			.logout()
				.logoutSuccessUrl("/landing/landing.html#/registration")
				.invalidateHttpSession(true)
				.and()
			.authorizeRequests()
				.antMatchers("/landing/**").permitAll()
				.antMatchers("/list/inbound").hasAnyAuthority("TWILIO", "ADMIN")
				.anyRequest().authenticated();
	}
}
