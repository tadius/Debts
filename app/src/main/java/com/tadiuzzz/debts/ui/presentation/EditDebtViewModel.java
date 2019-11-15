package com.tadiuzzz.debts.ui.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Debt;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private final SingleLiveEvent<DebtPOJO> navigateToPersonsList = new SingleLiveEvent<>();
    private final SingleLiveEvent<DebtPOJO> navigateToCategoryList = new SingleLiveEvent<>();
    private final SingleLiveEvent<String> showToast = new SingleLiveEvent<>();

    private final SingleLiveEvent<Calendar> showPickDateOfStartDialog = new SingleLiveEvent<>();
    private final SingleLiveEvent<Calendar> showPickDateOfExpirationDialog = new SingleLiveEvent<>();
    private final SingleLiveEvent<Calendar> showPickDateOfEndDialog = new SingleLiveEvent<>();
    private final MutableLiveData<Boolean> showConfirmDeleteDialog = new MutableLiveData<>();

    private final SingleLiveEvent<Boolean> showEndDateContainer = new SingleLiveEvent<>();

    private MutableLiveData<DebtPOJO> tempDebtPojo = new MutableLiveData<>();

    @Inject
    public EditDebtViewModel(DebtRepository debtRepository) {
        this.debtRepository = debtRepository;

        disposables = new CompositeDisposable();

    }

    public LiveData<DebtPOJO> getCachedDebtPOJO() {
        return tempDebtPojo;
    }

    public SingleLiveEvent getNavigateToPreviousScreenEvent() {
        return navigateToPreviousScreen;
    }

    public SingleLiveEvent<DebtPOJO> getNavigateToPickPersonScreenEvent() {
        return navigateToPersonsList;
    }

    public SingleLiveEvent<DebtPOJO> getNavigateToPickCategoryScreenEvent() {
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
        navigateToPersonsList.callWithArgument(tempDebtPojo.getValue());
    }

    public void pickCategoryClicked() {
        navigateToCategoryList.callWithArgument(tempDebtPojo.getValue());
    }

    public void pickDateOfStartClicked() {
        Calendar calendar = Calendar.getInstance();
        long dateOfStart = tempDebtPojo.getValue().getDebt().getDateOfStart();
        if (dateOfStart != 0) {
            calendar.setTimeInMillis(dateOfStart);
        }
        showPickDateOfStartDialog.callWithArgument(calendar);
    }

    public void pickDateOfExpirationClicked() {
        Calendar calendar = Calendar.getInstance();
        long dateOfExpiration = tempDebtPojo.getValue().getDebt().getDateOfExpiration();
        if (dateOfExpiration != 0) {
            calendar.setTimeInMillis(dateOfExpiration);
        }
        showPickDateOfExpirationDialog.callWithArgument(calendar);
    }

    public void pickDateOfEndClicked() {
        Calendar calendar = Calendar.getInstance();
        long dateOfEnd = tempDebtPojo.getValue().getDebt().getDateOfEnd();
        if (dateOfEnd != 0) {
            calendar.setTimeInMillis(dateOfEnd);
        }
        showPickDateOfEndDialog.callWithArgument(calendar);
    }

    public void pickedDate(int year, int month, int day, int typeOfDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        switch (typeOfDate) {
            case TYPE_OF_DATE_START:
                tempDebtPojo.getValue().getDebt().setDateOfStart(calendar.getTimeInMillis());
                break;
            case TYPE_OF_DATE_EXPIRATION:
                tempDebtPojo.getValue().getDebt().setDateOfExpiration(calendar.getTimeInMillis());
                break;
            case TYPE_OF_DATE_END:
                tempDebtPojo.getValue().getDebt().setDateOfEnd(calendar.getTimeInMillis());
                break;
        }

        tempDebtPojo.setValue(tempDebtPojo.getValue());
    }

    public void saveButtonClicked() {
        //возможно нужно будет сделать проверки на пустые поля?
        if (tempDebtPojo.getValue().getDebt().getId() != 0) {
            updateDebt();
        } else {
            insertDebt();
        }
    }

    private void updateDebt() {
        disposables.add(debtRepository.updateDebt(tempDebtPojo.getValue().getDebt())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        showToast.callWithArgument("Запись успешно обновлена!");
                        navigateToPreviousScreen.call();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast.callWithArgument("Ошибка обновления записи!");
                    }
                }));
    }

    private void insertDebt() {
        disposables.add(debtRepository.insertDebt(tempDebtPojo.getValue().getDebt())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        showToast.callWithArgument("Запись успешно сохранена!");
                        navigateToPreviousScreen.call();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast.callWithArgument("Ошибка сохранения записи!");
                    }
                }));
    }

    public void deleteButtonClicked() {
        if (tempDebtPojo.getValue().getDebt().getId() != 0) {
            showConfirmDeleteDialog.postValue(true);
        } else {
            showToast.callWithArgument("Такой записи не существует!");
        }
    }

    public void canceledDelete() {
        showConfirmDeleteDialog.postValue(false);
    }

    public void confirmedDebtDelete() {
        if (tempDebtPojo.getValue().getDebt().getId() != 0) {
            deleteDebt();
            showConfirmDeleteDialog.postValue(false);
        }
    }

    private void deleteDebt() {
        disposables.add(debtRepository.deleteDebt(tempDebtPojo.getValue().getDebt())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        showToast.callWithArgument("Запись успешно удалена!");
//                        debtRepository.clearDebtPOJOCache(); - нужно ли очищать объект во вьюмодели, если она уничтожится?
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
        tempDebtPojo.getValue().getDebt().setAmount(Double.valueOf(allText.trim()));
    }

    public void changedTextDebtDescription(final String allText) {
        tempDebtPojo.getValue().getDebt().setDescription(allText.trim());
    }

    public void isReturnedCheckChanged(boolean isChecked) {
        tempDebtPojo.getValue().getDebt().setReturned(isChecked);
        showEndDateContainer.callWithArgument(isChecked);
        if (isChecked) {
            Calendar calendar = Calendar.getInstance();
            tempDebtPojo.getValue().getDebt().setDateOfEnd(calendar.getTimeInMillis());
        } else {
            tempDebtPojo.getValue().getDebt().setDateOfEnd(Calendar.getInstance().getTimeInMillis() * 2); // ставим значительно большую дату, чтобы при сортировке незавершенные были в конце
        }
        tempDebtPojo.setValue(tempDebtPojo.getValue());
    }

    public void amIBorrowerClicked(boolean amIBorrower) {
        tempDebtPojo.getValue().getDebt().setAmIBorrower(amIBorrower);
        tempDebtPojo.setValue(tempDebtPojo.getValue()); //уведомить датабайндинг, что данные в объекте поменялись
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public void clearCacheEditing() {
        tempDebtPojo.setValue(null);
    }

    public void setTempDebtPojo(DebtPOJO debtPojo) {
        tempDebtPojo.setValue(debtPojo);
    }

    public void createEmptyTempDebtPojo() {
        DebtPOJO debtPOJO = new DebtPOJO();
        debtPOJO.setDebt(new Debt());

        debtPOJO.getDebt().setDateOfStart(Calendar.getInstance().getTimeInMillis());
        debtPOJO.getDebt().setDateOfExpiration(Calendar.getInstance().getTimeInMillis());
        debtPOJO.getDebt().setDateOfEnd(Calendar.getInstance().getTimeInMillis() * 2);

        List<Category> categories = new ArrayList<>();
        Category category = new Category();
        categories.add(category);
        debtPOJO.setCategory(categories);

        List<Person> persons = new ArrayList<>();
        Person person = new Person();
        persons.add(person);
        debtPOJO.setPerson(persons);

        tempDebtPojo.setValue(debtPOJO);
    }
}
