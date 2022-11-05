package com.junction2022.views.graphql.scalars;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Supplier;

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
public class UriOrIdScalar {

	private String uriOrId;

	@JsonCreator
	public UriOrIdScalar(final String uriOrId) {
		this.uriOrId = uriOrId;
	}

	public UriOrIdScalar(final URI uri) {
		this(uri.toString());
	}


	public URI toUri(final Supplier<String> defaultNamespaceSupplier) {
		try {
			return new URI(uriOrId);
		} catch (final URISyntaxException e) {
			return URI.create(defaultNamespaceSupplier.get() + uriOrId);
		}
	}

	public URI toUri(final String defaultNamespace) {
		return toUri(() -> defaultNamespace);
	}

	public Resource toResource(final Supplier<String> defaultNamespaceSupplier) {
		final URI uri = toUri(defaultNamespaceSupplier);
		return ResourceFactory.createResource(uri.toString());
	}

	public Resource toResource(final String defaultNamespace) {
		return toResource(() -> defaultNamespace);
	}

	@Override
	public String toString() {
		return uriOrId;
	}

}
