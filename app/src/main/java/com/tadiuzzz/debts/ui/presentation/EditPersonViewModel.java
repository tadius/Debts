package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditPersonViewModel extends AndroidViewModel {

    DebtRepository debtRepository;

    private MutableLiveData<Person> loadedLiveDataPerson = new MutableLiveData<>();

    private final SingleLiveEvent<String> showToast = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();

    public EditPersonViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
    }

    public void gotPickedPerson(int personId) { //Подгружаем из базы выбранную персону в объект LiveData
        debtRepository.getPersonByID(personId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<Person>() {
                    @Override
                    public void onSuccess(Person person) {
                        loadedLiveDataPerson.setValue(person);
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
    }

    public LiveData<Person> getPerson() {
        return loadedLiveDataPerson;
    }

    public SingleLiveEvent getShowToastEvent() {
        return showToast;
    }

    public SingleLiveEvent getNavigateToPreviousScreenEvent() {
        return navigateToPreviousScreen;
    }

    public void saveButtonClicked(String enteredPersonFirstName, String enteredPersonSecondName) {
        if (loadedLiveDataPerson.getValue() != null) {
            if (loadedLiveDataPerson.getValue().getFirstName().equals(enteredPersonFirstName) && loadedLiveDataPerson.getValue().getSecondName().equals(enteredPersonSecondName)) {
                showToast.callWithArgument("Имя и фамилия не изменились!");
            } else if (enteredPersonFirstName.isEmpty() || enteredPersonSecondName.isEmpty()) {
                showToast.callWithArgument("Введите имя и фамилию!");
            } else {
                loadedLiveDataPerson.getValue().setFirstName(enteredPersonFirstName);
                loadedLiveDataPerson.getValue().setSecondName(enteredPersonSecondName);
                debtRepository.updatePerson(loadedLiveDataPerson.getValue())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                showToast.callWithArgument("Персона обновлена!");
                                navigateToPreviousScreen.call();
                            }

                            @Override
                            public void onError(Throwable e) {
                                showToast.callWithArgument("Ошибка при сохранении!");
                            }
                        });
            }
        } else {
            if (!enteredPersonFirstName.isEmpty() && !enteredPersonSecondName.isEmpty()) {
                debtRepository.insertPerson(new Person(enteredPersonFirstName, enteredPersonSecondName))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                showToast.callWithArgument("Персона сохранена!");
                                navigateToPreviousScreen.call();
                            }

                            @Override
                            public void onError(Throwable e) {
                                showToast.callWithArgument("Ошибка при сохранении!");
                            }
                        });
            } else {
                showToast.callWithArgument("Введите имя и фамилию!");
            }
        }
    }

    public void deleteButtonClicked() {
        if (loadedLiveDataPerson.getValue() != null) {
            debtRepository.deletePerson(loadedLiveDataPerson.getValue())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            showToast.callWithArgument("Персона удалена!");
                            navigateToPreviousScreen.call();
                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast.callWithArgument("Ошибка при удалении!");
                        }
                    });
        } else {
            showToast.callWithArgument("Такой персоны не существует!");
        }
    }

}
