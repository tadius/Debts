package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class DebtsViewModel extends AndroidViewModel {

    DebtRepository debtRepository;
    private SingleLiveEvent<Void> navigateToEditDebtScreen = new SingleLiveEvent<>();

    private MutableLiveData<List<DebtPOJO>> listOfDebtPOJOS = new MutableLiveData<>();
    private MutableLiveData<List<DebtPOJO>> listOfDebtPOJOSIAmCreditor = new MutableLiveData<>();
    private MutableLiveData<List<DebtPOJO>> listOfDebtPOJOSIAmBorrower = new MutableLiveData<>();

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

        debtRepository.getAllDebtPOJOsIAmCreditor()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<DebtPOJO>>() {
                    @Override
                    public void onNext(List<DebtPOJO> debtPOJOS) {
                        listOfDebtPOJOSIAmCreditor.setValue(debtPOJOS);
                    }

                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onComplete() {}
                });

        debtRepository.getAllDebtPOJOsIAmBorrower()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<DebtPOJO>>() {
                    @Override
                    public void onNext(List<DebtPOJO> debtPOJOS) {
                        listOfDebtPOJOSIAmBorrower.setValue(debtPOJOS);
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

    public LiveData<List<DebtPOJO>> getListOfDebtPOJOSIAmCreditor() {
        return listOfDebtPOJOSIAmCreditor;
    }

    public LiveData<List<DebtPOJO>> getListOfDebtPOJOSIAmBorrower() {
        return listOfDebtPOJOSIAmBorrower;
    }

    public SingleLiveEvent getNavigateToEditDebtScreenEvent(){
        return navigateToEditDebtScreen;
    }

    public void viewLoaded() {
        debtRepository.clearDebtPOJOCache();
        loadAllDebtPOJOs();
    }

    public void clickedOnDebtPOJO(DebtPOJO debtPOJO){
        debtRepository.putDebtPOJOtoCache(debtPOJO);

        navigateToEditDebtScreen.call();
    }
}
