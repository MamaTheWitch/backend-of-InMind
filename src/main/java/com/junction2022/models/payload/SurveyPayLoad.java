package com.junction2022.models.payload;

import java.util.Optional;
import java.util.function.Function;

import org.apache.jena.rdf.model.Resource;

import com.junction2022.models.Survey;

public class SurveyPayLoad extends FreshAirEntityPayLoad<Survey> {

	public SurveyPayLoad(
			final Resource recordRef,
			final Function<Resource, Optional<Survey>> recordSupplier) {
		super(recordRef, recordSupplier);
	}

	public SurveyPayLoad(final Survey entity) {
		super(entity);
	}

}
