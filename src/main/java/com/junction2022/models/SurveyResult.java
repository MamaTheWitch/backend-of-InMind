package com.junction2022.models;

import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import com.junction2022.repositories.rdf.NameFormatter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SurveyResult {
	
	private final Survey survey;
	private List<QuestionAnswer> answers;

//	public String getUri() {
//		final String surveyLocalId = survey.getLocalId();
//		return NameFormatter.getInstance().formatDoubleEntityUri(serviceLocalId, surveyLocalId);
//	}
//
//	public Resource getRef() {
//		return ResourceFactory.createResource(getUri());
//	}

}
