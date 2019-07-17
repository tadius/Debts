package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.data.DebtRepository;
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
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class ViewPagerViewModel extends ViewModel {

    private SortingManager sortingManager;
    private DebtRepository debtRepository;
    private FilterManager filterManager;

    private CompositeDisposable disposables;

    private SingleLiveEvent<Void> navigateToEditDebtScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToPersonsScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToCategoriesScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToBackupRestoreScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToAboutScreen = new SingleLiveEvent<>();

    private MutableLiveData<List<Category>> listOfCategories = new MutableLiveData<>();

    private final MutableLiveData<Pair<List<Category>, Boolean>> showFilterCategoryDialog = new MutableLiveData<>();
    private final MutableLiveData<Pair<String, Boolean>> showFilterPersonDialog = new MutableLiveData<>();

    private MutableLiveData<Integer> sortMenuCheckedItem = new MutableLiveData<>();
    private MutableLiveData<String> sortMenuTitle = new MutableLiveData<>();
    private MutableLiveData<Integer> sortMenuIcon = new MutableLiveData<>();

    @Inject
    public ViewPagerViewModel(DebtRepository debtRepository, SortingManager sortingManager, FilterManager filterManager) {

        this.debtRepository = debtRepository;
        this.sortingManager = sortingManager;
        this.filterManager = filterManager;

        disposables = new CompositeDisposable();

        disposables.add(sortingManager.getSortIcon()
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

        disposables.add(sortingManager.getSortTitle()
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

        disposables.add(debtRepository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Category>>() {
                    @Override
                    public void onNext(List<Category> categories) {
                        filterManager.setFilteredCategories(categories);
                        listOfCategories.setValue(categories);
                    }

                    @Override
                    public void onError(Throwable t) {
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

    public SingleLiveEvent getNavigateToEditDebtScreenEvent() {
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

    public LiveData<Pair<List<Category>, Boolean>> getShowFilterCategoryDialogEvent() {
        return showFilterCategoryDialog;
    }

    public LiveData<Pair<String, Boolean>> getShowFilterPersonDialogEvent() {
        return showFilterPersonDialog;
    }

    public void clickedOnFilterActiveMenu() {
        //обновить фильтр-менеджер на только активные
    }

    public void clickedOnFilterCategoryMenu() {
        showFilterCategoryDialog.postValue(new Pair(filterManager.getFilteredCategories(), true));
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
        sortingManager.setSortBy(sortBy, sortTitle);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }


    public void selectedFilteredCategories(ArrayList<Category> selectedCategories) {
        filterManager.setFilteredCategories(selectedCategories);
        sortingManager.refreshSortingComparator();
    }

    public void clearCategoriesFilter() {
        disposables.add(debtRepository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Category>>() {
                    @Override
                    public void onNext(List<Category> categories) {
                        filterManager.setFilteredCategories(categories);
                        sortingManager.refreshSortingComparator();
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }
}
