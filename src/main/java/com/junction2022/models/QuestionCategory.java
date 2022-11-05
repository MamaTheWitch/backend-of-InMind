package com.junction2022.models;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.jena.ontology.OntClass;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junction2022.repositories.rdf.FreshAirOntology;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionCategory extends FreshAirEntity {

	public static final Comparator<QuestionCategory> COMPARATOR_BY_CODE =
			Comparator.nullsFirst(Comparator.comparing(QuestionCategory::getCode));

	public static final Comparator<QuestionCategory> COMPARATOR_BY_UUID =
			Comparator.nullsFirst(Comparator.comparing(QuestionCategory::getUuid));

	private String code;
	private String name;
	private String shortName;
	
	private String fileName;
	
	private  List<Question> questions;
	
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
		return FreshAirOntology.Metadata.QuestionCategory;
	}

}
