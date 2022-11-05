package com.junction2022.repositories.rdf;

import java.util.UUID;

import org.apache.jena.shared.PrefixMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.junction2022.FreshAirApplication;

import fi.vtt.bimlinker.rdf.jena.provider.JenaQueryBuilder;
import fi.vtt.utils.http.UriUtils;
import lombok.Getter;

@Service
@Getter
public class NameFormatter {


	private static NameFormatter instance$;

	public static NameFormatter getInstance() {
		return instance$;
	}

	public static final String OBJECTS_PATH = "obj/";

	private final String entityBaseUri;
	private final JenaQueryBuilder jenaQueryBuilder;
	private final PrefixMapping defaultPrefixMapping;


	public NameFormatter(@Autowired final FreshAirApplication application) {
		entityBaseUri = application.getVirtualBaseUri() + OBJECTS_PATH;

		defaultPrefixMapping =
				PrefixMapping.Factory.create()
					.setNsPrefixes(PrefixMapping.Standard)
					.setNsPrefix(FreshAirOntology.Metadata.NAMESPACE_PREFIX, FreshAirOntology.Metadata.NAMESPACE_URI)
					.setNsPrefix(FreshAirOntology.QuestionRecords.NAMESPACE_PREFIX, FreshAirOntology.QuestionRecords.NAMESPACE_URI)
					.lock();

		jenaQueryBuilder = new JenaQueryBuilder(FreshAirOntology.BASE_URI, defaultPrefixMapping);

		instance$ = this;
	}

	public String formatEntityUri(final String localName) {
		return getEntityBaseUri() + localName;
	}

	public String formatDoubleEntityUri(final String entity1LocalName, final String entity2LocalName) {
		return getEntityBaseUri() + entity1LocalName + "/" + entity2LocalName;
	}

	public UUID extractEntityUuid(final String uri) {
		final String localName = UriUtils.getLocalName(uri);
		final int index = localName.lastIndexOf('_');
		if (index == -1) {
			throw new IllegalArgumentException("No UUID found in URI: " + uri);
		}

		return UUID.fromString(localName.substring(index + 1));
	}

}
