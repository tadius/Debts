package com.tadiuzzz.debts;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.tadiuzzz.debts.utils.workers.BackupWorker;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;


/**
 * Created by Simonov.vv on 16.07.2019.
 */
public class MainViewModel extends ViewModel {

    private static final String BACKUP_WORK_TAG = "BACKUP_WORK_TAG";
    private static final String BACKUP_WORK_NAME = "backupWork";
    private static final String PREFERENCE_KEY_AUTOBACKUP = "pref_autobackup_enabled";
    private static final String PREFERENCE_KEY_AUTOBACKUP_INTERVAL = "pref_interval_int";
    private Context context;

    private SharedPreferences preferences;
    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(PREFERENCE_KEY_AUTOBACKUP)) {
                setupWorker();
            } else if (key.equals(PREFERENCE_KEY_AUTOBACKUP_INTERVAL)) {
                //если меняется интервал - удаляем старый work и создаем новый, иначе интервал останется старый
                cancelWorker();
                setupWorker();
            }
        }
    };

    @Inject
    public MainViewModel(Context context) {
        this.context = context;

        setupSharedPreferences();
        setupWorker();
    }

    private void setupSharedPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    private void setupWorker() {
        boolean isAutobackupActive = preferences.getBoolean(PREFERENCE_KEY_AUTOBACKUP, false);
        int interval = preferences.getInt(PREFERENCE_KEY_AUTOBACKUP_INTERVAL, 1);
        createWork(isAutobackupActive, interval);
    }

    private void createWork(boolean isActive, int interval) {
        if (isActive) {
            //This is the subclass of our WorkRequest
            final PeriodicWorkRequest workRequest
                    = new PeriodicWorkRequest.Builder(BackupWorker.class, interval, TimeUnit.DAYS)
                    .addTag(BACKUP_WORK_TAG)
                    .build();
            //создаем уникальный Work, чтобы не создавался новый Work после каждого запуска приложения
            WorkManager.getInstance().enqueueUniquePeriodicWork(BACKUP_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, workRequest);
        } else {
            cancelWorker();
        }

    }

    private void cancelWorker() {
        WorkManager.getInstance().cancelAllWorkByTag(BACKUP_WORK_TAG);
    }

}
