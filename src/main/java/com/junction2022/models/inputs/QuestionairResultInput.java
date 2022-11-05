package com.junction2022.models.inputs;

import java.util.List;
import java.util.UUID;

import com.junction2022.common.exceptions.ResourceNotFoundException;
import com.junction2022.models.QuestionCategory;
import com.junction2022.models.QuestionCategorySet;
import com.junction2022.models.QuestionairResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionairResultInput extends FreshAirEntityInput {
	
	private UUID categoryUuid;
	private List<QuestionAnswerInput> answers;
	
	public QuestionairResult toQuestionairResult(final QuestionCategorySet questionCategories) {
		final QuestionCategory category = questionCategories.getQuestionCategory(categoryUuid).orElse(null);
		if (category == null) {
			throw new ResourceNotFoundException("Unknown questionair: " + categoryUuid);
		}
		
		final QuestionairResult questionairResult = new QuestionairResult(category);
		questionairResult.setAnswers(
				answers
					.stream()
					.map(input -> input.toQuestionAnswer(category))
					.toList());
		return questionairResult;
		
	}


}
