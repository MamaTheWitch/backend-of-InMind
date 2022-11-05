package com.junction2022;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import fi.vtt.bimlinker.rdf.jena.provider.JenaProvider;
import fi.vtt.bimlinker.rdf.jena.provider.JenaProviderException;
import fi.vtt.bimlinker.rdf.jena.provider.JenaProviderFactory;
import fi.vtt.bimlinker.rdf.jena.provider.JenaProviderProperties;
import fi.vtt.bimlinker.rdf.jena.provider.ModelConnection;
import fi.vtt.utils.text.TextUtils;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class} )
@Log4j2
public class FreshAirApplication {
	private static FreshAirApplication instance$;

	@Autowired
	@Getter
	private FreshAirProperties properties;

	@Autowired
	@Getter
	private ApplicationContext context;


	private JenaProvider jenaProvider;

	@PostConstruct
	public void init() {
		instance$ = this;
	}

	public static FreshAirApplication getInstance() {
		return instance$;
	}
	
//    @Bean
//    WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").allowedOrigins("*");
//            }
//        };
//    }

	public static <T> T getBean(final Class<T> type) {
		return instance$.getContext().getBean(type);
	}


	public static void main(final String[] args) {
		SpringApplication.run(FreshAirApplication.class, args);
	}

	public String getRealBaseUri() {
		final String baseUri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		return baseUri.endsWith("/") ? baseUri : baseUri + "/";
	}

	public String getVirtualBaseUri() {
		final String virtualBaseUri = getProperties().getBaseUri().getVirtualBaseUri();
		return TextUtils.isEmptyOrNull(virtualBaseUri) ? getRealBaseUri() : virtualBaseUri;
	}

	public JenaProvider getJenaProvider() {
		if (jenaProvider == null) {
			final JenaProviderProperties jenaProviderProperties = getProperties().getJenaProvider();
			try {
				jenaProvider = JenaProviderFactory.getProvider(jenaProviderProperties);
			} catch (final JenaProviderException e) {
				log.error(e);
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
			}
		}
		return jenaProvider;
	}

	public ModelConnection openJenaModel(final String name) {
		try {
			return getJenaProvider().openModelEx(name);
		} catch (final JenaProviderException e) {
			log.error(e);
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		}
	}
	




}

