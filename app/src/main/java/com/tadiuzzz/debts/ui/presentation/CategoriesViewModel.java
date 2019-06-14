package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.tadiuzzz.debts.data.CacheEditing;
import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class CategoriesViewModel extends AndroidViewModel {

    DebtRepository debtRepository;
    private final SingleLiveEvent<Category> navigateToEditCategoryScreen = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();

    public CategoriesViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
    }

    public Flowable<List<Category>> getCategories(){
        return debtRepository.getAllCategories();
    }

    public Completable insertCategory(Category category){
        return debtRepository.insertCategory(category);
    }

    public Completable updateCategory(Category category){
        return debtRepository.updateCategory(category);
    }

    public Completable deleteCategory(Category category){
        return debtRepository.deleteCategory(category);
    }

    public SingleLiveEvent navigateToEditCategoryScreen(){
        return navigateToEditCategoryScreen;
    }

    public SingleLiveEvent navigateToPreviousScreen(){
        return navigateToPreviousScreen;
    }

    public void clickedOnCategory(Category category, boolean isPicking) {
        if(isPicking){
            List<Category> categories = new ArrayList<>();
            categories.add(category);
            putCategoryToCache(categories);
            navigateToPreviousScreen.call();
        } else {
            navigateToEditCategoryScreen.callWithArgument(category);
        }
    }

    public void putCategoryToCache(List<Category> categories){
        debtRepository.getCachedDebtPOJO()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<DebtPOJO>() {

                    @Override
                    public void onSuccess(DebtPOJO debtPOJO) {
                        debtPOJO.setCategory(categories);

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
