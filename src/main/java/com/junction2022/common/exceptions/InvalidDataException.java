package com.junction2022.common.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "invalid data")
public class InvalidDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidDataException() {
	}

	public InvalidDataException(final String message) {
		super(message);
	}

	public InvalidDataException(final Throwable cause) {
		super(cause);
	}

	public InvalidDataException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public InvalidDataException(final String message, final Throwable cause,
			final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
