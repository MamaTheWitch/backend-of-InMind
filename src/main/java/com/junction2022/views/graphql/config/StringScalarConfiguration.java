package com.junction2022.views.graphql.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.junction2022.views.graphql.scalars.StringScalarCoercing;
import com.junction2022.views.graphql.scalars.UriOrIdScalar;
import com.junction2022.views.graphql.scalars.UriScalar;
import com.junction2022.views.graphql.scalars.UuidScalar;

import graphql.kickstart.servlet.apollo.ApolloScalars;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;

/**
 *
 * @author Nam Vu
 */
@Configuration
public class StringScalarConfiguration {

	public static final String URI_SCALAR_NAME = "Uri";
	public static final String URI_SCALAR_DESCRIPTION = "URI Scalar";

	@Bean
	public GraphQLScalarType defineScalarUri() {
		return GraphQLScalarType
				.newScalar()
				.name(URI_SCALAR_NAME)
				.description(URI_SCALAR_DESCRIPTION)
				.coercing(new StringScalarCoercing<>(UriScalar::new))
				.build();
	}

	public static final String URI_OR_ID_SCALAR_NAME = "UriOrId";
	public static final String URI_OR_ID_SCALAR_DESCRIPTION = "URI or ID Scalar";

	@Bean
	public GraphQLScalarType defineScalarUriOrId() {
		return GraphQLScalarType
				.newScalar()
				.name(URI_OR_ID_SCALAR_NAME)
				.description(URI_OR_ID_SCALAR_DESCRIPTION)
				.coercing(new StringScalarCoercing<>(UriOrIdScalar::new))
				.build();
	}

	public static final String UUID_SCALAR_NAME = "Uuid";
	public static final String UUID_SCALAR_DESCRIPTION = "UUID Scalar";

	@Bean
	public GraphQLScalarType defineScalarUuid() {
		return GraphQLScalarType
				.newScalar()
				.name(UUID_SCALAR_NAME)
				.description(UUID_SCALAR_DESCRIPTION)
				.coercing(new StringScalarCoercing<>(UuidScalar::new))
				.build();
	}

	public static final String REGEX_SCALAR_NAME = "RegEx";
	public static final String REGEX_SCALAR_DESCRIPTION = "Regular Expression Scalar";

	@Bean
	public GraphQLScalarType defineScalarRegex() {
		return GraphQLScalarType
				.newScalar()
				.name(REGEX_SCALAR_NAME)
				.description(REGEX_SCALAR_DESCRIPTION)
				.coercing(new StringScalarCoercing<>(String::new))
				.build();
	}


}
