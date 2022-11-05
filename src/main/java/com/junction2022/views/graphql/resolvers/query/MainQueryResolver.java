package com.junction2022.views.graphql.resolvers.query;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junction2022.models.QuestionCategory;
import com.junction2022.repositories.file.MetadataFileRepository;
import com.junction2022.views.graphql.scalars.UuidScalar;

import graphql.kickstart.tools.GraphQLQueryResolver;

@Service
public class MainQueryResolver implements GraphQLQueryResolver {

	@Autowired
	private MetadataFileRepository metadataFileRepository;

	public List<QuestionCategory> getQuestionCategories() {
		final List<QuestionCategory> categories =
				metadataFileRepository
					.getQuestionCategorySet()
					.getQuestionCategories();
		categories.sort(QuestionCategory.COMPARATOR_BY_CODE);
		return categories;

	}

	public Optional<QuestionCategory> getQuestionCategory(final UuidScalar uuid) {
		return
				metadataFileRepository
					.getQuestionCategorySet()
					.getQuestionCategories()
					.stream()
					.filter(c -> c.getUuid().equals(uuid.getUuid()))
					.findAny();
	}

//	public List<MentalService> getServices() {
//		return metadataFileRepository
//					.getServiceSet()
//					.getRootServices();
//	}
//
//	public Optional<MentalService> getService(final UuidScalar uuid) {
//		return metadataFileRepository
//					.getServiceSet()
//					.getService(uuid.getUuid());
//	}

}
