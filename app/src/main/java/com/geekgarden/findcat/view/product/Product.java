package com.geekgarden.findcat.view.product;

/**
 * Created by rioswarawan on 5/14/17.
 */

public class Product {

    public String videoUrl;
    public int sample;
    public String name;
    public String description;

    public Product(String name, String description, int sample) {
        this.name = name;
        this.description = description;
        this.sample = sample;
    }
}
