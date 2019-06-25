package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditPersonViewModel extends AndroidViewModel {

    DebtRepository debtRepository;

    private final SingleLiveEvent<String> showToast = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();

    private MutableLiveData<Person> loadedLiveDataPerson = new MutableLiveData<>();


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

    public void saveButtonClicked(String enteredPersonName) {
        if (loadedLiveDataPerson.getValue() != null) {
            if (loadedLiveDataPerson.getValue().getName().equals(enteredPersonName)) {
                showToast.callWithArgument("Имя не изменилось!");
            } else if (enteredPersonName.isEmpty()) {
                showToast.callWithArgument("Введите имя!");
            } else {
                loadedLiveDataPerson.getValue().setName(enteredPersonName);
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
            if (!enteredPersonName.isEmpty()) {
                debtRepository.insertPerson(new Person(enteredPersonName))
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
