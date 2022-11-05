package com.junction2022.views.graphql.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import graphql.kickstart.servlet.apollo.ApolloScalars;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;

@Configuration
public class OtherScalarConfiguration {

	@Bean
	public GraphQLScalarType defineScalarUpload() {
		// https://github.com/graphql-java-kickstart/graphql-spring-boot/issues/287
		return ApolloScalars.Upload;
	}

	@Bean
	public GraphQLScalarType defineScalarJson() {
		// https://github.com/graphql-java/graphql-java-extended-scalars
		return ExtendedScalars.Json;
	}


}
