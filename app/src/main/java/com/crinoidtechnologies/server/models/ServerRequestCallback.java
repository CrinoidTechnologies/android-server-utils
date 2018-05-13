package com.crinoidtechnologies.server.models;

public interface ServerRequestCallback<T> {
    void onSuccess(ServerRequest request, T data);
    void onFailure(ServerRequest request, Error error);
}
