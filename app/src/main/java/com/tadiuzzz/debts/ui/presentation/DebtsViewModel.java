package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Debt;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class DebtsViewModel extends AndroidViewModel {

    DebtRepository debtRepository;
    private SingleLiveEvent<Void> navigateToEditDebtScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToPersonsScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToCategoriesScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToAboutScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> showFilterDialog = new SingleLiveEvent<>();

    private MutableLiveData<List<DebtPOJO>> listOfDebtPOJOS = new MutableLiveData<>();

    public DebtsViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
        loadAllDebtPOJOs();
    }

    private void loadAllDebtPOJOs() {
        debtRepository.getAllDebtPOJOs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<DebtPOJO>>() {
                    @Override
                    public void onNext(List<DebtPOJO> debtPOJOS) {
                        listOfDebtPOJOS.setValue(debtPOJOS);
                    }

                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onComplete() {}
                });
    }

    public LiveData<List<DebtPOJO>> getListOfDebtPOJOS() { //Предоставляем объект LiveData View для подписки
        return listOfDebtPOJOS;
    }

    public SingleLiveEvent getNavigateToEditDebtScreenEvent(){
        return navigateToEditDebtScreen;
    }

    public SingleLiveEvent getNavigateToPersonsScreenEvent(){
        return navigateToPersonsScreen;
    }

    public SingleLiveEvent getNavigateToCategoriesScreenEvent(){
        return navigateToCategoriesScreen;
    }

    public SingleLiveEvent getNavigateToAboutScreenEvent(){
        return navigateToAboutScreen;
    }

    public SingleLiveEvent getShowFilterDialogEvent(){
        return showFilterDialog;
    }

    public void viewLoaded() {
        debtRepository.clearDebtPOJOCache();
        loadAllDebtPOJOs();
    }

    public void clickedOnDebtPOJO(DebtPOJO debtPOJO){
        debtRepository.putDebtPOJOtoCache(debtPOJO);

        navigateToEditDebtScreen.call();
    }

    public void clickedOnFilterMenu(){
        showFilterDialog.call();
    }

    public void clickedOnPersonsMenu(){
        navigateToPersonsScreen.call();
    }

    public void clickedOnCategoriesMenu(){
        navigateToCategoriesScreen.call();
    }

    public void clickedOnAboutMenu(){
        navigateToAboutScreen.call();
    }

}
