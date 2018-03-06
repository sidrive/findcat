package id.findcat_store.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;/*
import com.google.firebase.analytics.FirebaseAnalytics;*/
import id.findcat_store.app.preference.GlobalPreferences;
import retrofit2.Retrofit;

/**
 * Created by rakasn on 24/01/18.
 */

public class FindcatDexApp extends Application {
  /*private FirebaseAnalytics firebaseAnalytics;*/
  public static Context context;
  public static Activity activity;
  public static Retrofit retrofit;
  public static GlobalPreferences glpref;
  @Override
  public void onCreate() {
    super.onCreate();
    context = getApplicationContext();
    /*firebaseAnalytics = FirebaseAnalytics.getInstance(this);
    firebaseAnalytics.setAnalyticsCollectionEnabled(true);*/
    activity = new Activity();
    retrofit = null;
    glpref = new GlobalPreferences(context);
  }
  public static Context getContext(){
    return context;
  }
  public static Activity getActivity(){
    return activity;
  }
  public static GlobalPreferences getGlpref(){
    return glpref;
  }
}
