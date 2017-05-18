package com.geekgarden.findcat.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;

/**
 * Created by rioswarawan on 1/3/17.
 */

public class ActivityUtils {
    /**
     * Get object from Intent that has been serialized as JSON string.
     */
    @Nullable
    public static final <T> T getObjectFromJsonIntent(@Nullable Intent intent,
                                                      @Nullable String key,
                                                      @NonNull Class<T> tClass) {
        if (intent == null)
            return null;

        String json = intent.getStringExtra(key);
        if (TextUtils.isEmpty(json))
            return null;

        try {
            return new Gson().fromJson(json, tClass);
        } catch (Exception e) {
            Log.printStackTrace(e);
            return null;
        }
    }

    /**
     * Get integer id based on class hashcode, truncated to 16 bits to conform
     * to {@link FragmentActivity#startActivityForResult(Intent, int)}. Android
     * only use lower 16 bits for requestCode, in order to support fragment on
     * older platform. <br />
     * <br />
     * Generated RequestCode shouldn't cause hash collision on small set of
     * branching (1 to 5 activities). <br />
     * <br />
     * TODO:
     * If requestCode need to be unique across entire app, just create a mapping
     * class like MGMap, backed with CPrefs. <br />
     * <br />
     */
    public static final int getRequestCode(@NonNull Class<?> classt, @NonNull String postfix) {
        return Math.abs(classt.getName().concat(postfix).hashCode() & 0xFFFF);
    }

    /**
     * Start another activity.
     */
    public static final void startActivity(@NonNull Activity activityFrom,
                                           @NonNull Class<? extends Activity> activityTo) {
        activityFrom.startActivity(new Intent(activityFrom, activityTo));
    }

    /**
     * From Fragment, start another activity.
     */
    public static final void startActivity(@NonNull Fragment fragmentFrom,
                                           @NonNull Class<? extends Activity> activityTo) {
        fragmentFrom.startActivity(new Intent(fragmentFrom.getActivity(), activityTo));
    }

    /**
     * Start another activity and wait for result.
     */
    public static final void startActivityAndWait(@NonNull Activity activityFrom,
                                                  @NonNull Class<? extends Activity> activityTo,
                                                  int requestCode) {
        Intent intent = new Intent(activityFrom, activityTo);
        activityFrom.startActivityForResult(intent, requestCode);
    }

    /**
     * Start android system setting page
     */
    public static final void startActivitySetting(@NonNull Activity activityFrom) {
        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityFrom.startActivity(intent);
    }

    /**
     * From Fragment, start another activity and wait for result. <br />
     * <br />
     * NOTE1: This will only work on normal fragment. Nested fragment cannot
     * receive result event so it must use {@link LocalBroadcastManager}
     * instead. See {@link #returnWithResult(Activity, String, String, Object)}. <br />
     * <br />
     * NOTE2: The activity hosting fragment must call super.onActivityResult()
     * as last fallback.
     */
    public static final void startActivityAndWait(@NonNull Fragment fragmentFrom,
                                                  @NonNull Class<? extends Activity> activityTo,
                                                  int requestCode) {
        Intent intent = new Intent(fragmentFrom.getActivity(), activityTo);
        fragmentFrom.startActivityForResult(intent, requestCode);
    }

    /**
     * Start another activity and pass parameter to it. <br />
     * We can use Context instead of Activity, but non-activity context sometimes gives more
     * headache, less safe than activity.
     */
    public static final <T> void startActivityWParam(@NonNull Activity activityFrom,
                                                     @NonNull Class<? extends Activity> activityTo,
                                                     String paramKey,
                                                     T param) {
        // NOTE: Generics will never accept primitives.
        // if supplied with primitive, it will be autoboxed automatically

        Intent intent = new Intent(activityFrom, activityTo);
        intent.putExtra(paramKey, new Gson().toJson(param));
        activityFrom.startActivity(intent);
    }

    /**
     * From Fragment, start another activity and pass parameter to it.
     */
    public static final <T> void startActivityWParam(@NonNull Fragment fragmentFrom,
                                                     @NonNull Class<? extends Activity> activityTo,
                                                     @Nullable String paramKey,
                                                     @Nullable T param) {
        Intent intent = new Intent(fragmentFrom.getActivity(), activityTo);
        intent.putExtra(paramKey, new Gson().toJson(param));
        fragmentFrom.startActivity(intent);
    }

    /**
     * Start another activity, pass parameter to it, and wait for result.
     */
    public static final <T> void startActivityWParamAndWait(@NonNull Activity activityFrom,
                                                            @NonNull Class<? extends Activity> activityTo,
                                                            @Nullable String paramKey,
                                                            @Nullable T param,
                                                            int requestCode) {
        Intent intent = new Intent(activityFrom, activityTo);
        intent.putExtra(paramKey, new Gson().toJson(param));
        activityFrom.startActivityForResult(intent, requestCode);
    }

    /**
     * From Fragment, start another activity, pass parameter to it, and wait for
     * result. <br />
     * <br />
     * NOTE1: This will only work on normal fragment. Nested fragment cannot
     * receive result event so it must use {@link LocalBroadcastManager}
     * instead. See {@link #returnWithResult(Activity, String, String, Object)}. <br />
     * <br />
     * NOTE2: The activity hosting fragment must call super.onActivityResult()
     * as last fallback.
     */
    public static final <T> void startActivityWParamAndWait(@NonNull Fragment fragmentFrom,
                                                            @NonNull Class<? extends Activity> activityTo,
                                                            @Nullable String paramKey,
                                                            @Nullable T param,
                                                            int requestCode) {
        Intent intent = new Intent(fragmentFrom.getActivity(), activityTo);
        intent.putExtra(paramKey, new Gson().toJson(param));
        fragmentFrom.startActivityForResult(intent, requestCode);
    }

    /**
     * Get parameter passed to an activity. Call this from activity onCreate()
     * or anywhere near initialization.
     */
    public static final <T> T getParam(@NonNull Activity activity,
                                       @Nullable String paramKey,
                                       @NonNull Class<T> paramClass) {
        return getObjectFromJsonIntent(activity.getIntent(), paramKey, paramClass);
    }

    /**
     * Finish an activity and set result T. Use this if you want activity to
     * return value other than RESULT_OK or RESULT_CANCELED. <br />
     * <br />
     * NOTE: This method will always set resultCode=RESULT_OK. <br />
     * <br />
     * If you want just to return bool, simply call
     * {@link Activity#setResult(int)} with RESULT_OK or RESULT_CANCELED. <br />
     * <br />
     */
    public static final <T> void returnWithResult(@NonNull Activity activity,
                                                  @Nullable String resultKey,
                                                  @Nullable T result) {
        Intent intent = new Intent();
        intent.putExtra(resultKey, new Gson().toJson(result));
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    /**
     * Get Activity from View. <br />
     * Storing reference to Activity from inside View should be avoided, because a View could be
     * re-used by different fragments and activities, potential for memory/reference leak. <br />
     * A safer approach would be to search through View's own context to find root Activity. <br />
     * Code from http://stackoverflow.com/questions/8276634
     */
    @Nullable
    public static final Activity getActivity(@NonNull View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    /**
     * Broadcast simple object as JSON string.
     */
    public static final <T> void broadcastObjectAsJsonString(@NonNull Context ctx,
                                                             @Nullable String broadcastKey,
                                                             @Nullable String tKey,
                                                             @Nullable T t) {
        Intent intent = new Intent(broadcastKey);
        if (tKey != null && t != null)
            intent.putExtra(tKey, new Gson().toJson(t));
        LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
    }

    /**
     * Similar to returnWithResult(), but the receiver we return to is either a nested fragment, or
     * other activity that doesn't originally start our activity (like dimo sdk),
     * so here we using LocalBroadcastManager instead of Activity.setResult(). <br />
     * <br />
     * To get result: <br />
     * 1. On fragment init, call registerBroadcastReceiver(). <br />
     * 2. On your BroadcastReceiver event handler, call {@link #getResult(Intent, String, Class)}. <br />
     * 3. On fragment destroy, call unregisterBroadcastReceiver(). <br />
     * <br />
     * NOTE: This method will not set any resultCode.
     */
    public static final <T> void returnWithResult(@NonNull Activity activity,
                                                  @Nullable String broadcastKey,
                                                  @Nullable String resultKey,
                                                  @Nullable T result) {
        broadcastObjectAsJsonString(activity, broadcastKey, resultKey, result);
        activity.finish();
    }

    /**
     * Return broadcast with no result.
     * This is intended as alternative of {@link #returnWithResult(Activity, String, String, Object)}
     * to use at onPause() event. This method doesn't call activity.finish().
     */
    public static final void returnWithNoResult(@NonNull Activity activity,
                                                @Nullable String broadcastKey) {
        broadcastObjectAsJsonString(activity, broadcastKey, null, null);
    }

    /**
     * Get T result from returned Activity. Call this from
     * {@link Activity#onActivityResult}, when resultCode=RESULT_OK. Also check
     * requestCode for your class. <br />
     * <br />
     * If you use fragment LocalBroadcastManager with
     * {@link #returnWithResult(Activity, String, String, Object)}, call this
     * from your BroadcastReceiver method handler.
     */
    public static final <T> T getResult(@NonNull Intent intent,
                                        @Nullable String resultKey,
                                        @NonNull Class<T> resultClass) {
        return getObjectFromJsonIntent(intent, resultKey, resultClass);
    }

    /**
     * Helper for LocalBroadcastManager.registerReceiver(). Call this from
     * fragment onActivityCreated().
     */
    public static final void registerBroadcastReceiver(@NonNull Context ctx,
                                                       @NonNull BroadcastReceiver receiver,
                                                       @NonNull String broadcastKey) {
        LocalBroadcastManager.getInstance(ctx).registerReceiver(receiver, new IntentFilter(broadcastKey));
    }

    /**
     * Helper for LocalBroadcastManager.unregisterReceiver(). Call this from
     * fragment onDestroy(). Do not call on onPause() or onStop() because we still need to
     * receive broadcast even when fragment/activity goes to background.
     */
    public static final void unregisterBroadcastReceiver(@NonNull Context ctx,
                                                         @NonNull BroadcastReceiver receiver) {
        LocalBroadcastManager.getInstance(ctx).unregisterReceiver(receiver);
    }

    /**
     * Clear all activity other than Main (prevent re-access to non-login area).
     * Main activity will then start Login activity. <br />
     * <br />
     * NOTE: Main activity should always be at bottom stack.
     */
    public static final void returnToLoginPage(@NonNull Activity fromActivity, @NonNull Class<? extends Activity> loginActivity) {
        // NOTE: going to Login page means we clear access token
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(fromActivity);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        returnClearTopFinish(fromActivity, loginActivity);
    }

    /**
     * Start or return to specific activity, and clear all other activity in front of it. <br />
     * <br />
     * NOTE: All MainTab item fragment (home, calendar, products, etc) should not call finish(),
     * to avoid MainTab closed.
     */
    public static final void returnClearTop(@NonNull Context ctx,
                                            @NonNull Class<? extends Activity> activityTo) {
        Intent intent = new Intent(ctx, activityTo);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(intent);
    }

    /**
     * Call {@link #returnClearTop(Context, Class)} and finish() the fromActivity.
     */
    public static final void returnClearTopFinish(@NonNull Activity fromActivity,
                                                  @NonNull Class<? extends Activity> activityTo) {
        returnClearTop(fromActivity, activityTo);
        fromActivity.finish();
    }
}
