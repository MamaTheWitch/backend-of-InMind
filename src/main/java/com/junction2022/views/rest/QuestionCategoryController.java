package com.junction2022.views.rest;


import org.springframework.web.bind.annotation.RestController;

import com.junction2022.common.exceptions.ResourceNotFoundException;
import com.junction2022.models.Question;
import com.junction2022.models.QuestionCategory;
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
@RequestMapping("/api/category")
public class QuestionCategoryController {


	@Autowired
	MetadataFileRepository repository;

	@GetMapping()
	public List<Map<String, String>> getAllUuids() {
		return repository
					.getQuestionCategorySet()
					.getQuestionCategories()
					.stream()
					.sorted(QuestionCategory.COMPARATOR_BY_CODE)
					.map(QuestionCategory::asShortMap)
					.toList();
	}

	@GetMapping("full")
	public List<QuestionCategory> getAll() {
		return repository
					.getQuestionCategorySet()
					.getQuestionCategories();
	}

	@GetMapping("{categoryUuid}")
	public QuestionCategory getByUuid(@PathVariable final String categoryUuid) throws ResourceNotFoundException {
		return repository
					.getQuestionCategorySet()
					.getQuestionCategory(UUID.fromString(categoryUuid))
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