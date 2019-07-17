package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import java.util.Calendar;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditDebtViewModel extends ViewModel {
    public static final String TAG = "logTag";

    public final int TYPE_OF_DATE_START = 100;
    public final int TYPE_OF_DATE_EXPIRATION = 200;
    public final int TYPE_OF_DATE_END = 300;

    private CompositeDisposable disposables;

    private DebtRepository debtRepository;

    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToPersonsList = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToCategoryList = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> showToast = new SingleLiveEvent<>();

    private final SingleLiveEvent<Calendar> showPickDateOfStartDialog = new SingleLiveEvent<>();
    private final SingleLiveEvent<Calendar> showPickDateOfExpirationDialog = new SingleLiveEvent<>();
    private final SingleLiveEvent<Calendar> showPickDateOfEndDialog = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> showConfirmDeleteDialog = new MutableLiveData<>();

    private final SingleLiveEvent<Boolean> showEndDateContainer = new SingleLiveEvent<>();

    private MutableLiveData<DebtPOJO> liveDataCachedDebtPOJO = new MutableLiveData<>();

    @Inject
    public EditDebtViewModel(DebtRepository debtRepository) {
        this.debtRepository = debtRepository;

        disposables = new CompositeDisposable();

        loadCachedDebtPOJO();
    }

    private void loadCachedDebtPOJO() {
        liveDataCachedDebtPOJO.setValue(debtRepository.getCachedDebtPOJO());
    }

    public LiveData<DebtPOJO> getCachedDebtPOJO() {
        return liveDataCachedDebtPOJO;
    }

    public SingleLiveEvent getNavigateToPreviousScreenEvent() {
        return navigateToPreviousScreen;
    }

    public SingleLiveEvent getNavigateToPickPersonScreenEvent() {
        return navigateToPersonsList;
    }

    public SingleLiveEvent getNavigateToPickCategoryScreenEvent() {
        return navigateToCategoryList;
    }

    public SingleLiveEvent getShowToastEvent() {
        return showToast;
    }

    public SingleLiveEvent getShowPickDateOfStartDialogEvent() {
        return showPickDateOfStartDialog;
    }

    public SingleLiveEvent getShowPickDateOfExpirationDialogEvent() {
        return showPickDateOfExpirationDialog;
    }

    public SingleLiveEvent getShowPickDateOfEndDialogEvent() {
        return showPickDateOfEndDialog;
    }

    public SingleLiveEvent getShowEndDateContainerEvent() {
        return showEndDateContainer;
    }
    public LiveData<Boolean> getShowConfirmDeleteDialogEvent() {
        return showConfirmDeleteDialog;
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
        }
        showPickDateOfStartDialog.callWithArgument(calendar);
    }

    public void pickDateOfExpirationClicked() {
        Calendar calendar = Calendar.getInstance();
        long dateOfExpiration = debtRepository.getCachedDebtPOJO().getDebt().getDateOfExpiration();
        if (dateOfExpiration != 0) {
            calendar.setTimeInMillis(dateOfExpiration);
        }
        showPickDateOfExpirationDialog.callWithArgument(calendar);
    }

    public void pickDateOfEndClicked() {
        Calendar calendar = Calendar.getInstance();
        long dateOfEnd = debtRepository.getCachedDebtPOJO().getDebt().getDateOfEnd();
        if (dateOfEnd != 0) {
            calendar.setTimeInMillis(dateOfEnd);
        }
        showPickDateOfEndDialog.callWithArgument(calendar);
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

    public void saveButtonClicked() {
        //возможно нужно будет сделать проверки на пустые поля?
        if (debtRepository.getCachedDebtPOJO().getDebt().getId() != 0) {
            updateDebt();
        } else {
            insertDebt();
        }
    }

    private void updateDebt() {
        disposables.add(debtRepository.updateDebt(debtRepository.getCachedDebtPOJO().getDebt())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        showToast.callWithArgument("Запись успешно обновлена!");
                        debtRepository.clearDebtPOJOCache();
                        navigateToPreviousScreen.call();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast.callWithArgument("Ошибка обновления записи!");
                    }
                }));
    }

    private void insertDebt() {
        disposables.add(debtRepository.insertDebt(debtRepository.getCachedDebtPOJO().getDebt())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        showToast.callWithArgument("Запись успешно сохранена!");
                        debtRepository.clearDebtPOJOCache();
                        navigateToPreviousScreen.call();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast.callWithArgument("Ошибка сохранения записи!");
                    }
                }));
    }

    public void deleteButtonClicked() {
        if (debtRepository.getCachedDebtPOJO().getDebt().getId() != 0) {
            showConfirmDeleteDialog.postValue(true);
        } else {
            showToast.callWithArgument("Такой записи не существует!");
        }
    }

    public void canceledDelete() {
        showConfirmDeleteDialog.postValue(false);
    }

    public void confirmedDebtDelete() {
        if (debtRepository.getCachedDebtPOJO().getDebt().getId() != 0) {
            deleteDebt();
            showConfirmDeleteDialog.postValue(false);
        }
    }

    private void deleteDebt() {
        disposables.add(debtRepository.deleteDebt(debtRepository.getCachedDebtPOJO().getDebt())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        showToast.callWithArgument("Запись успешно удалена!");
                        debtRepository.clearDebtPOJOCache();
                        navigateToPreviousScreen.call();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast.callWithArgument("Ошибка удаления записи!");
                    }
                }));
    }

    public void changedTextDebtAmount(String allText) {
        if (allText.trim().equals("")) allText = "0.0";
        debtRepository.getCachedDebtPOJO().getDebt().setAmount(Double.valueOf(allText.trim()));
    }

    public void changedTextDebtDescription(final String allText) {
        debtRepository.getCachedDebtPOJO().getDebt().setDescription(allText.trim());
    }

    public void isReturnedCheckChanged(boolean isChecked) {
        DebtPOJO cachedDebtPojo = debtRepository.getCachedDebtPOJO();
        cachedDebtPojo.getDebt().setReturned(isChecked);
        showEndDateContainer.callWithArgument(isChecked);
        if (isChecked) {
            Calendar calendar = Calendar.getInstance();
            cachedDebtPojo.getDebt().setDateOfEnd(calendar.getTimeInMillis());
        } else {
            cachedDebtPojo.getDebt().setDateOfEnd(Calendar.getInstance().getTimeInMillis() * 2); // ставим значительно большую дату, чтобы при сортировке незавершенные были в конце
        }
        liveDataCachedDebtPOJO.setValue(cachedDebtPojo);
    }

    public void amIBorrowerClicked(boolean amIBorrower) {
        DebtPOJO cachedDebtPojo = debtRepository.getCachedDebtPOJO();
        cachedDebtPojo.getDebt().setIAmBorrower(amIBorrower);
        liveDataCachedDebtPOJO.setValue(cachedDebtPojo);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

}
