package com.junction2022.views.graphql.resolvers.query;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junction2022.models.Survey;
import com.junction2022.repositories.file.MetadataFileRepository;
import com.junction2022.views.graphql.scalars.UuidScalar;

import graphql.kickstart.tools.GraphQLQueryResolver;

@Service
public class MainQueryResolver implements GraphQLQueryResolver {

	@Autowired
	private MetadataFileRepository metadataFileRepository;

	public List<Survey> getSurveys() {
		final List<Survey> categories =
				metadataFileRepository
					.getSurveySet()
					.getSurveys();
		categories.sort(Survey.COMPARATOR_BY_CODE);
		return categories;

	}

	public Optional<Survey> getSurvey(final UuidScalar uuid) {
		return
				metadataFileRepository
					.getSurveySet()
					.getSurveys()
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
