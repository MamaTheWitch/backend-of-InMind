package com.junction2022.models.payload;

import java.util.Optional;
import java.util.function.Function;

import org.apache.jena.rdf.model.Resource;

import com.junction2022.models.FreshAirEntity;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Nam Vu
 *
 */
@Getter
@Setter
public abstract class FreshAirEntityPayLoad<T extends FreshAirEntity> {

	// generic type is incompatible with graphql-java-kickstart:
	// https://github.com/graphql-java-kickstart/graphql-spring-boot/issues/306

	private final Resource recordRef;
	private final Function<Resource, Optional<T>> recordSupplier;

	protected FreshAirEntityPayLoad(final Resource recordUri, final Function<Resource, Optional<T>> recordSupplier) {
		this.recordRef = recordUri;
		this.recordSupplier = recordSupplier;
	}

	protected FreshAirEntityPayLoad(final T entity) {
		this(entity.getRef(), ref -> Optional.of(entity));
	}

	public String getRecordLocalId() {
		return recordRef.getLocalName();
	}

	public Optional<T> getRecord() {
		return recordSupplier.apply(getRecordRef());
	}
}
