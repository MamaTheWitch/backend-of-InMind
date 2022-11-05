package com.junction2022.models;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QuestionCategorySet {

	@Getter
	private final List<QuestionCategory> questionCategories;

	private Map<UUID, Question> flatQuestionMap;


	public Optional<QuestionCategory> getQuestionCategory(final UUID categoryUuid) {
		return getQuestionCategories()
				.stream()
				.filter(category -> category.getUuid().equals(categoryUuid))
				.findFirst();
	}


	/////////////////////////////////
	// Question cards
	/////////////////////////////////

	public Optional<Question> getQuestion(final QuestionCategory category, final UUID questionUuid) {
		return category
				.getQuestions()
				.stream()
				.filter(question -> question.getUuid().equals(questionUuid))
				.findFirst();
	}

	public Map<UUID, Question> getFlatQuestionMap() throws IOException {
		if (flatQuestionMap == null) {
			flatQuestionMap = new HashMap<>();

			for (final QuestionCategory category : getQuestionCategories()) {
				final List<Question> questions = category.getQuestions();
				if (questions != null) {
					for (final Question question : questions) {
						flatQuestionMap.put(question.getUuid(), question);
					}
				}
			}
		}
		return flatQuestionMap;
	}

	public Optional<Question> getQuestion(final UUID questionUuid) throws IOException {
		return Optional.ofNullable(getFlatQuestionMap().get(questionUuid));
	}


}
