package com.junction2022.models;

import org.apache.jena.rdf.model.Resource;

import com.junction2022.repositories.rdf.FreshAirOntology;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Question extends FreshAirEntity {
	
	private String text;
	private String type;
	private int min;
	private int max;
	
	@Override
	public Resource getTypeRef() {
		return FreshAirOntology.Metadata.Question;
	}

}
