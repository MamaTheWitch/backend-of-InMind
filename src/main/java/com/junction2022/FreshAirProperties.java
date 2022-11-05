package com.junction2022;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.junction2022.common.config.BasicSecurityProperties;

import fi.vtt.bimlinker.rdf.jena.provider.JenaProviderProperties;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("freshair")
@Component
@Getter
@Setter
public class FreshAirProperties {

	@Getter
	@Setter
	public static class BaseUriProperties {
	     private String virtualBaseUri;
	     private boolean useActualBaseUriForHref;
	}

	private BaseUriProperties baseUri;
	private JenaProviderProperties jenaProvider;
	private BasicSecurityProperties security;

}
