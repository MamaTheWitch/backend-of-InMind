package com.junction2022.models;

import java.util.UUID;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junction2022.repositories.rdf.NameFormatter;

import lombok.Getter;
import lombok.Setter;

/**
*
* @author Nam Vu
*
*/
@Getter
@Setter
public abstract class FreshAirEntity {

	private UUID uuid;

	@JsonIgnore
	public abstract Resource getTypeRef();

	@JsonIgnore
	public String getTypeName() {
		return getTypeRef().getLocalName();
	}

	@JsonIgnore
	public String getTypeNamespace() {
		return getTypeRef().getNameSpace();
	}

	@JsonIgnore
	public String getUri() {
		final String localId = String.format("%s_%s", getTypeName(), getUuid());
		return NameFormatter.getInstance().formatEntityUri(localId);
	}

	@JsonIgnore
	public Resource getRef() {
		return ResourceFactory.createResource(getUri());
	}

	public void setRef(final Resource ref) {
		final String localId = ref.getLocalName();
		uuid = UUID.fromString(localId.substring(localId.indexOf('_') + 1));
	}

	@JsonIgnore
	public String getLocalId() {
		return getRef().getLocalName();
	}

	@JsonIgnore
	public String getNamespace() {
		return getRef().getNameSpace();
	}

	@Override
	public String toString() {
		return getRef().toString();
	}

}
