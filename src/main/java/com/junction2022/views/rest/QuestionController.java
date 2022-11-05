//package com.junction2022.views.rest;
//
//
//import org.springframework.web.bind.annotation.RestController;
//
//import com.junction2022.common.exceptions.ResourceNotFoundException;
//import com.junction2022.models.Question;
//import com.junction2022.repositories.file.MetadataFileRepository;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//
///**
// *
// * @author Nam Vu
// *
// */
//@RestController
//@RequestMapping("/api/question")
//public class QuestionController {
//
//	@Autowired
//	private MetadataFileRepository repository;
//
//	@GetMapping()
//	public List<Map<String, String>> getUuids() throws IOException {
//		return
//			repository
//				.getSurveySet()
//				.getFlatQuestionMap()
//				.values()
//				.stream()
//				.map(Question::asShortMap)
//				.toList();
//	}
//
//	@GetMapping("{questionUuid}")
//	public Question getByUuid(@PathVariable final String questionUuid) throws IOException, ResourceNotFoundException {
//		return repository
//				.getSurveySet()
//				.getQuestion(UUID.fromString(questionUuid))
//				.orElseThrow(() -> new ResourceNotFoundException("Question card not found: " + questionUuid));
//	}
//
//
//
//
//}