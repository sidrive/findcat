package id.findcat.app.preference;

import android.content.Context;
import id.findcat.app.view.splash.SplashActivity;

/**
 * Created by GeekGarden on 02/08/2017.
 */

public class GlobalPreferences {
  private static final String CACHE_NAME = GlobalPreferences.class.getName();
  private static CachePreferences cachePreferences;
  private Context mContext;
  public GlobalPreferences(Context context) {
    this.mContext = context;
  }

  private static CachePreferences getInstance(Context context) {
    if (cachePreferences == null)
      cachePreferences = new CachePreferences(context,CACHE_NAME);
    return cachePreferences;
  }

  public synchronized <T> T read(String key, Class<T> tClass) {
    return getInstance(this.mContext).read(key, tClass);
  }

  public synchronized <T> void write(String key, T value, Class<T> tClass) {
    getInstance(this.mContext).write(key, value, tClass);
  }

  public synchronized void clear() {
    getInstance(this.mContext).clear();
  }
}