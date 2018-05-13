package com.crinoidtechnologies.server.utils;

import com.crinoidtechnologies.server.models.BaseApiError;
import com.crinoidtechnologies.server.retrofit.ServiceGenerator;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by ${Vivek} on 4/13/2017 for _serverUtilsVolley.Be careful
 */

public class ErrorUtils {

    public static BaseApiError parseError(Response<?> response) {
        Converter<ResponseBody, BaseApiError> converter =
                ServiceGenerator.retrofit.responseBodyConverter(BaseApiError.class, new Annotation[0]);

        BaseApiError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new BaseApiError();
        }

        return error;
    }
}
