package com.junction2022.models.inputs;

import java.util.UUID;

import com.junction2022.common.exceptions.ResourceNotFoundException;
import com.junction2022.models.Question;
import com.junction2022.models.QuestionAnswer;
import com.junction2022.models.Survey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionAnswerInput extends FreshAirEntityInput {

	private UUID questionUuid;
	private int value;

	public QuestionAnswer toQuestionAnswer(final Survey category) {
		final Question question = category.getQuestionByUuid(questionUuid).orElse(null);
		if (question == null) {
			throw new ResourceNotFoundException("Unknown question UUID: " + questionUuid);
		}
		return new QuestionAnswer(question, value);
	}

}
