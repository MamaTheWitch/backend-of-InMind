package com.junction2022.common.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.junction2022.FreshAirProperties;

import lombok.extern.log4j.Log4j2;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private FreshAirProperties properties;

	// https://howtodoinjava.com/spring-boot2/security-rest-basic-auth-example/
	// https://www.baeldung.com/spring-boot-security-autoconfiguration

    @Override
    protected void configure(final HttpSecurity http) throws Exception
    {
        final var authorizedUrl =
        		http
		         .csrf().disable()
		         .authorizeRequests().anyRequest();

    	final List<UserProperties> users =
    			properties
    				.getSecurity()
    				.getUsers();

    	if (users != null && !users.isEmpty()) {
    		log.info("HTTP Basic Authentication enabled for {} users.", users.size());
    		authorizedUrl
    			.authenticated()
    			.and()
    			.httpBasic();
    	} else {
    		log.info("HTTP Basic Authentication disabled.");
    		authorizedUrl
    			.anonymous();
    	}
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder authBuilder)
            throws Exception
    {
    	final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    	final List<UserProperties> userPropertiesList =
    			Optional
    				.ofNullable(properties.getSecurity())
    				.map(BasicSecurityProperties::getUsers)
    				.orElseGet(ArrayList::new);

    	for (final UserProperties userProperties : userPropertiesList) {
    		authBuilder
	    		.inMemoryAuthentication()
	    		.withUser(userProperties.getName())
	    		.password(encoder.encode(userProperties.getPassword()))
	    		.roles(userProperties.getRoles());
    	}

    }

//	@Bean
//	public static InMemoryUserDetailsManager userDetailsService() {
//		User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
//		UserDetails rob = userBuilder.username("rob").password("rob").roles("USER").build();
//		UserDetails admin = userBuilder.username("admin").password("admin").roles("USER", "ADMIN").build();
//		return new InMemoryUserDetailsManager(rob, admin);
//	}
//
//
//    private static UserDetails buildUser(final UserProperties properties) {
//		return
//				User
//					.withDefaultPasswordEncoder()
//					.username(properties.getName())
//					.password(properties.getPassword())
//					.roles(properties.getRoles().toArray(new String[properties.getRoles().size()]))
//					.build();
//
//    }

}
