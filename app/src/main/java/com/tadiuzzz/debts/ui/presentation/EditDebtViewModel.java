package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Debt;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditDebtViewModel extends AndroidViewModel {
    public static final String TAG = "logTag";

    DebtRepository debtRepository;
    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToPersonsList = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToCategoryList = new SingleLiveEvent<>();
    private final SingleLiveEvent<Calendar> showPickDateOfStartDialog = new SingleLiveEvent<>();

    private MutableLiveData<DebtPOJO> liveDataCachedDebtPOJO = new MutableLiveData<>();

    // ====== подписки для навигации и диалоговых окон

    public SingleLiveEvent navigateToPreviousScreen(){
        return navigateToPreviousScreen;
    }

    public SingleLiveEvent navigateToPickPersonScreen(){
        return navigateToPersonsList;
    }

    public SingleLiveEvent navigateToPickCategoryScreen(){
        return navigateToCategoryList;
    }

    public SingleLiveEvent showPickDateOfStartDialog(){
        return showPickDateOfStartDialog;
    }

    // ===================================

    public EditDebtViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
        loadCachedDebtPOJO();
    }

    private void loadCachedDebtPOJO() {
        debtRepository.getCachedDebtPOJO()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<DebtPOJO>() {
                    @Override
                    public void onSuccess(DebtPOJO debtPOJO) {
                        liveDataCachedDebtPOJO.setValue(debtPOJO);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public LiveData<DebtPOJO> getLiveDataCachedDebtPOJO() { //Предоставляем объект LiveData View для подписки
        return liveDataCachedDebtPOJO;
    }

    public void pickPersonClicked(){
        navigateToPersonsList.call();
    }

    public void pickCategoryClicked(){
        navigateToCategoryList.call();
    }

    public void pickDateOfStartClicked(){
        Calendar calendar = Calendar.getInstance();

        debtRepository.getCachedDebtPOJO().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<DebtPOJO>() {
                    @Override
                    public void onSuccess(DebtPOJO debtPOJO) {
                        if (debtPOJO.getDebt().getDateOfStart() != 0) {
                            calendar.setTimeInMillis(debtPOJO.getDebt().getDateOfStart());
                            showPickDateOfStartDialog.callWithArgument(calendar);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void pickedDateOfStart(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        debtRepository.getCachedDebtPOJO().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<DebtPOJO>() {
                    @Override
                    public void onSuccess(DebtPOJO debtPOJO) {
                        debtPOJO.getDebt().setDateOfStart(calendar.getTimeInMillis());
                        liveDataCachedDebtPOJO.setValue(debtPOJO);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
