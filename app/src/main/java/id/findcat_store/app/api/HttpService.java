package id.findcat_store.app.api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import id.findcat_store.app.FindcatDexApp;
import id.findcat_store.app.preference.PrefKey;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by rioswarawan on 8/3/16.
 */
public interface HttpService {

    @Multipart
    @POST("/api/search")
    Observable<Search.Response> search(
            @Part MultipartBody.Part image,
            @Part("api_token") RequestBody apiToken);

    @GET("/api/product/{productId}")
    Observable<Product.Response> getProduct(
            @Path("productId") String productId,
            @Query("api_token") String apiToken);

    @GET("/api/product/{productId}/videos")
    Observable<Video.Response> getVideo(
            @Path("productId") String productId,
            @Query("api_token") String apiToken);



    class Factory {
        private static Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        public static HttpService create(){
            return createRetro(FindcatDexApp.getGlpref().read(PrefKey.base_url,String.class)).create(HttpService.class);
        }
        public static Retrofit createRetro(String base_url) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client())
                    .build();
            return retrofit;
        }

        private static OkHttpClient client() {
            return new OkHttpClient.Builder()
                    .readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .addInterceptor(new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)
                    ).addInterceptor(chain -> {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Accept", "application/json")
                                .header("Content-Type", "multipart/form-data")
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }).build();
        }
    }
}
