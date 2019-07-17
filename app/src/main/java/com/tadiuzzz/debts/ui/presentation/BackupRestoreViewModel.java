package com.tadiuzzz.debts.ui.presentation;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.utils.FileUtils;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.ui.SingleLiveEvent;
import com.tadiuzzz.debts.utils.SortingManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class BackupRestoreViewModel extends ViewModel {

    private final String DATABASE_NAME = "debts_database";
    private final String DATABASE_NAME_SHM = "debts_database-shm";
    private final String DATABASE_NAME_WAL = "debts_database-wal";

    private SortingManager sortingManager;

    private CompositeDisposable disposables;

    //    FilesRepository filesRepository;
    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> showToast = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> showPickFileDialog = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> showSetNameOfBackupDialog = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> showRequestPermissionsDialog = new SingleLiveEvent<>();
    private final MutableLiveData<Pair<String, Boolean>> showConfirmRestoreDialog = new MutableLiveData<>();
    private final MutableLiveData<Pair<String, Boolean>> showConfirmDeleteDialog = new MutableLiveData<>();

    private String pathToSdCardAppFolder = "";

    private Context context;

    private MutableLiveData<List<String>> listOfFiles = new MutableLiveData<>();

    @Inject
    public BackupRestoreViewModel(Context context, SortingManager sortingManager) {
        this.context = context;
        this.sortingManager = sortingManager;
        disposables = new CompositeDisposable();
        pathToSdCardAppFolder = Environment.getExternalStorageDirectory() + File.separator + context.getResources().getString(R.string.app_name);
    }

    private void loadListOfFiles() {
        listOfFiles.setValue(FileUtils.getListOfFiles(pathToSdCardAppFolder));
    }

    public LiveData<List<String>> getListOfFiles() {
        return listOfFiles;
    }

    public SingleLiveEvent getNavigateToPreviousScreenEvent() {
        return navigateToPreviousScreen;
    }

    public SingleLiveEvent getShowToastEvent() {
        return showToast;
    }

    public SingleLiveEvent getShowPickFileDialogEvent() {
        return showPickFileDialog;
    }

    public SingleLiveEvent getShowSetNameOfBackupDialogEvent() {
        return showSetNameOfBackupDialog;
    }

    public SingleLiveEvent getShowRequestPermissionsDialogEvent() {
        return showRequestPermissionsDialog;
    }

    public LiveData<Pair<String, Boolean>> getShowConfirmRestoreDialogEvent() {
        return showConfirmRestoreDialog;
    }

    public LiveData<Pair<String, Boolean>>  getShowConfirmDeleteDialogEvent() {
        return showConfirmDeleteDialog;
    }

    public void clickedOnBackupButton() {
        if (checkPermissions()) {
            showSetNameOfBackupDialog.call();
        } else {
            showRequestPermissionsDialog.call();
        }
    }

    public void clickedOnRestoreButton() {
        if (checkPermissions()) {
            loadListOfFiles();
            if (listOfFiles.getValue().isEmpty()) {
                showToast.callWithArgument("Не найдено ни одной резервной копии");
            } else {
                showPickFileDialog.call();
            }
        } else {
            showRequestPermissionsDialog.call();
        }
    }

    public void permissionsGranted() {
        showToast.callWithArgument("Разрешения получены! Повторите действие!");
    }

    public void permissionsCanceled() {
        showToast.callWithArgument("Разрешения не получены!");
    }

    public void enteredBackupName(String backupName) {
        String date = new SimpleDateFormat("_ddMMyyyyHHmmss", Locale.getDefault()).format(new Date());
        String fullPath = pathToSdCardAppFolder + File.separator + backupName + date + File.separator;

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
                        showToast.callWithArgument("Невозможно создать папку для резервных копий. Проверьте разрешения.");
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
                        showToast.callWithArgument("Резервная копия сохранена!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast.callWithArgument("Ошибка создания резервной копии!");
                    }
                }));
    }

    public void pickedFileToRestore(String nameOfBackupFolder) {
        String fullPath = pathToSdCardAppFolder + File.separator + nameOfBackupFolder + File.separator;
        showConfirmRestoreDialog.postValue(new Pair(fullPath, true));
    }

    public void canceledRestore() {
        showConfirmRestoreDialog.postValue(new Pair("", false));
        clickedOnRestoreButton();
    }

    public void confirmedFileRestore(String fullPath) {
        performRestore(fullPath);
        showConfirmRestoreDialog.postValue(new Pair("", false));
    }

    private void performRestore(String fullPath) {
        disposables.add(Completable.fromAction(() -> restoreDB(fullPath))
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        showToast.callWithArgument("Резервная копия восстановлена!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast.callWithArgument("Ошибка восстановления резервной копии!");
                    }
                }));
    }

    private void backupDB(String pathToBackupSave) throws IOException {
        final String inFileName = context.getDatabasePath(DATABASE_NAME).toString();
        final String inFileNameshm = context.getDatabasePath(DATABASE_NAME_SHM).toString();
        final String inFileNamewal = context.getDatabasePath(DATABASE_NAME_WAL).toString();

        FileUtils.copyFile(inFileName, pathToBackupSave + DATABASE_NAME);
        FileUtils.copyFile(inFileNameshm, pathToBackupSave + DATABASE_NAME_SHM);
        FileUtils.copyFile(inFileNamewal, pathToBackupSave + DATABASE_NAME_WAL);
    }

    private void restoreDB(String pathToBackupLocation) throws IOException {
        final String outFileName = context.getDatabasePath(DATABASE_NAME).toString();
        final String outFileNameSHM = context.getDatabasePath(DATABASE_NAME_SHM).toString();
        final String outFileNameWAL = context.getDatabasePath(DATABASE_NAME_WAL).toString();

        FileUtils.copyFile(pathToBackupLocation + DATABASE_NAME, outFileName);
        FileUtils.copyFile(pathToBackupLocation + DATABASE_NAME_SHM, outFileNameSHM);
        FileUtils.copyFile(pathToBackupLocation + DATABASE_NAME_WAL, outFileNameWAL);

        sortingManager.refreshSortingComparator(); //для того, чтобы после восстановления список долгов обновился
    }

    public void pickedFileToDelete(String nameOfBackupFolder) {
        String fullPath = pathToSdCardAppFolder + File.separator + nameOfBackupFolder;
        showConfirmDeleteDialog.postValue(new Pair(fullPath, true));
    }

    public void canceledDelete() {
        showConfirmDeleteDialog.postValue(new Pair("", false));
        clickedOnRestoreButton();
    }

    public void confirmedFileDelete(String fullPath) {
        performDelete(fullPath);
        showConfirmDeleteDialog.postValue(new Pair("", false));
    }

    private void performDelete(String fullPath) {
        disposables.add(Completable.fromAction(() -> deleteBackupFile(fullPath))
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        showToast.callWithArgument("Резервная копия удалена!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast.callWithArgument("Ошибка удаления резервной копии!");
                    }
                }));
    }

    private void deleteBackupFile(String pathToBackupLocation) throws IOException {
        FileUtils.deleteFile(pathToBackupLocation);
    }

    private boolean checkPermissions() {
        int readPermissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermissionStatus = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (readPermissionStatus == PackageManager.PERMISSION_GRANTED && writePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }



}
