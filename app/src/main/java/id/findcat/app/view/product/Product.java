package id.findcat.app.view.product;

/**
 * Created by rioswarawan on 5/14/17.
 */

public class Product {

    public int id;
    public int score;
    public String name;
    public String description;
    public String image;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", score=" + score +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
