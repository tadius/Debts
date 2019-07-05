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


    private MutableLiveData<Integer> sortMenuCheckedItem = new MutableLiveData<>();
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

        disposables.add(SortingManager.getInstance().getSortTitle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String title) {
                        sortMenuTitle.setValue(title);
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

    public LiveData<Integer> getSortMenuCheckedItem() {
        return sortMenuCheckedItem;
    }

    public LiveData<Integer> getSortMenuIcon() {
        return sortMenuIcon;
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

    public SingleLiveEvent getShowFilterDialogEvent() {
        return showFilterDialog;
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
        sortMenuCheckedItem.setValue(sortBy);
        SortingManager.getInstance().setSortBy(sortBy, sortTitle);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

}
