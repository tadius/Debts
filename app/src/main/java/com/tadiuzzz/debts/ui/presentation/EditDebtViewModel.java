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

    public final int TYPE_OF_DATE_START = 100;
    public final int TYPE_OF_DATE_EXPIRATION = 200;
    public final int TYPE_OF_DATE_END = 300;

    DebtRepository debtRepository;
    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToPersonsList = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToCategoryList = new SingleLiveEvent<>();
    private final SingleLiveEvent<Calendar> showPickDateOfStartDialog = new SingleLiveEvent<>();
    private final SingleLiveEvent<Calendar> showPickDateOfExpirationDialog = new SingleLiveEvent<>();
    private final SingleLiveEvent<Calendar> showPickDateOfEndDialog = new SingleLiveEvent<>();

    private MutableLiveData<DebtPOJO> liveDataCachedDebtPOJO = new MutableLiveData<>();

    // ====== подписки для навигации и диалоговых окон

    public SingleLiveEvent navigateToPreviousScreen() {
        return navigateToPreviousScreen;
    }

    public SingleLiveEvent navigateToPickPersonScreen() {
        return navigateToPersonsList;
    }

    public SingleLiveEvent navigateToPickCategoryScreen() {
        return navigateToCategoryList;
    }

    public SingleLiveEvent showPickDateOfStartDialog() {
        return showPickDateOfStartDialog;
    }

    public SingleLiveEvent showPickDateOfExpirationDialog() {
        return showPickDateOfExpirationDialog;
    }

    public SingleLiveEvent showPickDateOfEndDialog() {
        return showPickDateOfEndDialog;
    }

    // ===================================

    public EditDebtViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
        loadCachedDebtPOJO();
    }

    private void loadCachedDebtPOJO() {
        liveDataCachedDebtPOJO.setValue(debtRepository.getCachedDebtPOJO());
    }

    public LiveData<DebtPOJO> getLiveDataCachedDebtPOJO() { //Предоставляем объект LiveData View для подписки
        return liveDataCachedDebtPOJO;
    }

    public void pickPersonClicked() {
        navigateToPersonsList.call();
    }

    public void pickCategoryClicked() {
        navigateToCategoryList.call();
    }

    public void pickDateOfStartClicked() {
        Calendar calendar = Calendar.getInstance();
        long dateOfStart = debtRepository.getCachedDebtPOJO().getDebt().getDateOfStart();
        if (dateOfStart != 0) {
            calendar.setTimeInMillis(dateOfStart);
            showPickDateOfStartDialog.callWithArgument(calendar);
        }
    }

    public void pickDateOfExpirationClicked() {
        Calendar calendar = Calendar.getInstance();
        long dateOfExpiration = debtRepository.getCachedDebtPOJO().getDebt().getDateOfExpiration();
        if (dateOfExpiration != 0) {
            calendar.setTimeInMillis(dateOfExpiration);
            showPickDateOfExpirationDialog.callWithArgument(calendar);
        }
    }

    public void pickDateOfEndClicked() {
        Calendar calendar = Calendar.getInstance();
        long dateOfEnd = debtRepository.getCachedDebtPOJO().getDebt().getDateOfEnd();
        if (dateOfEnd != 0) {
            calendar.setTimeInMillis(dateOfEnd);
            showPickDateOfEndDialog.callWithArgument(calendar);
        }
    }

    public void pickedDate(int year, int month, int day, int typeOfDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        DebtPOJO debtPOJO = debtRepository.getCachedDebtPOJO();

        switch (typeOfDate) {
            case TYPE_OF_DATE_START:
                debtPOJO.getDebt().setDateOfStart(calendar.getTimeInMillis());
                break;
            case TYPE_OF_DATE_EXPIRATION:
                debtPOJO.getDebt().setDateOfExpiration(calendar.getTimeInMillis());
                break;
            case TYPE_OF_DATE_END:
                debtPOJO.getDebt().setDateOfEnd(calendar.getTimeInMillis());
                break;
        }

        liveDataCachedDebtPOJO.setValue(debtRepository.getCachedDebtPOJO());

    }

    public void changedTextDebtAmount(String allText) {
        if (allText.trim().equals("")) allText = "0.0";
        debtRepository.getCachedDebtPOJO().getDebt().setAmount(Double.valueOf(allText.trim()));
    }

    public void changedTextDebtDescription(final String allText) {
        debtRepository.getCachedDebtPOJO().getDebt().setDescription(allText.trim());
    }

}
