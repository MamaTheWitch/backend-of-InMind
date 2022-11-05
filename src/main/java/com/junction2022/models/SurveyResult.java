package com.junction2022.models;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SurveyResult {
	
	private final Survey survey;
	private List<QuestionAnswer> answers;
	
	public int getTotalPoint() {
		int sum = 0;
		for (final QuestionAnswer answer: answers) {
			sum += answer.getValue();
		}
		return sum;
	}	

}
