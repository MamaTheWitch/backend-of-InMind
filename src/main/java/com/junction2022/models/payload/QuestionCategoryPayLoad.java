package com.junction2022.models.payload;

import java.util.Optional;
import java.util.function.Function;

import org.apache.jena.rdf.model.Resource;

import com.junction2022.models.QuestionCategory;

public class QuestionCategoryPayLoad extends FreshAirEntityPayLoad<QuestionCategory> {

	public QuestionCategoryPayLoad(
			final Resource recordRef,
			final Function<Resource, Optional<QuestionCategory>> recordSupplier) {
		super(recordRef, recordSupplier);
	}

	public QuestionCategoryPayLoad(final QuestionCategory entity) {
		super(entity);
	}

}
