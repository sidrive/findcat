import android.app.Activity;
import android.app.Application;
import android.content.Context;
import id.findcat.app.preference.GlobalPreferences;
import retrofit2.Retrofit;

/**
 * Created by rakasn on 24/01/18.
 */

public class FindcatDexApp extends Application {

  public static Context context;
  public static Activity activity;
  public static Retrofit retrofit;
  public static GlobalPreferences glpref;
  @Override
  public void onCreate() {
    super.onCreate();
    context = getApplicationContext();
    activity = new Activity();
    retrofit = null;
    glpref = new GlobalPreferences(this);
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
