package com.junction2022.repositories.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import esdl.EnergySystem;
import lombok.extern.log4j.Log4j2;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.junction2022.common.exceptions.InvalidDataException;
import com.junction2022.models.Question;
import com.junction2022.models.QuestionCategory;
import com.junction2022.models.QuestionCategorySet;
import com.junction2022.models.MentalService;
import com.junction2022.models.MentalServiceSet;
import com.junction2022.repositories.file.esdl.EsdlUtils;
import com.junction2022.repositories.file.yaml.YamlUtils;

@Service
@Log4j2
public class MetadataFileRepository {

	private static final String METADATA_FOLDER_PATH = "static/metadata/";
	private static final String CATEGORIES_FILE_NAME = "categories.yaml";
	private static final String[] LOCATIONS_FILE_NAMES =
		{
			"Portugal-normalized.esdl",
			"services.yaml",
		};

	private QuestionCategorySet questionCategorySet;
	private MentalServiceSet serviceSet;

	private static InputStream getResourceInputStream(final String path) throws IOException {
		log.info("Reading YAML file: {}", path);
		return new ClassPathResource(METADATA_FOLDER_PATH + path).getInputStream();
	}


	/////////////////////////////////
	// Question categories
	/////////////////////////////////

	public synchronized QuestionCategorySet getQuestionCategorySet() throws InvalidDataException {
		if (questionCategorySet == null) {
			try {
				questionCategorySet = readStaticQuestionCategorySet();
			} catch (final IOException e) {
				throw new InvalidDataException("Error reading static Question categories: " + e.getMessage(), e);
			}
		}
		return questionCategorySet;
	}


	private QuestionCategorySet readStaticQuestionCategorySet() throws IOException {
		try (final var categoriesInputStream = getResourceInputStream(CATEGORIES_FILE_NAME)) {
			final QuestionCategory[] categories = YamlUtils.readYaml(categoriesInputStream, QuestionCategory[].class);
			if (categories != null) {
				for (final QuestionCategory category : categories) {
					if (category.getFileName() != null) {						
						try (final var categoryInputStream = getResourceInputStream(category.getFileName())) {
							final Question[] cards = YamlUtils.readYaml(categoryInputStream, Question[].class);
							category.setQuestions(cards != null ? Arrays.asList(cards) : Arrays.asList());
						}
					}
				}
				return new QuestionCategorySet(Arrays.asList(categories));
			}
			return new QuestionCategorySet(Arrays.asList());
		}
	}

	/////////////////////////////////
	// Services
	/////////////////////////////////

	public synchronized MentalServiceSet getServiceSet() throws InvalidDataException {
		if (serviceSet == null) {
			try {
				for (final String filePath : LOCATIONS_FILE_NAMES) {
					final MentalServiceSet serviceSet1 = readStaticService(filePath);
					if (serviceSet == null) {
						serviceSet = serviceSet1;
					} else {
						serviceSet.addAll(serviceSet1);
					}
				}
			} catch (final IOException e) {
				throw new InvalidDataException("Error reading static services: " + e.getMessage(), e);
			}
		}
		return serviceSet;
	}

	private MentalServiceSet readStaticService(final String filePath) throws IOException {
		try (final var resourceInputStream = getResourceInputStream(filePath)) {
			final String extension = FilenameUtils.getExtension(filePath);

			switch (extension) {

			case EsdlUtils.FILE_EXTENSION:
				final EnergySystem energySystem = EsdlUtils.loadEsdlModel(resourceInputStream);
				return EsdlUtils.extractServices(energySystem, true, Optional.empty());

			case YamlUtils.FILE_EXTENSION:
				final MentalService[] services = YamlUtils.readYaml(resourceInputStream, MentalService[].class);
				return new MentalServiceSet(services != null ? Arrays.asList(services) : Arrays.asList());

			default:
				throw new IllegalArgumentException("Unsupported metadata file format: " + filePath);
			}
		}
	}


}
