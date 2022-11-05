package com.junction2022.views.graphql.scalars;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Data;

/**
 *
 * @author Nam Vu
 *
 */
@Data
public class UriScalar {

	private URI uri;

	@JsonCreator
	public UriScalar(final String uri) throws IllegalArgumentException {
		if (uri != null) {
			try {
				this.uri = new URI(uri);
			} catch (final URISyntaxException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	public UriScalar(final URI uri) {
		this.uri = uri;
	}

	public Resource toResource() {
		return ResourceFactory.createResource(uri.toString());
	}

	@Override
	public String toString() {
		return uri.toString();
	}

}
