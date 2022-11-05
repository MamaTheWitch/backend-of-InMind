package com.junction2022.repositories.rdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import fi.vtt.utils.function.FunctionEx;


class RdfRepositoryUtils {

	public static <T> void mapOptionalPropertiesToResource(
			final T source,
			final Resource target,
			final Map<Property, Function<T, Object>> propertyExtractors) {

		for (final var entry : propertyExtractors.entrySet()) {
			final Property property = entry.getKey();
			final Function<T, Object> propertyExtractor = entry.getValue();
			mapObjectPropertyToResource(source, target, property, propertyExtractor);
		}

	}

	public static <T> void mapObjectPropertyToResource(
			final T source,
			final Resource target,
			final Property property,
			final Function<T, Object> propertyExtractor) {

		final Object propertyValue = propertyExtractor.apply(source);
		if (propertyValue != null) {
			target.addLiteral(property, propertyValue);
		}

	}

	public static <T, E extends Exception> List<T> mapResultSetToList(
			final ResultSet resultSet,
			final FunctionEx<QuerySolution, T, E> mapper) throws E
	{
		if (resultSet == null) {
			return null;
		}

		final List<T> list = new ArrayList<>();
		while (resultSet.hasNext()) {
			list.add(mapper.apply(resultSet.nextSolution()));
		}
		return list;
	}

	protected static <T> T mapResultSetToObject(final ResultSet resultSet, final Function<QuerySolution, T> mapper) {
		return resultSet != null && resultSet.hasNext() ? mapper.apply(resultSet.nextSolution()) : null;
	}

	private RdfRepositoryUtils() {
	}

}
