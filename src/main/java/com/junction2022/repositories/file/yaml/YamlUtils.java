package com.junction2022.repositories.file.yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class YamlUtils {

	public static final String FILE_EXTENSION = "yaml";

	public static <T> T readYaml(final InputStream resourceInputStream, final Class<T> clazz) throws IOException {
		Objects.nonNull(resourceInputStream);
		final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
		final T value = objectMapper.readValue(resourceInputStream, clazz);
		log.info("Reading YAML file: {} completed", resourceInputStream);
		return value;
	}

	private YamlUtils() {}

}
