package com.junction2022.views.rest;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.junction2022.common.exceptions.ResourceNotFoundException;

import lombok.extern.log4j.Log4j2;

/**
 *
 * @author Nam Vu
 *
 */
@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Object> handleResponseStatusException(
			final ResponseStatusException exception,
			final WebRequest request,
			final HttpServletResponse response) throws IOException {
		return internalHandleException(exception.getStatus(), exception.getMessage(), exception, request, response);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<Object> handleIOException(
			final Exception exception,
			final WebRequest request,
			final HttpServletResponse response) throws IOException {
		return internalHandleException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception, request, response);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Object> handleNoSuchElementException(
			final Exception exception,
			final WebRequest request,
			final HttpServletResponse response) throws IOException {
		return internalHandleException(HttpStatus.NOT_FOUND, exception.getMessage(), exception, request, response);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(
			final Exception exception,
			final WebRequest request,
			final HttpServletResponse response) throws IOException {
		return internalHandleException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception, request, response);
	}

	private static ResponseEntity<Object> internalHandleException(
			final HttpStatus status,
			final String message,
			final Exception exception,
			final WebRequest request,
			final HttpServletResponse response) throws IOException {

		final String formattedMessage = String.format("[%s] %s", exception.getClass().getSimpleName(), message);

		if (status.is5xxServerError()) {
			log.error("Server error: " + formattedMessage, exception);
		} else {
			log.info("Client error: " + formattedMessage);
		}

		// https://mkyong.com/spring-boot/spring-rest-error-handling-example/
		// response.sendError(status.value(), formattedMessage);

		final Map<String, Object> body = new TreeMap<>();
        body.put("timestamp", new Date().toInstant());
        body.put("status", status.value());
        body.put("error", exception.getClass().getSimpleName());
        body.put("message", message);
        body.put("path", request.getContextPath());
        return new ResponseEntity<>(body, status);
	}

}