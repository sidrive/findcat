package com.geekgarden.findcat.api;


import com.geekgarden.findcat.BuildConfig;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by rioswarawan on 8/3/16.
 */
public interface HttpService {

    @Multipart
    @POST("api/search")
    Observable<Search.Response> search(
            @PartMap Map<String, RequestBody> body);

    @GET("api/product/{productId}")
    Observable<Product.Response> getProduct(
            @Path("productId") String productId,
            @Query("api_token") String apiToken);


    class Factory {
        public static HttpService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.FindCatHost)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client())
                    .build();
            return retrofit.create(HttpService.class);
        }

        private static OkHttpClient client() {
            return new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)
                    )
                    .build();
        }
    }
}
