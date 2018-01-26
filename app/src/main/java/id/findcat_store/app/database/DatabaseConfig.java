package id.findcat_store.app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import id.findcat_store.app.database.entity.ProductHistory;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Rio Swarawan on 12/7/2015.
 */
public class DatabaseConfig extends OrmLiteSqliteOpenHelper {

    private Context context;
    private Dao<ProductHistory, Integer> productHistoryDao = null;

    public DatabaseConfig(Context context) {
        super(context, "findcat_store", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        init();
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        dropUpdatedTable();
        init();
    }

    private void init() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, ProductHistory.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dropUpdatedTable() {
        try {
            TableUtils.dropTable(connectionSource, ProductHistory.class, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearTable() {
        try {
            TableUtils.clearTable(connectionSource, ProductHistory.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public <T> void clearTable(Class<T> dataClass) {
        try {
            TableUtils.clearTable(connectionSource, dataClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<ProductHistory, Integer> getProductHistoryDao() throws SQLException {
        if (productHistoryDao == null)
            productHistoryDao = getDao(ProductHistory.class);
        return productHistoryDao;
    }
}
