package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Debt;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditDebtViewModel extends AndroidViewModel {
    public static final String TAG = "logTag";

    DebtRepository debtRepository;
    private final SingleLiveEvent<Void> navigateToPersonsList = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToCategoryList = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> showPickDateOfStartDialog = new SingleLiveEvent<>();

    public EditDebtViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
    }

    public Maybe<DebtPOJO> getDebtPOJOByID(int id) {
        return debtRepository.getDebtPOJOByID(id);
    }

    public Completable insertDebt(Debt debt) {
        return debtRepository.insertDebt(debt);
    }

    public Completable updateDebt(Debt debt) {
        return debtRepository.updateDebt(debt);
    }

    public Completable deleteDebt(Debt debt) {
        return debtRepository.deleteDebt(debt);
    }

    public void pickPersonClicked(){
        navigateToPersonsList.call();
    }

    public SingleLiveEvent navigateToPickPersonScreen(){
        return navigateToPersonsList;
    }

    public void pickCategoryClicked(){
        navigateToCategoryList.call();
    }

    public SingleLiveEvent navigateToPickCategoryScreen(){
        return navigateToCategoryList;
    }

    public void pickDateOfStartClicked(){
        showPickDateOfStartDialog.call();
    }

    public SingleLiveEvent showPickDateOfStartDialog(){
        return showPickDateOfStartDialog;
    }

    public Maybe<DebtPOJO> getCachedDebtPOJO(){
        return debtRepository.getCachedDebtPOJO();
    }

    public void changedTextDebtAmount(final String allText) {
        debtRepository.getCachedDebtPOJO().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<DebtPOJO>() {
                    @Override
                    public void onSuccess(DebtPOJO debtPOJO) {
                        String text = allText;
                        if(text.trim().equals("")) text = "0.0";
                        debtPOJO.getDebt().setAmount(Double.valueOf(text.trim()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void changedTextDebtDescription(final String allText) {
        debtRepository.getCachedDebtPOJO().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<DebtPOJO>() {
                    @Override
                    public void onSuccess(DebtPOJO debtPOJO) {
                        debtPOJO.getDebt().setDescription(allText.trim());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
