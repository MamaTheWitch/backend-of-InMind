package com.junction2022.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class QuestionAnswer {

	private Question question;
	private int value;

}
