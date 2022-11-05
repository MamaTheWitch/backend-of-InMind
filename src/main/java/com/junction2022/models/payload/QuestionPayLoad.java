package com.junction2022.models.payload;

import java.util.Optional;
import java.util.function.Function;

import org.apache.jena.rdf.model.Resource;

import com.junction2022.models.Question;

public class QuestionPayLoad extends FreshAirEntityPayLoad<Question> {

	public QuestionPayLoad(
			final Resource recordRef,
			final Function<Resource, Optional<Question>> recordSupplier) {
		super(recordRef, recordSupplier);
	}

	public QuestionPayLoad(final Question entity) {
		super(entity);
	}

}
