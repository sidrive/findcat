package id.findcat.app.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import com.google.gson.Gson;

/**
 * Created by GeekGarden on 02/08/2017.
 */

public class CachePreferences {
  private SharedPreferences sPreferences;
  private Context mContext;
  public CachePreferences(Context context,String cacheName) {
    this.mContext = context;
    this.sPreferences = mContext.getSharedPreferences(cacheName, Context.MODE_PRIVATE);
  }
  public synchronized <T> T read(String key, Class<T> tClass) {
    Object object = null;
    if (tClass == String.class)
      object = sPreferences.getString(key, "");
    else if (tClass == Integer.class)
      object = sPreferences.getInt(key, 0);
    else if (tClass == Boolean.class)
      object = sPreferences.getBoolean(key, false);
    else if (tClass == Uri.class) {
      String uri = sPreferences.getString(key, "");
      object = !TextUtils.isEmpty(uri) ? Uri.parse(uri) : "";
    }
    return tClass.cast(object);
  }
  public synchronized <T> void write(String key, T value, Class<T> tClass) {
    SharedPreferences.Editor editor = sPreferences.edit();
    if (tClass == String.class)
      editor.putString(key, (String) value);
    else if (tClass == Integer.class)
      editor.putInt(key, (value != null ? (Integer) value : 0));
    else if (tClass == Boolean.class)
      editor.putBoolean(key, (value != null ? (Boolean) value : false));
    else if (tClass == Uri.class)
      editor.putString(key, (value != null ? new Gson().toJson(value) : null));
    editor.apply();
  }
  public synchronized void clear() {
    SharedPreferences.Editor editor = sPreferences.edit();
    editor.clear();
    editor.apply();
  }
}
