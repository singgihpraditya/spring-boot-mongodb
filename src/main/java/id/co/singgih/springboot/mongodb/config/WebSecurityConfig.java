package id.co.singgih.springboot.mongodb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String[] AUTH_WHITELIST = {
	        "**/swagger-resources/**",
	        "**/swagger-ui.html",
	        "**/v2/api-docs",
	        "**/webjars/**"
	};
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
    	httpSecurity
		.cors().and()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers(AUTH_WHITELIST).permitAll();	
    }
}
