package com.tadiuzzz.debts.utils;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Person;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Simonov.vv on 25.06.2019.
 */
public class FilterManager {

    private List<Category> allCategories;
    private List<Person> allPersons;

    private List<Category> filteredCategories;
    private List<Person> filteredPersons;
    private boolean showOnlyActive;

    @Inject
    public FilterManager() {
        allCategories = new ArrayList<>();
        filteredCategories = new ArrayList<>();
        filteredPersons = new ArrayList<>();
    }

    public List<Category> getAllCategories() {
        return allCategories;
    }

    public void setAllCategories(List<Category> allCategories) {
        this.allCategories = allCategories;
    }

    public List<Person> getAllPersons() {
        return allPersons;
    }

    public void setAllPersons(List<Person> allPersons) {
        this.allPersons = allPersons;
    }

    public List<Category> getFilteredCategories() {
        return filteredCategories;
    }

    public void setFilteredCategories(List<Category> selectedCategories) {
        filteredCategories = selectedCategories;
    }

    public List<Person> getFilteredPersons() {
        return filteredPersons;
    }

    public void setFilteredPersons(List<Person> selectedPersons) {
        filteredPersons = selectedPersons;
    }

    public boolean isShowOnlyActive() {
        return showOnlyActive;
    }

    public void setShowOnlyActive(boolean showOnlyActive) {
        this.showOnlyActive = showOnlyActive;
    }


}
