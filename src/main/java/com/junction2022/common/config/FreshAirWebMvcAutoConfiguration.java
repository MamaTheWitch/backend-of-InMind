package com.junction2022.common.config;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author Nam Vu
 *
 */
@Configuration
@ComponentScan("com.junction2022.views.rest")
public class FreshAirWebMvcAutoConfiguration extends WebMvcAutoConfiguration implements WebMvcConfigurer {

//    @Override
//	public void addFormatters(final FormatterRegistry registry) {
//        registry.addConverter(new BimLinkerOverwritingMethod.BimLinkerOverwrittingMethodConverter());
//    }
	
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*");
    }
	
	
}
