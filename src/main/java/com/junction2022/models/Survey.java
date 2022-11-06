package com.junction2022.models;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.jena.ontology.OntClass;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junction2022.common.exceptions.InvalidDataException;
import com.junction2022.repositories.rdf.FreshAirOntology;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Survey extends FreshAirEntity {

	public static final Comparator<Survey> COMPARATOR_BY_CODE =
			Comparator.nullsFirst(Comparator.comparing(Survey::getCode));

	public static final Comparator<Survey> COMPARATOR_BY_UUID =
			Comparator.nullsFirst(Comparator.comparing(Survey::getUuid));

	@JsonIgnore
	private String code;
	private String name;
	private String shortName;
	
	@JsonIgnore
	private String fileName;
	
	private List<Question> questions;
	private List<MentalSuggestion> suggestions;
	
	public Optional<Question> getQuestionByUuid(final UUID questionUuid) {
		return questions
				.stream()
				.filter(question -> question.getUuid().equals(questionUuid))
				.findAny();
	}
	
	public Map<String, String> asShortMap() {
		return Map.of(
				FreshAirVocabulary.UUID_FIELD, getUuid().toString(),
				FreshAirVocabulary.NAME_FIELD, getName(),
				FreshAirVocabulary.CODE_FIELD, getCode());
	}

	@Override
	public OntClass getTypeRef() {
		return FreshAirOntology.Metadata.Survey;
	}
	
	public int getMaxPoint() {
		int sum = 0;
		if (questions != null) {			
			for (final Question question : questions) {
				sum += question.getMax();
			}
		}
		return sum;
	}
	
	public MentalSuggestion findSuggestion(SurveyResult surveyResult) {
		double totalPoint = surveyResult.getTotalPoint();
		double maxPoint = getMaxPoint();
		return findSuggestionByPoint(totalPoint / maxPoint * 100.0);
	}
	
	public MentalSuggestion findSuggestionByPoint(double percentagePoint) {
		MentalSuggestion suggestion = null;
		if (suggestions != null) {
			suggestion = 
				suggestions
					.stream()
					.sorted()
					.filter(s -> percentagePoint < s.getMaxPercentage())
					.findFirst()
					.orElse(null);
		}
		
		if (suggestion == null) {
			throw new InvalidDataException("No correct suggestion found");
		}
		
		return suggestion;
	}
	
}
