package id.findcat.app.base;

/**
 * Created by rioswarawan on 5/18/17.
 */

public interface BaseListener {

    void onError(String message);

    void showLoading();

    void hideLoading();
}
