package com.tadiuzzz.debts.data;

import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Debt;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.domain.entity.Person;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by Simonov.vv on 13.06.2019.
 */
public class CacheEditing {

    private DebtPOJO debtPOJO;

    @Inject
    public CacheEditing(){
        // Создаем пустой объект кэша, чтобы не было NPE при заполнении полей до выбора категории, персоны
        clearCachedDebtPOJO();
    }

    public DebtPOJO getCachedDebtPOJO() {
        return debtPOJO;
    }

    public void putDebtPOJOToCache(DebtPOJO debtPOJO) {
        this.debtPOJO = debtPOJO;
    }

    public void clearCachedDebtPOJO(){
        debtPOJO = new DebtPOJO();
        debtPOJO.setDebt(new Debt());

        debtPOJO.getDebt().setDateOfStart(Calendar.getInstance().getTimeInMillis());
        debtPOJO.getDebt().setDateOfExpiration(Calendar.getInstance().getTimeInMillis());
        debtPOJO.getDebt().setDateOfEnd(Calendar.getInstance().getTimeInMillis() * 2);

        List<Category> categories = new ArrayList<>();
        Category category = new Category();
        categories.add(category);
        debtPOJO.setCategory(categories);

        List<Person> persons = new ArrayList<>();
        Person person = new Person();
        persons.add(person);
        debtPOJO.setPerson(persons);
    }
}
