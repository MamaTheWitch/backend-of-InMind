package com.junction2022.models.inputs;

import java.util.List;
import java.util.UUID;

import com.junction2022.common.exceptions.ResourceNotFoundException;
import com.junction2022.models.Survey;
import com.junction2022.models.SurveySet;
import com.junction2022.models.SurveyResult;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SurveyResultInput extends FreshAirEntityInput {
	
	private UUID categoryUuid;
	private List<QuestionAnswerInput> answers;
	
	public SurveyResult toSurveyResult(final SurveySet surveys) {
		final Survey category = surveys.getSurvey(categoryUuid).orElse(null);
		if (category == null) {
			throw new ResourceNotFoundException("Unknown questionair: " + categoryUuid);
		}
		
		final SurveyResult questionairResult = new SurveyResult(category);
		questionairResult.setAnswers(
				answers
					.stream()
					.map(input -> input.toQuestionAnswer(category))
					.toList());
		return questionairResult;
		
	}


}
