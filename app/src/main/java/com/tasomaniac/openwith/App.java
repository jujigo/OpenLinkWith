package com.tasomaniac.openwith;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class App extends Application {

    Analytics analytics;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Fabric.with(this, new Crashlytics());
            Timber.plant(new CrashReportingTree());
        }
        analytics = provideAnalytics();
    }

    private Analytics provideAnalytics() {
        if (BuildConfig.DEBUG) {
            return new Analytics.DebugAnalytics();
        }

        GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(this);
        Tracker tracker = googleAnalytics.newTracker(BuildConfig.ANALYTICS_KEY);
        tracker.setSessionTimeout(300); // ms? s? better be s.
        return new Analytics.AnalyticsImpl(tracker);
    }

    public static App getApp(Context context) {
        return (App) context.getApplicationContext();
    }

    public Analytics getAnalytics() {
        return analytics;
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            Crashlytics.log(priority, tag, message);
            if (t != null && priority >= Log.WARN) {
                Crashlytics.logException(t);
            }
        }
    }
}
