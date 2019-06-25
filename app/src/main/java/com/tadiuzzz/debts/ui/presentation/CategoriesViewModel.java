package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class CategoriesViewModel extends AndroidViewModel {

    DebtRepository debtRepository;
    private final SingleLiveEvent<Category> navigateToEditCategoryScreen = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();

    private MutableLiveData<List<Category>> listOfCategories = new MutableLiveData<>();


    public CategoriesViewModel(@NonNull Application application) {
        super(application);

        debtRepository = new DebtRepository(application);
        loadAllCategories();
    }

    private void loadAllCategories() {
        debtRepository.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<Category>>() {
                    @Override
                    public void onNext(List<Category> categories) {
                        listOfCategories.setValue(categories);
                    }

                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onComplete() {}
                });
    }

    public LiveData<List<Category>> getCategories() {
        return listOfCategories;
    }

    public SingleLiveEvent getNavigateToPreviousScreenEvent(){
        return navigateToPreviousScreen;
    }

    public SingleLiveEvent getNavigateToEditCategoryScreenEvent(){
        return navigateToEditCategoryScreen;
    }

    public void clickedOnCategory(Category category, boolean isPicking) {
        if(isPicking){
            putCategoryToCache(category);
            navigateToPreviousScreen.call();
        } else {
            navigateToEditCategoryScreen.callWithArgument(category);
        }
    }

    private void putCategoryToCache(Category category){
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        debtRepository.getCachedDebtPOJO().setCategory(categories);
        debtRepository.getCachedDebtPOJO().getDebt().setCategoryId(category.getId());
    }

}
