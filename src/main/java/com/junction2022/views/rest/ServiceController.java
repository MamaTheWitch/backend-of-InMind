//package com.junction2022.views.rest;
//
//
//import org.springframework.web.bind.annotation.RestController;
//
//import com.junction2022.common.exceptions.ResourceNotFoundException;
//import com.junction2022.models.Question;
//import com.junction2022.models.QuestionAnswer;
//import com.junction2022.models.QuestionairResult;
//import com.junction2022.models.MentalService;
//import com.junction2022.repositories.file.MetadataFileRepository;
//import com.junction2022.repositories.rdf.QuestionRecordRdfRepository;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.UUID;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
///**
// *
// * @author Nam Vu
// *
// */
//@RestController
//@RequestMapping("/api/service")
//public class ServiceController {
//
//	@Autowired
//	MetadataFileRepository metadataFileRepository;
//
//	@Autowired
//	QuestionRecordRdfRepository questionRecordRdfRepository;
//
//	@GetMapping()
//	public List<Map<String, String>> getAllUuids() throws IOException {
//		return metadataFileRepository
//					.getServiceSet()
//					.getFlatServiceMap()
//					.values()
//					.stream()
//					.sorted()
//					.map(MentalService::asShortMap)
//					.toList();
//	}
//
//	@GetMapping("full")
//	public Collection<MentalService> getAll() throws IOException {
//		return metadataFileRepository
//					.getServiceSet()
//					.getFlatServiceMap()
//					.values();
//	}
//
//	@GetMapping("{serviceUuid}")
//	public MentalService getByUuid(@PathVariable final String serviceUuid) throws IOException, ResourceNotFoundException {
//		return metadataFileRepository
//					.getServiceSet()
//					.getService(UUID.fromString(serviceUuid))
//					.orElseThrow(() -> new ResourceNotFoundException("MentalService not found: " + serviceUuid));
//	}
//
//	@GetMapping("{serviceUuid}/question")
//	public List<Map<String, String>> getQuestionsByUuid(@PathVariable final String serviceUuid) throws IOException, ResourceNotFoundException {
//		final MentalService service =
//				metadataFileRepository
//					.getServiceSet()
//					.getService(UUID.fromString(serviceUuid))
//					.orElseThrow(() -> new ResourceNotFoundException("MentalService not found: " + serviceUuid));
//
//		// TODO: remove duplicated values
//		return
//				questionRecordRdfRepository
//					.getQuestionRecordGroups(service)
//					.stream()
//					.map(QuestionairResult::getQuestion)
//					.sorted()
//					.map(Question::asShortMap)
//					.toList();
//	}
//
//	@GetMapping("{serviceUuid}/question/{questionUuid}")
//	public List<QuestionAnswer> getQuestionRecords(
//			@PathVariable final String serviceUuid,
//			@PathVariable final String questionUuid) throws IOException, ResourceNotFoundException {
//		final MentalService service =
//				metadataFileRepository
//					.getServiceSet()
//					.getService(UUID.fromString(serviceUuid))
//					.orElseThrow(() -> new ResourceNotFoundException("MentalService not found: " + serviceUuid));
//
//		final Question question =
//				metadataFileRepository
//					.getQuestionCategorySet()
//					.getQuestion(UUID.fromString(questionUuid))
//					.orElseThrow(() -> new ResourceNotFoundException("Question card not found: " + questionUuid));
//
//		final QuestionairResult questionRecordGroup = new QuestionairResult(service, question);
//		return questionRecordRdfRepository.getQuestionRecords(questionRecordGroup, Optional.empty(), Optional.empty(), Optional.empty());
//	}
//
//
//	@PostMapping("{serviceUuid}/question/{questionUuid}")
//	public List<QuestionAnswer> addQuestionRecords(
//			@PathVariable final String serviceUuid,
//			@PathVariable final String questionUuid,
//			@RequestBody final List<QuestionAnswer> questionRecords) throws IOException, ResourceNotFoundException {
//		final MentalService service =
//				metadataFileRepository
//					.getServiceSet()
//					.getService(UUID.fromString(serviceUuid))
//					.orElseThrow(() -> new ResourceNotFoundException("MentalService not found: " + serviceUuid));
//
//		final Question question =
//				metadataFileRepository
//					.getQuestionCategorySet()
//					.getQuestion(UUID.fromString(questionUuid))
//					.orElseThrow(() -> new ResourceNotFoundException("Question card not found: " + questionUuid));
//
//		final QuestionairResult questionRecordGroup = new QuestionairResult(service, question);
//		return questionRecordRdfRepository.getQuestionRecords(questionRecordGroup, Optional.empty(), Optional.empty(), Optional.empty());
//	}
//
//
//
//}