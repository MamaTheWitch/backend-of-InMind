package com.junction2022.repositories.rdf;
//package com.junction2022.repositories.rdf;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.apache.jena.rdf.model.Literal;
//import org.apache.jena.rdf.model.Model;
//import org.apache.jena.rdf.model.ModelFactory;
//import org.apache.jena.rdf.model.Resource;
//import org.apache.jena.vocabulary.RDF;
//import org.apache.jena.vocabulary.RDFS;
//import org.springframework.stereotype.Service;
//
//import fi.vtt.bimlinker.rdf.jena.provider.ModelConnection;
//import com.junction2022.models.Question;
//import com.junction2022.models.Survey;
//import com.junction2022.models.payload.QuestionPayLoad;
//import com.junction2022.models.payload.SurveyPayLoad;
//import com.junction2022.repositories.rdf.FreshAirOntology.Metadata;
//
//@MentalService
//public class MetadataRdfRepository extends BaseRdfRepository {
//
//	private ModelConnection getMetadataGraph() {
//		return application.openJenaModel(Metadata.NAMESPACE_URI);
//	}
//
//	public SurveyPayLoad createSurvey(final Survey survey) {
//		final Model model = ModelFactory.createDefaultModel();
//
//		final Resource surveyResource =
//				model
//					.createResource(survey.getUri())
//						.addProperty(RDF.type, survey.getTypeRef())
//						.addProperty(RDFS.label, survey.getName());
//
//		RdfRepositoryUtils
//			.mapOptionalPropertiesToResource(
//				survey, surveyResource,
//				Map.of(
//						Metadata.hasCode, Survey::getCode,
//						Metadata.hasShortName, Survey::getShortName,
//						Metadata.storedInFile, Survey::getFileName
//				)
//			);
//
//		try (final ModelConnection modelConnection = getMetadataGraph()) {
//			modelConnection.pushStatements(model);
//		}
//
//		return new SurveyPayLoad(survey);
//	}
//
//	public List<Survey> getSurveys() {
//
//		final var sparql =
//				nameFormatter
//					.getJenaQueryBuilder()
//					.buildParameterizedSparql(
//							"""
//							SELECT ?ref ?name ?code ?shortName ?fileName
//							FROM md:
//							WHERE {
//									?ref
//										a ?Survey ;
//										rdfs:label ?name .
//
//									OPTIONAL { ?ref ?hasCode ?code }
//									OPTIONAL { ?ref ?hasShortName ?shortName }
//									OPTIONAL { ?ref ?storedInFile ?fileName }
//							}
//							""",
//							null,
//							Map.of(
//								"Survey", Metadata.Survey,
//								"hasCode", Metadata.hasCode,
//								"hasShortName", Metadata.hasShortName,
//								"storedInFile", Metadata.storedInFile
//							));
//
//		try (final ModelConnection modelConnection = getMetadataGraph();
//			final var queryExecution = modelConnection.createQueryExecution(sparql.asQuery())) {
//			return RdfRepositoryUtils
//						.mapResultSetToList(
//							queryExecution.execSelect(),
//							querySolution -> {
//								final Survey survey = new Survey();
//								survey.setRef(querySolution.getResource("ref"));
//								survey.setName(querySolution.getLiteral("name").getString());
//
//								Optional
//									.ofNullable(querySolution.getLiteral("code"))
//									.map(Literal::getString)
//									.ifPresent(survey::setCode);
//
//								Optional
//									.ofNullable(querySolution.getLiteral("shortName"))
//									.map(Literal::getString)
//									.ifPresent(survey::setShortName);
//
//								Optional
//									.ofNullable(querySolution.getLiteral("fileName"))
//									.map(Literal::getString)
//									.ifPresent(survey::setFileName);
//
//								return survey;
//							});
//		}
//
//	}
//
//
//	public QuestionPayLoad createQuestion(final Survey survey, final Question question) {
//		final Model model = ModelFactory.createDefaultModel();
//
//
//		final Resource questionResource =
//				model
//					.createResource(question.getUri())
//						.addProperty(RDF.type, question.getTypeRef())
//						.addProperty(RDFS.label, question.getName());
//
//		RdfRepositoryUtils
//				.mapObjectPropertyToResource(
//						question,
//						questionResource,
//						Metadata.hasCode, Question::getCode);
//
//		// surveyResource
//		model
//			.createResource(survey.getUri())
//			.addProperty(Metadata.hasQuestion, questionResource);
//
//
//		try (final ModelConnection modelConnection = getMetadataGraph()) {
//			modelConnection.pushStatements(model);
//		}
//
//		return new QuestionPayLoad(question);
//	}
//
//
//}
