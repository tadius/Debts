package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Person;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditPersonViewModel extends AndroidViewModel {

    DebtRepository debtRepository;

    public EditPersonViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
    }

    public Maybe<Person> getPersonByID(int id){
        return debtRepository.getPersonByID(id);
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
