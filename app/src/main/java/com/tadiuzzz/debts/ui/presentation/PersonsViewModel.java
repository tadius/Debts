package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class PersonsViewModel extends ViewModel {

    CompositeDisposable disposables;

    private DebtRepository debtRepository;
    private final SingleLiveEvent<Person> navigateToEditPersonScreen = new SingleLiveEvent<>();
    private final SingleLiveEvent<DebtPOJO> navigateToPreviousScreen = new SingleLiveEvent<>();

    private MutableLiveData<List<Person>> listOfPersons = new MutableLiveData<>();

    @Inject
    public PersonsViewModel(DebtRepository debtRepository) {
        this.debtRepository = debtRepository;

        disposables = new CompositeDisposable();

        loadAllPersons();
    }

    private void loadAllPersons() {
        disposables.add(debtRepository.getAllPersons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Person>>() {
                    @Override
                    public void onNext(List<Person> persons) {
                        listOfPersons.setValue(persons);
                    }
                    @Override
                    public void onError(Throwable t) {}

                    @Override
                    public void onComplete() {}
                }));
    }

    public LiveData<List<Person>> getPersons() {
        return listOfPersons;
    }

    public SingleLiveEvent<DebtPOJO> getNavigateToPreviousScreenEvent() {
        return navigateToPreviousScreen;
    }

    public SingleLiveEvent getNavigateToEditPersonScreenEvent() {
        return navigateToEditPersonScreen;
    }

    public void clickedOnPerson(Person person, DebtPOJO debtPojo) {
        if (debtPojo != null) {
            debtPojo = putPersonToDebtPojo(person, debtPojo);
            navigateToPreviousScreen.callWithArgument(debtPojo);
        } else {
            navigateToEditPersonScreen.callWithArgument(person);
        }
    }

    private DebtPOJO putPersonToDebtPojo(Person person, DebtPOJO debtPOJO) {
        List<Person> persons = new ArrayList<>();
        persons.add(person);
        debtPOJO.setPerson(persons);
        debtPOJO.getDebt().setPersonId(person.getId());
        return debtPOJO;
    }

    public void onAddButtonClick() {
        navigateToEditPersonScreen.call();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
