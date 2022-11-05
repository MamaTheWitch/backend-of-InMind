package com.junction2022.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentalSuggestion implements Comparable<MentalSuggestion> {
	
	private MentalStateEnum state;
	private String text;
	private int maxPercentage;
	
	@Override
	public int compareTo(final MentalSuggestion o) {
		return Integer.compare(maxPercentage, o.maxPercentage);
	}

}
