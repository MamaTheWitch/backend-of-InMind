package com.junction2022.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "not found")
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {
	}

	public ResourceNotFoundException(final String message) {
		super(message);
	}

	public ResourceNotFoundException(final Throwable cause) {
		super(cause);
	}

	public ResourceNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
