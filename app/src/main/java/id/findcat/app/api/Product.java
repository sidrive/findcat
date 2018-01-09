package id.findcat.app.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rioswarawan on 5/18/17.
 */

public class Product {

    public class Response {

        public class Data {
            public int id;
            public String name;
            public String description;
            public Label label;
        }

        public class Label {
            public int id;
            public String name;
            public String description;
            public Pivot pivot;
        }

        public class Pivot {
            @SerializedName("product_id")
            public int productId;

            @SerializedName("label_id")
            public int labelid;
        }
    }
}
