//package com.junction2022.views.graphql.resolvers.mutation;
//
//import java.util.List;
//
//import com.junction2022.FreshAirApplication;
//import com.junction2022.models.Question;
//import com.junction2022.models.QuestionairResult;
//import com.junction2022.models.MentalService;
//import com.junction2022.models.inputs.QuestionAnswerInput;
//import com.junction2022.repositories.rdf.QuestionRecordRdfRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//public class QuestionRecordGroupMutationResolver {
//
//	private final MentalService service;
//	private final Question question;
//
//
//	public QuestionairResult addQuestionRecords(final List<QuestionAnswerInput> questionRecords) {
//		final QuestionRecordRdfRepository questionRecordRdfRepository = FreshAirApplication.getBean(QuestionRecordRdfRepository.class);
//		return questionRecordRdfRepository.addQuestionRecords(service, question, questionRecords);
//	 }
//
//
//
//
//}
