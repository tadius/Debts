package com.tadiuzzz.debts;

import android.app.Application;

import com.tadiuzzz.debts.entity.Category;
import com.tadiuzzz.debts.entity.Debt;
import com.tadiuzzz.debts.entity.DebtPOJO;
import com.tadiuzzz.debts.entity.Person;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
public class DebtRepository {

    private DebtDao debtDao;
    private PersonDao personDao;
    private CategoryDao categoryDao;
    private DebtPOJODao debtPOJODao;

    private Flowable<List<Person>> allPersons;
    private Flowable<List<Category>> allCategories;
    private Flowable<List<DebtPOJO>> allDebtPOJOs;

    public DebtRepository(Application application) {
        DebtsDatabase database = DebtsDatabase.getInstance(application);
        debtDao = database.debtDao();
        personDao = database.personDao();
        categoryDao = database.categoryDao();
        debtPOJODao = database.debtWithPersonAndCategoryDao();

        allDebtPOJOs = debtPOJODao.getAllDebtPOJOs();
        allPersons = personDao.getAllPersons();
        allCategories = categoryDao.getAllCategories();
    }

    public Flowable<List<DebtPOJO>> getAllDebtPOJOs() {
        return allDebtPOJOs;
    }

    public Flowable<List<Person>> getAllPersons() {
        return allPersons;
    }

    public Flowable<List<Category>> getAllCategories() {
        return allCategories;
    }

    public Maybe<Category> getCategoryByID(int id) {
        return categoryDao.getCategoryById(id);
    }

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

    public Completable insertCategory(Category category){
        return categoryDao.insert(category);
    }

    public Completable updateCategory(Category category){
        return categoryDao.update(category);
    }

    public Completable deleteCategory(Category category){
        return categoryDao.delete(category);
    }

}
