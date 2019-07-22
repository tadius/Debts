package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class CategoriesViewModel extends ViewModel {

    private CompositeDisposable disposables;

    private DebtRepository debtRepository;

    private final SingleLiveEvent<Category> navigateToEditCategoryScreen = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();

    private MutableLiveData<List<Category>> listOfCategories = new MutableLiveData<>();


    @Inject
    public CategoriesViewModel(DebtRepository debtRepository) {

        this.debtRepository = debtRepository;

        disposables = new CompositeDisposable();

        loadAllCategories();
    }

    private void loadAllCategories() {
        disposables.add(debtRepository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Category>>() {
                    @Override
                    public void onNext(List<Category> categories) {
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

    public LiveData<List<Category>> getCategories() {
        return listOfCategories;
    }

    public SingleLiveEvent getNavigateToPreviousScreenEvent() {
        return navigateToPreviousScreen;
    }

    public SingleLiveEvent getNavigateToEditCategoryScreenEvent() {
        return navigateToEditCategoryScreen;
    }

    public void clickedOnCategory(Category category, boolean isPicking) {
        if (isPicking) {
            putCategoryToCache(category);
            navigateToPreviousScreen.call();
        } else {
            navigateToEditCategoryScreen.callWithArgument(category);
        }
    }

    private void putCategoryToCache(Category category) {
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        debtRepository.getCachedDebtPOJO().setCategory(categories);
        debtRepository.getCachedDebtPOJO().getDebt().setCategoryId(category.getId());
    }

    public void onAddButtonClick() {
        navigateToEditCategoryScreen.call();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
