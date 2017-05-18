package com.geekgarden.findcat.api;

import android.support.v4.util.ArrayMap;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by rioswarawan on 5/18/17.
 */

public class Search {

    public static class Request {
        public String apiToken;
        public File image;

        public Map<String, RequestBody> toMap() {
            Map<String, RequestBody> body = new ArrayMap<>();
            body.put("api_token", RequestBody.create(MediaType.parse("multipart/form-data"), apiToken));
            body.put("image", RequestBody.create(MediaType.parse("multipart/form-data"), image));
            return body;
        }
    }

    public static class Response {
        public Data data;

        public class Data {
            public Query query;
            public List<Result> results;
        }

        public class Query {
            @SerializedName("small_url")
            public String smallUrl;

            @SerializedName("medium_url")
            public String mediumUrl;

            @SerializedName("large_url")
            public String largeUrl;
        }

        public class Result {
            public int id;
            public String name;
            public String description;
            public String score;
        }
    }
}
