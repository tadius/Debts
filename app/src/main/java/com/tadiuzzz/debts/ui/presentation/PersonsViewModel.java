package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Person;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class PersonsViewModel extends AndroidViewModel {

    DebtRepository debtRepository;

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

}
