package com.example;

import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@SpringBootApplication
@EnableWebSecurity
@Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class DemoApplication extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		new SpringApplicationBuilder(DemoApplication.class)
				.properties("management.context-path:/actuator").run(args);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("secret").roles("ACTUATOR");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		BasicAuthenticationEntryPoint authenticationEntryPoint = new BasicAuthenticationEntryPoint();
		authenticationEntryPoint.setRealmName("example");
		http.httpBasic().authenticationEntryPoint(authenticationEntryPoint);
		http.authorizeRequests().antMatchers("/actuator").denyAll();
	}

}