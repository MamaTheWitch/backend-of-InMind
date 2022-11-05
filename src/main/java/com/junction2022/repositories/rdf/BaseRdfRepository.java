package com.junction2022.repositories.rdf;

import org.springframework.beans.factory.annotation.Autowired;

import com.junction2022.FreshAirApplication;

abstract class BaseRdfRepository {

	@Autowired
	protected FreshAirApplication application;

	@Autowired
	protected NameFormatter nameFormatter;

}
