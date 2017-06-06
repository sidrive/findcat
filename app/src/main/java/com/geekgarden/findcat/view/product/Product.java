package com.geekgarden.findcat.view.product;

/**
 * Created by rioswarawan on 5/14/17.
 */

public class Product {

    public int id;
    public int score;
    public String name;
    public String description;

    public Product() {
    }

    public Product(int id, int score, String name, String description) {
        this.id = id;
        this.score = score;
        this.name = name;
        this.description = description;
    }
}