package com.junction2022.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ServiceType {
	@JsonProperty("Country")
	COUNTRY("Country"),

	@JsonProperty("City")
	CITY("City"),

	@JsonProperty("District")
	DISTRICT("District"),

	@JsonProperty("Building")
	BUILDING("Building"),

	@JsonProperty("BuildingBlock")
	BUILDING_BLOCK("Building Block");

	private final String typeName;

	@Override
	public String toString() {
		return typeName;
	}
}
