package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class PersonsViewModel extends AndroidViewModel {

    DebtRepository debtRepository;
    private final SingleLiveEvent<Person> navigateToEditPersonScreen = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();

    private MutableLiveData<List<Person>> listOfPersons = new MutableLiveData<>();

    public PersonsViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
        loadAllPersons();
    }

    private void loadAllPersons() {
        debtRepository.getAllPersons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<Person>>() {
                    @Override
                    public void onNext(List<Person> persons) {
                        listOfPersons.setValue(persons);
                    }
                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onComplete() {}
                });
    }

    public LiveData<List<Person>> getPersons() {
        return listOfPersons;
    }

    public SingleLiveEvent getNavigateToPreviousScreenEvent() {
        return navigateToPreviousScreen;
    }

    public SingleLiveEvent getNavigateToEditPersonScreenEvent() {
        return navigateToEditPersonScreen;
    }

    public void clickedOnPerson(Person person, boolean isPicking) {
        if (isPicking) {
            putPersonToCache(person);
            navigateToPreviousScreen.call();
        } else {
            navigateToEditPersonScreen.callWithArgument(person);
        }
    }

    private void putPersonToCache(Person person) {
        List<Person> persons = new ArrayList<>();
        persons.add(person);
        debtRepository.getCachedDebtPOJO().setPerson(persons);
        debtRepository.getCachedDebtPOJO().getDebt().setPersonId(person.getId());
    }

}
