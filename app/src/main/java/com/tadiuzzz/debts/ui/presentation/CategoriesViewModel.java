package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    private MutableLiveData<List<Category>> liveDataCategories = new MutableLiveData<>();


    // ====== подписки для навигации

    public SingleLiveEvent navigateToPreviousScreen(){
        return navigateToPreviousScreen;
    }

    public SingleLiveEvent navigateToEditCategoryScreen(){
        return navigateToEditCategoryScreen;
    }

    // ===================================

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
                        liveDataCategories.setValue(categories);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public LiveData<List<Category>> getLiveDataCategories() { //Предоставляем объект LiveData View для подписки
        return liveDataCategories;
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
