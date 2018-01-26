package id.findcat_store.app.database.entity;

import android.content.Context;

import id.findcat_store.app.database.DatabaseConfig;
import id.findcat_store.app.utils.DateUtils;
import id.findcat_store.app.view.product.Product;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rioswarawan on 6/14/17.
 */

@DatabaseTable(tableName = "product_history")
public class ProductHistory {
    @DatabaseField(id = true)
    public String id;
    @DatabaseField
    public Integer productId;
    @DatabaseField
    public String name;
    @DatabaseField
    public String description;
    @DatabaseField
    public String image;
    @DatabaseField
    public String createdAt;
    @DatabaseField
    public int score;

    @Override
    public String toString() {
        return "ProductHistory{" +
                "id='" + id + '\'' +
                ", productId=" + productId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", score=" + score +
                '}';
    }

    public static class Controller {
        private Context context;
        private DatabaseConfig database;

        public Controller(Context context) {
            this.context = context;
            if (database == null) {
                this.database = new DatabaseConfig(context);
            }
        }

        public void insert(Product productHistory) {
            ProductHistory entity = new ProductHistory();
            entity.id = DateUtils.createSDF("yyyyMMddhhmmss").format(new Date());
            entity.productId = productHistory.id;
            entity.name = productHistory.name;
            entity.description = productHistory.description;
            entity.image = productHistory.image;
            entity.createdAt = DateUtils.getDateTimeStr(new Date().getTime(), "dd MMMM yyyy hh:mm:ss");

            insertUpdateDatabase(entity);
        }

        public ProductHistory getProductHistory(int id) {
            List<ProductHistory> data = new ArrayList<>();
            try {
                Dao<ProductHistory, Integer> dao = database.getProductHistoryDao();
                QueryBuilder<ProductHistory, Integer> query = dao.queryBuilder();
                query.where().eq("id", id);

                data = query.query();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return data.size() > 0 ? data.get(0) : null;
        }

        public List<ProductHistory> getProductHistories() {
            List<ProductHistory> data = new ArrayList<>();
            try {
                Dao<ProductHistory, Integer> dao = database.getProductHistoryDao();
                QueryBuilder<ProductHistory, Integer> query = dao.queryBuilder();

                data = query.query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return data;
        }

        private void insertUpdateDatabase(ProductHistory entity) {
            try {
                database.getProductHistoryDao().createOrUpdate(entity);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void clear() {
            database.clearTable();
        }
    }
}
