//package com.junction2022.views.graphql.resolvers.mutation;
//
//import java.io.IOException;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.junction2022.models.MentalService;
//import com.junction2022.repositories.file.MetadataFileRepository;
//import com.junction2022.views.graphql.scalars.UuidScalar;
//
//import graphql.kickstart.tools.GraphQLMutationResolver;
//import lombok.Getter;
//
//@MentalService
//public class MainMutationResolver implements GraphQLMutationResolver {
//
//	@Autowired
//	private MetadataFileRepository metadataFileRepository;
//
////	@Autowired
////	@Getter
////	private MetadataMutationResolver metadata;
//
//	public Optional<QuestionRecordGroupMutationResolver> getQuestionRecordGroup(
//			final UuidScalar serviceUuid,
//			final UuidScalar questionUuid) throws IOException {
//
//		final MentalService service =
//				metadataFileRepository
//					.getServiceSet()
//					.getService(serviceUuid.getUuid())
//					.orElse(null);
//		if (service == null) {
//			return Optional.empty();
//		}
//
//		return metadataFileRepository
//					.getSurveySet()
//					.getQuestion(questionUuid.getUuid())
//					.map(question -> new QuestionRecordGroupMutationResolver(service, question));
//	}
//
//}
