//package com.junction2022.repositories.rdf;
//
//import java.io.IOException;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.UUID;
//
//import org.apache.jena.query.QueryExecution;
//import org.apache.jena.query.QuerySolution;
//import org.apache.jena.query.ResultSet;
//import org.apache.jena.rdf.model.Model;
//import org.apache.jena.rdf.model.ModelFactory;
//import org.apache.jena.rdf.model.Resource;
//import org.apache.jena.vocabulary.RDF;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Service;
//
//import com.junction2022.FreshAirApplication;
//import com.junction2022.common.exceptions.ResourceNotFoundException;
//import com.junction2022.models.Question;
//import com.junction2022.models.QuestionAnswer;
//import com.junction2022.models.SurveyResult;
//import com.junction2022.models.MentalService;
//import com.junction2022.models.inputs.QuestionAnswerInput;
//import com.junction2022.repositories.file.MetadataFileRepository;
//import com.junction2022.repositories.rdf.FreshAirOntology.QuestionRecords;
//
//import fi.vtt.bimlinker.rdf.jena.provider.ModelConnection;
//import fi.vtt.bimlinker.rdf.utils.RdfUtils;
//
//@MentalService
//public class QuestionRecordRdfRepository extends BaseRdfRepository {
//
//	public static QuestionRecordRdfRepository getInstance() {
//		return FreshAirApplication.getBean(QuestionRecordRdfRepository.class);
//	}
//
//	@Autowired
//	private MetadataFileRepository metadataFileRepository;
//
//	private ModelConnection getQuestionRecordsGraph() {
//		return application.openJenaModel(QuestionRecords.NAMESPACE_URI);
//	}
//
//	@PreAuthorize("hasRole('MANAGER')")
//	public SurveyResult addQuestionRecords(
//			final MentalService service,
//			final Question question,
//			final List<QuestionAnswerInput> questionRecordInputs) {
//		final Model model = ModelFactory.createDefaultModel();
//
//		final Resource serviceResource = service.getRef();
//		final Resource questionResource = question.getRef();
//		final SurveyResult questionRecordGroup = new SurveyResult(service, question);
//		questionRecordGroup.setRecords(questionRecordInputs.stream().map(QuestionAnswerInput::toQuestionRecord).toList());
//
//		final Resource serviceQuestionResource =
//				questionRecordGroup
//					.getRef()
//					.inModel(model)
//					.addProperty(RDF.type, QuestionRecords.QuestionRecordGroup)
//					.addProperty(QuestionRecords.belongsToService, serviceResource)
//					.addProperty(QuestionRecords.belongsToQuestion, questionResource);
//
//
//		for (final QuestionAnswerInput questionRecordInput : questionRecordInputs) {
//
//			final Instant timestamp = questionRecordInput.getTimestamp();
//
//			final Resource questionRecordResource =
//				model
//					.createResource(questionRecordGroup.getUri() + "/T" + timestamp.toEpochMilli())
//						.addProperty(RDF.type, QuestionRecords.QuestionRecord)
//						.addLiteral(QuestionRecords.hasTimestamp, RdfUtils.createTypedLiteral(timestamp))
//						.addLiteral(QuestionRecords.hasValue, questionRecordInput.getValue());
//
//			serviceQuestionResource
//				.addProperty(QuestionRecords.hasRecord, questionRecordResource);
//
//		}
//
//		try (final ModelConnection modelConnection = getQuestionRecordsGraph()) {
//			modelConnection.pushStatements(model);
//		}
//
//		return questionRecordGroup;
//	}
//
//	public List<SurveyResult> getQuestionRecordGroups(final MentalService service) throws IOException {
//
//		final var sparql =
//				nameFormatter
//					.getJenaQueryBuilder()
//					.buildParameterizedSparql(
//							"""
//							SELECT DISTINCT ?question
//							FROM kr:
//							WHERE {
//								?questionRecordGroup
//									a ?QuestionRecordGroup ;
//									?boundToService ?service ;
//									?boundToQuestion ?question
//							}
//							ORDER BY ?question
//
//							""",
//							null,
//							Map.of(
//								"service", service.getRef(),
//								"SurveyResult", QuestionRecords.QuestionRecordGroup,
//								"boundToService", QuestionRecords.belongsToService,
//								"boundToQuestion", QuestionRecords.belongsToQuestion)
//					);
//
//		try (final ModelConnection modelConnection = getQuestionRecordsGraph();
//			final QueryExecution queryExecution = modelConnection.createQueryExecution(sparql.asQuery())) {
//			return RdfRepositoryUtils.mapResultSetToList(
//					queryExecution.execSelect(),
//					querySolution -> {
//						final Resource questionResource = querySolution.getResource("question");
//						final UUID questionUuid = nameFormatter.extractEntityUuid(questionResource.getURI());
//						final Question question =
//								metadataFileRepository
//									.getSurveySet()
//									.getQuestion(questionUuid)
//									.orElse(null);
//						if (question == null) {
//							throw new ResourceNotFoundException("Unknown Question card: " + questionUuid);
//						}
//						return new SurveyResult(service, question);
//					});
//		}
//
//	}
//
//	public List<QuestionAnswer> getQuestionRecords(
//			final SurveyResult questionRecordGroup,
//			final Optional<Instant> start,
//			final Optional<Instant> end,
//			final Optional<Integer> limit) throws IOException {
//		final List<SurveyResult> questionRecordGroups =
//				getQuestionRecords(
//						Optional.of(questionRecordGroup.getService()),
//						Optional.of(questionRecordGroup.getQuestion()),
//						start,
//						end,
//						limit);
//		return questionRecordGroups
//				.stream()
//				.findFirst()
//				.map(SurveyResult::getRecords)
//				.orElse(Arrays.asList());
//	}
//
//
//	public List<SurveyResult> getQuestionRecords(
//			final Optional<MentalService> service,
//			final Optional<Question> question,
//			final Optional<Instant> start,
//			final Optional<Instant> end,
//			final Optional<Integer> limit) throws IOException {
//
//		final Map<String, Object> params = new HashMap<>();
//
//		final Resource serviceResource = service.map(MentalService::getRef).orElse(null);
//		String serviceRestriction = "";
//		if (serviceResource != null) {
//			serviceRestriction = "VALUES ?service { ?service1 }";
//			params.put("service1", serviceResource);
//		}
//
//		final Resource questionResource = question.map(Question::getRef).orElse(null);
//		String questionRestriction = "";
//		if (questionResource != null) {
//			questionRestriction = "VALUES ?question { ?question1 }";
//			params.put("question1", questionResource);
//		}
//
//		String startRestriction = "";
//		if (start.isPresent()) {
//			startRestriction = "FILTER(?start1 <= ?timestamp)";
//			params.put("start1", RdfUtils.createTypedLiteral(start.get()));
//		}
//
//
//		String endRestriction = "";
//		if (end.isPresent()) {
//			endRestriction = "FILTER(?timestamp <= ?end1)";
//			params.put("end1", RdfUtils.createTypedLiteral(end.get()));
//		}
//
//		final String limitRestriction = limit.map(l -> "LIMIT " + l).orElse("");
//
//		params.put("QuestionAnswer", QuestionRecords.QuestionRecord);
//		params.put("SurveyResult", QuestionRecords.QuestionRecordGroup);
//		params.put("boundToService", QuestionRecords.belongsToService);
//		params.put("boundToQuestion", QuestionRecords.belongsToQuestion);
//		params.put("hasRecord", QuestionRecords.hasRecord);
//		params.put("hasTimestamp", QuestionRecords.hasTimestamp);
//		params.put("hasValue", QuestionRecords.hasValue);
//
//
//		final var sparql =
//				nameFormatter
//					.getJenaQueryBuilder()
//					.buildParameterizedSparql(
//							String.format(
//								"""
//								SELECT ?service ?question ?timestamp ?value
//								FROM kr:
//								WHERE {
//									%1$s 	# serviceRestriction
//									%2$s 	# questionRestriction
//
//									?questionRecordGroup
//										a ?QuestionRecordGroup ;
//										?boundToService ?service ;
//										?boundToQuestion ?question ;
//										?hasRecord ?questionRecord .
//
//									?questionRecord
//										a ?QuestionRecord ;
//										kr:hasTimestamp ?timestamp ;
//										kr:hasValue ?value .
//
//									%3$s 	# startRestriction
//									%4$s	# endRestriction
//								}
//								ORDER BY ?service ?question ?timestamp
//								%5$s
//
//								""",
//								serviceRestriction,
//								questionRestriction,
//								startRestriction,
//								endRestriction,
//								limitRestriction),
//							null,
//							params
//					);
//
//		try (final ModelConnection modelConnection = getQuestionRecordsGraph();
//			final QueryExecution queryExecution = modelConnection.createQueryExecution(sparql.asQuery())) {
//			final ResultSet resultSet = queryExecution.execSelect();
//
//			final List<SurveyResult> questionRecordGroups = new ArrayList<>();
//			SurveyResult currentQuestionRecordGroup = null;
//
//			while (resultSet.hasNext()) {
//				final QuerySolution querySolution = resultSet.next();
//
//				final Resource serviceResource1 = querySolution.getResource("service");
//				final UUID serviceUuid1 = nameFormatter.extractEntityUuid(serviceResource1.getURI());
//				final MentalService service1 =
//						metadataFileRepository
//							.getServiceSet()
//							.getService(serviceUuid1)
//							.orElse(null);
//				if (service1 == null) {
//					throw new ResourceNotFoundException("Unknown service: " + serviceUuid1);
//				}
//
//				final Resource questionResource1 = querySolution.getResource("question");
//				final UUID questionUuid1 = nameFormatter.extractEntityUuid(questionResource1.getURI());
//				final Question question1 =
//						metadataFileRepository
//							.getSurveySet()
//							.getQuestion(questionUuid1)
//							.orElse(null);
//				if (question1 == null) {
//					throw new ResourceNotFoundException("Unknown Question card: " + serviceUuid1);
//				}
//
//				if (currentQuestionRecordGroup == null
//						|| !currentQuestionRecordGroup.getService().equals(service1)
//						|| !currentQuestionRecordGroup.getQuestion().equals(question1))
//				{
//					currentQuestionRecordGroup = new SurveyResult(service1, question1);
//					currentQuestionRecordGroup.setRecords(new ArrayList<>());
//					questionRecordGroups.add(currentQuestionRecordGroup);
//				}
//
//
//				final Instant timestamp = RdfUtils.getInstantValue(querySolution.getLiteral("timestamp"));
//				final double value = querySolution.getLiteral("value").getDouble();
//				final QuestionAnswer questionRecord = new QuestionAnswer(timestamp, value);
//				currentQuestionRecordGroup.getRecords().add(questionRecord);
//			}
//
//			return questionRecordGroups;
//		}
//
//	}
//
//	public int getQuestionRecordGroupSize(final SurveyResult questionRecordGroup) {
//
//		final var sparql =
//				nameFormatter
//				.getJenaQueryBuilder()
//				.buildParameterizedSparql(
//						"""
//						SELECT (COUNT(DISTINCT ?questionRecord)) AS ?count
//						FROM kr:
//						WHERE {
//							VALUES ?service { ?service1 }
//							VALUES ?question { ?question1 }
//
//							?questionRecordGroup
//								a ?QuestionRecordGroup ;
//								?boundToService ?service ;
//								?boundToQuestion ?question ;
//								?hasRecord ?questionRecord .
//
//							?questionRecord
//								a ?QuestionRecord .
//						}
//
//						""",
//						null,
//						Map.of(
//								"QuestionAnswer", QuestionRecords.QuestionRecord,
//								"SurveyResult", QuestionRecords.QuestionRecordGroup,
//								"boundToService", QuestionRecords.belongsToService,
//								"boundToQuestion", QuestionRecords.belongsToQuestion,
//								"hasRecord", QuestionRecords.hasRecord,
//								"service1", questionRecordGroup.getService().getRef(),
//								"question1", questionRecordGroup.getQuestion().getRef())
//						);
//
//		try (final ModelConnection modelConnection = getQuestionRecordsGraph();
//				final QueryExecution queryExecution = modelConnection.createQueryExecution(sparql.asQuery())) {
//			final ResultSet resultSet = queryExecution.execSelect();
//			if (resultSet.hasNext()) {
//				final QuerySolution querySolution = resultSet.next();
//				return querySolution.getLiteral("count").getInt();
//			}
//
//			return 0;
//		}
//
//	}
//
//}
