package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tadiuzzz.debts.ui.SingleLiveEvent;
import com.tadiuzzz.debts.utils.SortingManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class ViewPagerViewModel extends AndroidViewModel {

    private CompositeDisposable disposables;

    private SingleLiveEvent<Void> navigateToEditDebtScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToPersonsScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToCategoriesScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToBackupRestoreScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToAboutScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> showFilterDialog = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> showSortDialog = new SingleLiveEvent<>();

    private MutableLiveData<String> sortMenuTitle = new MutableLiveData<>();
    private MutableLiveData<Integer> sortMenuIcon = new MutableLiveData<>();

    public ViewPagerViewModel(@NonNull Application application) {
        super(application);

        disposables = new CompositeDisposable();

        disposables.add(SortingManager.getInstance().getSortIcon()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer integer) {
                sortMenuIcon.setValue(integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));
    }

    public LiveData<String> getSortMenuTitle() {
        return sortMenuTitle;
    }

    public SingleLiveEvent getNavigateToEditDebtScreenEvent(){
        return navigateToEditDebtScreen;
    }

    public SingleLiveEvent getNavigateToPersonsScreenEvent() {
        return navigateToPersonsScreen;
    }

    public SingleLiveEvent getNavigateToCategoriesScreenEvent() {
        return navigateToCategoriesScreen;
    }

    public SingleLiveEvent getNavigateToBackupRestoreScreenEvent() {
        return navigateToBackupRestoreScreen;
    }

    public SingleLiveEvent getNavigateToAboutScreenEvent() {
        return navigateToAboutScreen;
    }

    public SingleLiveEvent getShowSortDialogEvent() {
        return showSortDialog;
    }

    public SingleLiveEvent getShowFilterDialogEvent() {
        return showFilterDialog;
    }

    public void clickedOnSortMenu() {
        showSortDialog.callWithArgument(SortingManager.getInstance().getSortBy());
    }

    public void clickedOnFilterMenu() {
        showFilterDialog.call();
    }

    public void clickedOnPersonsMenu() {
        navigateToPersonsScreen.call();
    }

    public void clickedOnCategoriesMenu() {
        navigateToCategoriesScreen.call();
    }

    public void clickedOnBackupRestoreMenu() {
        navigateToBackupRestoreScreen.call();
    }

    public void clickedOnAboutMenu() {
        navigateToAboutScreen.call();
    }

    public void clickedOnAddDebt() {
        navigateToEditDebtScreen.call();
    }

    public void pickedSortBy(int sortBy, String sortTitle) {
        SortingManager.getInstance().setSortBy(sortBy);
        sortMenuTitle.setValue(sortTitle);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public LiveData<Integer> getSortMenuIcon() {
        return sortMenuIcon;
    }
}
