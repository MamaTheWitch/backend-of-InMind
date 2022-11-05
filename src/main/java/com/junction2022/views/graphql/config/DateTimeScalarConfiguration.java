package com.junction2022.views.graphql.config;

import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.junction2022.views.graphql.scalars.StringScalarCoercing;

import graphql.schema.GraphQLScalarType;

@Configuration
public class DateTimeScalarConfiguration {

	public static final String DATE_TIME_SCALAR_NAME = "DateTime";

//	 @Bean
//	public GraphQLScalarType defineOffsetDateTimeScalarType() {
//		// https://github.com/graphql-java/graphql-java-extended-scalars
//		return ExtendedScalars.DateTime;
//	}

	@Bean
	public GraphQLScalarType defineInstantScalarType() {
		return GraphQLScalarType
				.newScalar()
				.name(DATE_TIME_SCALAR_NAME)
				.description("Instant Scalar")
				.coercing(new StringScalarCoercing<Instant>(Instant::parse, DateTimeScalarConfiguration::serializeInstant))
				.build();
	}

	private static String serializeInstant(final Object outputObject) {
		final Instant instant;
		if (outputObject instanceof final TemporalAccessor temporalAccessor) {
			instant = Instant.from(temporalAccessor);
		} else if (outputObject instanceof final Calendar calendar) {
			instant = calendar.toInstant();
		} else {
			throw new IllegalArgumentException(
					String.format(
							"Expected something we can convert to '%s' but was '%s.",
							Instant.class,
							outputObject.getClass())
					);
		}
		return instant.toString();
	}

}
