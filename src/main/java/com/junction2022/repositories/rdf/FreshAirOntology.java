package com.junction2022.repositories.rdf;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

public abstract class FreshAirOntology {

	public static final String BASE_URI = "https://ba.vtt.fi/freshAir/";

	public static class Metadata {
		public static final String NAMESPACE_PREFIX = "md";
		public static final String NAMESPACE_URI = BASE_URI + "metadata#";

		public static final OntModel ONT_MODEL = ModelFactory.createOntologyModel();

		public static final OntClass Question = ONT_MODEL.createClass(NAMESPACE_URI + "Question");
		public static final OntClass QuestionCategory = ONT_MODEL.createClass(NAMESPACE_URI + "QuestionCategory");
		public static final OntClass Service = ONT_MODEL.createClass(NAMESPACE_URI + "MentalService");

		public static final DatatypeProperty hasCode = ONT_MODEL.createDatatypeProperty(NAMESPACE_URI + "hasCode");
		public static final DatatypeProperty hasName = ONT_MODEL.createDatatypeProperty(NAMESPACE_URI + "hasName");
		public static final DatatypeProperty hasShortName = ONT_MODEL.createDatatypeProperty(NAMESPACE_URI + "hasShortName");
		public static final DatatypeProperty hasUuid = ONT_MODEL.createDatatypeProperty(NAMESPACE_URI + "hasUuid");
		public static final DatatypeProperty isCore = ONT_MODEL.createDatatypeProperty(NAMESPACE_URI + "isCore");
		public static final DatatypeProperty storedInFile = ONT_MODEL.createDatatypeProperty(NAMESPACE_URI + "storedInFile");

		public static final ObjectProperty hasQuestion = ONT_MODEL.createObjectProperty(NAMESPACE_URI + "hasQuestion");
		public static final ObjectProperty hasDescription = ONT_MODEL.createObjectProperty(NAMESPACE_URI + "hasDescription");
		public static final ObjectProperty hasFormula = ONT_MODEL.createObjectProperty(NAMESPACE_URI + "hasFormula");
		public static final ObjectProperty hasOwner = ONT_MODEL.createObjectProperty(NAMESPACE_URI + "hasOwner");
		public static final ObjectProperty hasMeasurementProcess = ONT_MODEL.createObjectProperty(NAMESPACE_URI + "hasMeasurementProcess");
		public static final ObjectProperty hasMonitoringInterval = ONT_MODEL.createObjectProperty(NAMESPACE_URI + "hasMonitoringInterval");

		private Metadata() {}
	}

	public static class QuestionRecords {
		public static final String NAMESPACE_PREFIX = "kr";
		public static final String NAMESPACE_URI = BASE_URI + "questionRecords#";

		public static final OntModel ONT_MODEL = ModelFactory.createOntologyModel();

		public static final OntClass QuestionRecord = ONT_MODEL.createClass(NAMESPACE_URI + "QuestionAnswer");

		public static final ObjectProperty hasQuestion = ONT_MODEL.createObjectProperty(NAMESPACE_URI + "hasQuestion");
		public static final ObjectProperty hasService = ONT_MODEL.createObjectProperty(NAMESPACE_URI + "hasService");

		public static final OntClass QuestionRecordGroup = ONT_MODEL.createClass(NAMESPACE_URI + "QuestionairResult");

		public static final ObjectProperty belongsToQuestion = ONT_MODEL.createObjectProperty(NAMESPACE_URI + "belongsToQuestion");
		public static final ObjectProperty belongsToService = ONT_MODEL.createObjectProperty(NAMESPACE_URI + "belongsToService");
		public static final ObjectProperty hasRecord = ONT_MODEL.createObjectProperty(NAMESPACE_URI + "hasRecord");

		public static final DatatypeProperty hasTimestamp = ONT_MODEL.createDatatypeProperty(NAMESPACE_URI + "hasTimestamp");
		public static final DatatypeProperty hasValue = ONT_MODEL.createDatatypeProperty(NAMESPACE_URI + "hasValue");

		private QuestionRecords() {}
	}

	private FreshAirOntology() {}

}
