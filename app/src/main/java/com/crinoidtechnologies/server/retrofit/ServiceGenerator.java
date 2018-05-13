package com.crinoidtechnologies.server.retrofit;

import android.text.TextUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ${Vivek} on 4/13/2017 for _serverUtilsVolley.Be careful
 */

public class ServiceGenerator {

    public static String API_BASE_URL = "https://your.api-base.url";
    public static int READ_OUT_TIME = 50 * 1000;
    public static int WRITE_OUT_TIME = 50 * 1000;
    public static Retrofit retrofit;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder;

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, String username, String password) {
        if (!TextUtils.isEmpty(username)
                && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null);
    }

    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {

        httpClient.readTimeout(READ_OUT_TIME, TimeUnit.MILLISECONDS);
        httpClient.connectTimeout(WRITE_OUT_TIME, TimeUnit.MILLISECONDS);

        //        if (BuildConfig.DEBUG) {
//           HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClient.addInterceptor(logging);
//        }

        if (!TextUtils.isEmpty(authToken)) {
            // AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder request = chain.request().newBuilder();
                    if (!TextUtils.isEmpty(authToken)) {
                        request.addHeader("authorization", "Bearer " + authToken);
                    }
                    return chain.proceed(request.build());
                }
            });
        }
        builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        builder.client(httpClient.build());
        retrofit = builder.build();

        return retrofit.create(serviceClass);
    }
}