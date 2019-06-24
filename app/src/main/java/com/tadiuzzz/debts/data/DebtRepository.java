package com.tadiuzzz.debts.data;

import android.app.Application;

import com.tadiuzzz.debts.data.db.CategoryDao;
import com.tadiuzzz.debts.data.db.DebtDao;
import com.tadiuzzz.debts.data.db.DebtPOJODao;
import com.tadiuzzz.debts.data.db.DebtsDatabase;
import com.tadiuzzz.debts.data.db.PersonDao;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Debt;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.domain.entity.Person;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

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
    private Flowable<List<DebtPOJO>> allDebtPOJOsIAmCreditor;
    private Flowable<List<DebtPOJO>> allDebtPOJOsIAmBorrower;

    private DebtPOJO cachedDebtPOJO;

    public DebtRepository(Application application) {
        DebtsDatabase database = DebtsDatabase.getInstance(application);
        debtDao = database.debtDao();
        personDao = database.personDao();
        categoryDao = database.categoryDao();
        debtPOJODao = database.debtWithPersonAndCategoryDao();

        allDebtPOJOs = debtPOJODao.getAllDebtPOJOs();
        allDebtPOJOsIAmCreditor = debtPOJODao.getAllDebtPOJOsIAmCreditor();
        allDebtPOJOsIAmBorrower = debtPOJODao.getAllDebtPOJOsIAmBorrower();

        allPersons = personDao.getAllPersons();
        allCategories = categoryDao.getAllCategories();

        cachedDebtPOJO = CacheEditing.getInstance().getCachedDebtPOJO();
    }

    public Flowable<List<DebtPOJO>> getAllDebtPOJOs() {
        return allDebtPOJOs;
    }

    public Flowable<List<DebtPOJO>> getAllDebtPOJOsIAmCreditor() {
        return allDebtPOJOsIAmCreditor;
    }

    public Flowable<List<DebtPOJO>> getAllDebtPOJOsIAmBorrower() {
        return allDebtPOJOsIAmBorrower;
    }

    public Flowable<List<Person>> getAllPersons() {
        return allPersons;
    }

    public Flowable<List<Category>> getAllCategories() {
        return allCategories;
    }

    public Maybe<DebtPOJO> getDebtPOJOByID(int id) {
        return debtPOJODao.getDebtPOJOById(id);
    }

    public Maybe<Category> getCategoryByID(int id) {
        return categoryDao.getCategoryById(id);
    }

    public Maybe<Person> getPersonByID(int id) {
        return personDao.getPersonById(id);
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

    public DebtPOJO getCachedDebtPOJO(){
        return cachedDebtPOJO;
    }

    public void putDebtPOJOtoCache(DebtPOJO debtPOJO){
        CacheEditing.getInstance().putDebtPOJOToCache(debtPOJO);
    }

    public void clearDebtPOJOCache(){
        CacheEditing.getInstance().clearCachedDebtPOJO();
    }

}
