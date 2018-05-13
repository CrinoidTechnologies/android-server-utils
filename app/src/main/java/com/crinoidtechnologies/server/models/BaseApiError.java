package com.crinoidtechnologies.server.models;

public class BaseApiError {
    protected String statusCode;
    protected String message;

    public BaseApiError() {
    }

    public BaseApiError(ErrorType statusCode, String errorMsg) {
        this.message = errorMsg;
        this.statusCode = statusCode.toString();
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum ErrorType {
        ApiErrorNone,
        ApiErrorRequestedResourceDoesNotExists,
        ApiErrorErrorUpdatingTheDataOnServer,
        ApiErrorInsufficientData,
        ApiErrorWrongDataForThisApi,
        ApiErrorServerError
    }

}
