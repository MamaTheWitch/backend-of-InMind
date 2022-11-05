package com.junction2022.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author Nam Vu
 *
 */
@Configuration
@EnableWebMvc
public class StaticResourceConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/resources/**")
//				.addResourceLocations(new String[] { "/resources/", "classpath:/resources/" });

		registry.addResourceHandler("/resources/**")
				.addResourceLocations(new String[] { "/static/", "classpath:/static/" });

		registry
				.addResourceHandler("/vendor/**").addResourceLocations("classpath:/static/vendor/");
	}

}
