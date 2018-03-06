package id.findcat_store.app.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rioswarawan on 6/14/17.
 */

public class Video {

    public class Response {
        public List<Data> data;
    }

    public class Data {
        public int id;
        public String title;
        public String description;
        @SerializedName("embed_url") public String embedUrl;
        @SerializedName("uploaded_at")public String uploadedAt;

        //public List<String> thumbnails;
        public  String thumbnails;
        @SerializedName("company_rating") public int companyRating;
    }
}
