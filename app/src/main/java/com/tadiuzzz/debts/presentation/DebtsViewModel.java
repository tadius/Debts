package com.tadiuzzz.debts.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.tadiuzzz.debts.DebtRepository;
import com.tadiuzzz.debts.entity.Category;
import com.tadiuzzz.debts.entity.Debt;
import com.tadiuzzz.debts.entity.DebtPOJO;
import com.tadiuzzz.debts.entity.Person;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class DebtsViewModel extends AndroidViewModel {

    DebtRepository debtRepository;

    public DebtsViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
    }

    public Flowable<List<DebtPOJO>> getDebtPOJOs(){
        return debtRepository.getAllDebtPOJOs();
    }

    public Completable insertDebt(Debt debt){
        return debtRepository.insertDebt(debt);
    }

    public Completable updateDebt(Debt debt){
        return debtRepository.updateDebt(debt);
    }

    public Completable deleteDebt(Debt debt){
        return debtRepository.deleteDebt(debt);
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

    public Completable insertCategory(Category category){
        return debtRepository.insertCategory(category);
    }

    public Completable updateCategory(Category category){
        return debtRepository.updateCategory(category);
    }

    public Completable deleteCategory(Category category){
        return debtRepository.deleteCategory(category);
    }

}
