package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class PersonsViewModel extends AndroidViewModel {

    DebtRepository debtRepository;
    private final SingleLiveEvent<Person> navigateToEditPersonScreen = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();

    public PersonsViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
    }

    public Flowable<List<Person>> getPersons(){
        return debtRepository.getAllPersons();
    }

    public Completable insertPerson(Person person){
        return debtRepository.insertPerson(person);
    }

    public Completable updatePerson(Person person){
        return debtRepository.updatePerson(person);
    }

    public Completable deletePerson(Person person){
        return debtRepository.deletePerson(person);
    }

    public SingleLiveEvent navigateToEditPersonScreen(){
        return navigateToEditPersonScreen;
    }

    public SingleLiveEvent navigateToPreviousScreen(){
        return navigateToPreviousScreen;
    }

    public void clickedOnPerson(Person person, boolean isPicking) {
        if(isPicking){
            List<Person> persons = new ArrayList<>();
            persons.add(person);
            putPersonToCache(persons);
            navigateToPreviousScreen.call();
        } else {
            navigateToEditPersonScreen.callWithArgument(person);
        }
    }

    public void putPersonToCache(List<Person> persons){
        debtRepository.getCachedDebtPOJO()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<DebtPOJO>() {

                    @Override
                    public void onSuccess(DebtPOJO debtPOJO) {
                        debtPOJO.setPerson(persons);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
