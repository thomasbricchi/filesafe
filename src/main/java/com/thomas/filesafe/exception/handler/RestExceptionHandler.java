package com.thomas.filesafe.exception.handler;

import com.thomas.filesafe.exception.ApiError;
import com.thomas.filesafe.exception.GetFileContentException;
import com.thomas.filesafe.exception.UploadFileException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;

    @ExceptionHandler(GetFileContentException.class)
    protected ResponseEntity<Object> handleUploadFileExe(GetFileContentException ex) {
        return getObjectResponseEntity(BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UploadFileException.class)
    protected ResponseEntity<Object> handleUploadFileExe(UploadFileException ex) {
        return getObjectResponseEntity(BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private ResponseEntity<Object> getObjectResponseEntity(HttpStatus badRequest, String message) {
        ApiError apiError = new ApiError(badRequest);
        apiError.setMessage(message);
        return buildResponseEntity(apiError);
    }


}
