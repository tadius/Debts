package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class PersonsViewModel extends AndroidViewModel {

    DebtRepository debtRepository;
    private final SingleLiveEvent<Person> navigateToEditPersonScreen = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();

    private MutableLiveData<List<Person>> liveDataPersons = new MutableLiveData<>();

    // ====== подписки для навигации

    public SingleLiveEvent navigateToPreviousScreen(){
        return navigateToPreviousScreen;
    }

    public SingleLiveEvent navigateToEditPersonScreen(){
        return navigateToEditPersonScreen;
    }

    // ===================================

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
                        liveDataPersons.setValue(persons);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public LiveData<List<Person>> getLiveDataPersons() { //Предоставляем объект LiveData View для подписки
        return liveDataPersons;
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
