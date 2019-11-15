package com.tadiuzzz.debts.ui.presentation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Debt;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.domain.entity.Person;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by Simonov.vv on 25.09.2019.
 */
public class SharedViewModel extends ViewModel {

    private MutableLiveData<DebtPOJO> tempDebtPojo = new MutableLiveData<>();

    @Inject
    public SharedViewModel() {
        clearTempDebtPojo();
    }

    public MutableLiveData<DebtPOJO> getTempDebtPojo() {
        return tempDebtPojo;
    }

    public void setTempDebtPojo(DebtPOJO debtPojo) {
        tempDebtPojo.setValue(debtPojo);
    }

    public void clearTempDebtPojo() {
        DebtPOJO debtPOJO = new DebtPOJO();
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

        tempDebtPojo.setValue(debtPOJO);
    }
}