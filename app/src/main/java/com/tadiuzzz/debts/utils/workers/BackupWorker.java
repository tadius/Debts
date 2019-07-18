package com.tadiuzzz.debts.utils.workers;

import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 18.07.2019.
 */
public class BackupWorker extends Worker {

    private final String DATABASE_NAME = "debts_database";
    private final String DATABASE_NAME_SHM = "debts_database-shm";
    private final String DATABASE_NAME_WAL = "debts_database-wal";

    private CompositeDisposable disposables;

    public BackupWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        disposables = new CompositeDisposable();
    }

    @NonNull
    @Override
    public Result doWork() {
        startBackup();
        return Result.success();
    }

    private void startBackup() {
        String backupName = "auto3hr";
        String pathToSdCardAppFolder = Environment.getExternalStorageDirectory() + File.separator + getApplicationContext().getResources().getString(R.string.app_name);
        String date = new SimpleDateFormat("_ddMMyyyyHHmmss", Locale.getDefault()).format(new Date());
        String fullPath = pathToSdCardAppFolder + File.separator + backupName + date + "_auto" + File.separator;

        prepareForBackup(fullPath);
    }

    private void prepareForBackup(String fullPath) {
        disposables.add(Completable.fromAction(() -> FileUtils.createFoldersForBackup(fullPath))
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        performBackup(fullPath);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToast.callWithArgument("Невозможно создать папку для резервных копий. Проверьте разрешения.");
                    }
                }));
    }

    private void performBackup(String fullPath) {
        disposables.add(Completable.fromAction(() -> backupDB(fullPath))
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
//                        showToast.callWithArgument("Резервная копия сохранена!");
                    }

                    @Override
                    public void onError(Throwable e) {
//                        showToast.callWithArgument("Ошибка создания резервной копии!");
                    }
                }));
    }

    private void backupDB(String pathToBackupSave) throws IOException {
        final String inFileName = getApplicationContext().getDatabasePath(DATABASE_NAME).toString();
        final String inFileNameshm = getApplicationContext().getDatabasePath(DATABASE_NAME_SHM).toString();
        final String inFileNamewal = getApplicationContext().getDatabasePath(DATABASE_NAME_WAL).toString();

        FileUtils.copyFile(inFileName, pathToBackupSave + DATABASE_NAME);
        FileUtils.copyFile(inFileNameshm, pathToBackupSave + DATABASE_NAME_SHM);
        FileUtils.copyFile(inFileNamewal, pathToBackupSave + DATABASE_NAME_WAL);
    }

}
