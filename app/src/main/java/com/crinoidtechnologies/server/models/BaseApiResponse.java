package com.crinoidtechnologies.server.models;

public class BaseApiResponse<T> {

    protected BaseApiError error;
    protected T responseData;

    public BaseApiError getError() {
        return error;
    }

    public void setError(BaseApiError error) {
        this.error = error;
    }

    public T getResponseData() {
        return responseData;
    }

    public void setResponseData(T responseData) {
        this.responseData = responseData;
    }

}
