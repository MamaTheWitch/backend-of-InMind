package com.junction2022.views.graphql.scalars;

import fi.vtt.utils.function.FunctionEx;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;


/**
 *
 * @author Nam Vu
 *
 * @param <T>
 */
public class StringScalarCoercing<T> implements Coercing<T, String> {

	private final FunctionEx<Object, String, IllegalArgumentException> serializer;
	private final FunctionEx<String, T, IllegalArgumentException> deserializer;

	public StringScalarCoercing(
			final FunctionEx<String, T, IllegalArgumentException> deserializer,
			final FunctionEx<Object, String, IllegalArgumentException> serializer) {
		this.deserializer = deserializer;
		this.serializer = serializer;
	}


	public StringScalarCoercing(final FunctionEx<String, T, IllegalArgumentException> deserializer) {
		this(deserializer, Object::toString);
	}


	@Override
	public String serialize(final Object dataFetcherResult) throws CoercingSerializeException {
		try {
			return serializer.apply(dataFetcherResult);
		} catch (final IllegalArgumentException e) {
			throw new CoercingSerializeException(e);
		}
	}


	@Override
	public T parseValue(final Object input) throws CoercingParseValueException {
		try {
			return parse(input);
		} catch (final IllegalArgumentException e) {
			throw new CoercingParseValueException(e);
		}
	}


	@Override
	public T parseLiteral(final Object input) throws CoercingParseLiteralException {
		try {
			return parse(input);
		} catch (final IllegalArgumentException e) {
			throw new CoercingParseLiteralException(e);
		}
	}


	private T parse(final Object input) throws IllegalArgumentException {
		if (input instanceof final StringValue stringInput) {
			return deserializer.apply(stringInput.getValue());
		} else if (input instanceof final String stringInput2) {
			return deserializer.apply(stringInput2);
		}
		throw new IllegalArgumentException("String input expected, but was " + input.getClass().getName());
	}

}