package com.junction2022.views.rest;


import org.springframework.web.bind.annotation.RestController;

import com.junction2022.common.exceptions.ResourceNotFoundException;
import com.junction2022.models.Question;
import com.junction2022.models.Survey;
import com.junction2022.repositories.file.MetadataFileRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Nam Vu
 *
 */
@RestController
@RequestMapping("/api/survey")
public class SurveyController {


	@Autowired
	MetadataFileRepository repository;

	@GetMapping()
	public List<Map<String, String>> getAllUuids() {
		return repository
					.getSurveySet()
					.getSurveys()
					.stream()
					.sorted(Survey.COMPARATOR_BY_CODE)
					.map(Survey::asShortMap)
					.toList();
	}

	@GetMapping("full")
	public List<Survey> getAll() {
		return repository
					.getSurveySet()
					.getSurveys();
	}

	@GetMapping("{categoryUuid}")
	public Survey getByUuid(@PathVariable final String categoryUuid) throws ResourceNotFoundException {
		return repository
					.getSurveySet()
					.getSurvey(UUID.fromString(categoryUuid))
					.orElseThrow(() -> new ResourceNotFoundException("Question category not found: " + categoryUuid));
	}

//	@GetMapping("{categoryUuid}/question")
//	public List<Map<String, String>> getChildrenByUuid(@PathVariable final String categoryUuid) throws ResourceNotFoundException {
//		return
//			getByUuid(categoryUuid)
//				.getQuestions()
//				.stream()
////				.sorted(Question.COMPARATOR_BY_CODE)
////				.map(Question::asShortMap)
//				.toList();
//	}

//	@GetMapping("{categoryUuid}/question/full")
//	public List<Question> getChildrenByUuidFull(@PathVariable final String categoryUuid) throws ResourceNotFoundException {
//		final List<Question> questions = getByUuid(categoryUuid).getQuestions();
//		questions.sort(Question.COMPARATOR_BY_CODE);
//		return questions;
//	}



}