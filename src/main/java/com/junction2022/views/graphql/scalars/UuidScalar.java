package com.junction2022.views.graphql.scalars;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Nam Vu
 *
 */
@Getter
@Setter
public class UuidScalar {

	public static UuidScalar getRandom() {
		return new UuidScalar(UUID.randomUUID());
	}

	private UUID uuid;

	// TODO: Remove default constructor. Currently, it is used to avoid Jackson mapping error.
	public UuidScalar() {
	}


	public UuidScalar(final UUID uuid) {
		this.uuid = uuid;
	}

	@JsonCreator
	public UuidScalar(final String uuid) throws IllegalArgumentException {
		this(UUID.fromString(uuid));
	}

	@Override
	public String toString() {
		return uuid.toString();
	}

}
