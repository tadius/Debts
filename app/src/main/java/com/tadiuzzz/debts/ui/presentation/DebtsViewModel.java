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

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class DebtsViewModel extends AndroidViewModel {

    private CompositeDisposable disposables;

    DebtRepository debtRepository;
    private SingleLiveEvent<Void> navigateToEditDebtScreen = new SingleLiveEvent<>();

    private MutableLiveData<List<DebtPOJO>> listOfDebtPOJOS = new MutableLiveData<>();
    private boolean amIBorrower;

    public DebtsViewModel(@NonNull Application application) {
        super(application);

        disposables = new CompositeDisposable();

        debtRepository = new DebtRepository(application);
    }

    private void loadAllDebtPOJOs() {

        disposables.add(debtRepository.getAllDebtPOJOs()
                .flatMap(debtPOJOS -> Flowable.fromIterable(debtPOJOS)
                        .filter(debtPOJO -> debtPOJO.getDebt().amIBorrower() == amIBorrower)
                        .toList()
                        .toFlowable()
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<DebtPOJO>>() {
                    @Override
                    public void onNext(List<DebtPOJO> debtPOJOS) {
                        listOfDebtPOJOS.setValue(debtPOJOS);
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                }));

    }

    public LiveData<List<DebtPOJO>> getListOfDebtPOJOS() { //Предоставляем объект LiveData View для подписки
        return listOfDebtPOJOS;
    }

    public SingleLiveEvent getNavigateToEditDebtScreenEvent() {
        return navigateToEditDebtScreen;
    }

    public void viewLoaded() {
        debtRepository.clearDebtPOJOCache();
        loadAllDebtPOJOs();
    }

    public void clickedOnDebtPOJO(DebtPOJO debtPOJO) {
        debtRepository.putDebtPOJOtoCache(debtPOJO);

        navigateToEditDebtScreen.call();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public void setAmIBorrower(boolean amIBorrower) {
        this.amIBorrower = amIBorrower;
    }
}
