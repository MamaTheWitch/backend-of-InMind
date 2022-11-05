package com.junction2022.common.utils;

import java.util.Optional;
import java.util.UUID;

import com.neovisionaries.i18n.CountryCode;

public class CountryUtils {

	public static Optional<CountryCode> findByName(final String countryName) {
		return
				CountryCode
					.findByName(String.format("^%s$", countryName))
					.stream()
					.findFirst();
	}

	public static Optional<CountryCode> findByCode(final String countryCode) {
		return Optional.ofNullable(CountryCode.getByCode(countryCode));
	}

	public static Optional<CountryCode> findByNameOrCode(final String countryNameOrCode) {
		return findByName(countryNameOrCode).or(() -> findByCode(countryNameOrCode));
	}

	public static UUID convertToUuid(final CountryCode countryCode) {
		return UUID.nameUUIDFromBytes(countryCode.getAlpha2().getBytes());
	}


	private CountryUtils() {
	}

}
