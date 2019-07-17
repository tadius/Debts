package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.ui.SingleLiveEvent;
import com.tadiuzzz.debts.utils.FilterManager;
import com.tadiuzzz.debts.utils.SortingManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class ViewPagerViewModel extends ViewModel {

    private CompositeDisposable disposables;

    private SingleLiveEvent<Void> navigateToEditDebtScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToPersonsScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToCategoriesScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToBackupRestoreScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToAboutScreen = new SingleLiveEvent<>();

    private MutableLiveData<List<Category>> listOfCategories = new MutableLiveData<>();

    private final MutableLiveData<Pair<ArrayList<Integer>, Boolean>> showFilterCategoryDialog = new MutableLiveData<>();
    private final MutableLiveData<Pair<String, Boolean>> showFilterPersonDialog = new MutableLiveData<>();

    private MutableLiveData<Integer> sortMenuCheckedItem = new MutableLiveData<>();
    private MutableLiveData<String> sortMenuTitle = new MutableLiveData<>();
    private MutableLiveData<Integer> sortMenuIcon = new MutableLiveData<>();

    @Inject
    public ViewPagerViewModel() {

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

        disposables.add(FilterManager.getInstance().getListOfCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Category>>() {
                    @Override
                    public void onNext(List<Category> categories) {
                        listOfCategories.setValue(categories);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    public LiveData<List<Category>> getListOfCategories() {
        return listOfCategories;
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

    public LiveData<Pair<ArrayList<Integer>, Boolean>> getShowFilterCategoryDialogEvent() {
        return showFilterCategoryDialog;
    }

    public LiveData<Pair<String, Boolean>> getShowFilterPersonDialogEvent() {
        return showFilterPersonDialog;
    }

    public void clickedOnFilterActiveMenu() {
        //обновить фильтр-менеджер на только активные
    }

    public void clickedOnFilterCategoryMenu() {
//        showFilterCategoryDialog.postValue(FilterManager.getNumbersOfFilteredCategories(), true);
    }

    public void clickedOnFilterPersonMenu() {
//        showFilterPersonDialog.call();
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
