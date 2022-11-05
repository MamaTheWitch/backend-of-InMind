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
//import com.junction2022.models.QuestionCategory;
//import com.junction2022.models.payload.QuestionPayLoad;
//import com.junction2022.models.payload.QuestionCategoryPayLoad;
//import com.junction2022.repositories.rdf.FreshAirOntology.Metadata;
//
//@MentalService
//public class MetadataRdfRepository extends BaseRdfRepository {
//
//	private ModelConnection getMetadataGraph() {
//		return application.openJenaModel(Metadata.NAMESPACE_URI);
//	}
//
//	public QuestionCategoryPayLoad createQuestionCategory(final QuestionCategory questionCategory) {
//		final Model model = ModelFactory.createDefaultModel();
//
//		final Resource questionCategoryResource =
//				model
//					.createResource(questionCategory.getUri())
//						.addProperty(RDF.type, questionCategory.getTypeRef())
//						.addProperty(RDFS.label, questionCategory.getName());
//
//		RdfRepositoryUtils
//			.mapOptionalPropertiesToResource(
//				questionCategory, questionCategoryResource,
//				Map.of(
//						Metadata.hasCode, QuestionCategory::getCode,
//						Metadata.hasShortName, QuestionCategory::getShortName,
//						Metadata.storedInFile, QuestionCategory::getFileName
//				)
//			);
//
//		try (final ModelConnection modelConnection = getMetadataGraph()) {
//			modelConnection.pushStatements(model);
//		}
//
//		return new QuestionCategoryPayLoad(questionCategory);
//	}
//
//	public List<QuestionCategory> getQuestionCategories() {
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
//										a ?QuestionCategory ;
//										rdfs:label ?name .
//
//									OPTIONAL { ?ref ?hasCode ?code }
//									OPTIONAL { ?ref ?hasShortName ?shortName }
//									OPTIONAL { ?ref ?storedInFile ?fileName }
//							}
//							""",
//							null,
//							Map.of(
//								"QuestionCategory", Metadata.QuestionCategory,
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
//								final QuestionCategory questionCategory = new QuestionCategory();
//								questionCategory.setRef(querySolution.getResource("ref"));
//								questionCategory.setName(querySolution.getLiteral("name").getString());
//
//								Optional
//									.ofNullable(querySolution.getLiteral("code"))
//									.map(Literal::getString)
//									.ifPresent(questionCategory::setCode);
//
//								Optional
//									.ofNullable(querySolution.getLiteral("shortName"))
//									.map(Literal::getString)
//									.ifPresent(questionCategory::setShortName);
//
//								Optional
//									.ofNullable(querySolution.getLiteral("fileName"))
//									.map(Literal::getString)
//									.ifPresent(questionCategory::setFileName);
//
//								return questionCategory;
//							});
//		}
//
//	}
//
//
//	public QuestionPayLoad createQuestion(final QuestionCategory questionCategory, final Question question) {
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
//		// questionCategoryResource
//		model
//			.createResource(questionCategory.getUri())
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
