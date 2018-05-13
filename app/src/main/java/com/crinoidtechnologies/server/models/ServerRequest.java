package com.crinoidtechnologies.server.models;

import java.util.HashMap;
import java.util.Map;

public class ServerRequest<T> {
    private int id;
    private String url;
    private Map<Object, Object> parameters = new HashMap<>(2);
    private ServerRequestCallback<T> callback;
    private ServerRequestCallback<T> secondaryCallback;
    private int retryCount = 1;
    private float retryInterval = 1;
    private Map<String, String> headers = new HashMap<>(2);

    public ServerRequest(int id, Map<Object, Object> parameters, ServerRequestCallback callback, int retryCount, float retryInterval) {
        this.id = id;
        if (parameters != null) {
            this.parameters = parameters;
        }
        this.callback = callback;
        this.retryCount = retryCount;
        this.retryInterval = retryInterval;
    }

    public Map<Object, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<Object, Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, String value) {
        if (this.headers == null)
            headers = new HashMap<>();

        headers.put(key, value);
    }

    public ServerRequestCallback getCallback() {
        return callback;
    }

    public void setCallback(ServerRequestCallback callback) {
        this.callback = callback;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public float getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(float retryInterval) {
        this.retryInterval = retryInterval;
    }

    public ServerRequestCallback getSecondaryCallback() {
        return secondaryCallback;
    }

    public void setSecondaryCallback(ServerRequestCallback secondaryCallback) {
        this.secondaryCallback = secondaryCallback;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

