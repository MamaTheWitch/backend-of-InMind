package com.junction2022.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentalAssessment {
	
	private int totalPoint;
	private int maxPoint;
	private double percentagePoint;
	private MentalSuggestion suggestion;

}
