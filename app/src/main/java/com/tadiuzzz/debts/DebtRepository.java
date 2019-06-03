package com.tadiuzzz.debts;

import android.app.Application;
import android.provider.ContactsContract;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;

import com.tadiuzzz.debts.entity.Category;
import com.tadiuzzz.debts.entity.Debt;
import com.tadiuzzz.debts.entity.DebtWithPersonAndCategory;
import com.tadiuzzz.debts.entity.Person;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
public class DebtRepository {

    private DebtDao debtDao;
    private PersonDao personDao;
    private CategoryDao categoryDao;
    private DebtWithPersonAndCategoryDao debtWithPersonAndCategoryDao;

//    private Flowable<List<DebtWithPersonAndCategory>> allDebtsWithPersonAndCategory;
    private Flowable<List<Debt>> allDebts;

    public DebtRepository(Application application) {
        DebtsDatabase database = DebtsDatabase.getInstance(application);
        debtDao = database.debtDao();
        personDao = database.personDao();
        categoryDao = database.categoryDao();
        debtWithPersonAndCategoryDao = database.debtWithPersonAndCategoryDao();

//        allDebtsWithPersonAndCategory = getAllDebts();
        allDebts = getAllDebts();
    }

//    private Flowable<List<DebtWithPersonAndCategory>> getAllDebts() {
//        return debtWithPersonAndCategoryDao.getAllDebtsWithPersonAndCategory();
//    }

    public Flowable<List<Debt>> getAllDebts() {
        return debtDao.getAllDebts();
    }

//    public Completable insert(DebtWithPersonAndCategory debtWithPersonAndCategory) {
//
//        for (Person person : debtWithPersonAndCategory.getPerson()) {
//            insertPerson(person);
//        }
//
//        insertPerson(debtWithPersonAndCategory.getPerson());
//        return Completable.fromAction(() -> debtWithPersonAndCategoryDao.getAllDebtsWithPersonAndCategory())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    public Completable insertDebt(Debt debt){
        return debtDao.insert(debt);
    }

    public Completable updateDebt(Debt debt){
        return debtDao.update(debt);
    }

    public Completable deleteDebt(Debt debt){
        return debtDao.delete(debt);
    }

    public Completable insertPerson(Person person){
        return personDao.insert(person);
    }

    public Completable updatePerson(Person person){
        return personDao.update(person);
    }

    public Completable deletePerson(Person person){
        return personDao.delete(person);
    }

}
