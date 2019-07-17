package com.tadiuzzz.debts.utils;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Simonov.vv on 25.06.2019.
 */
public class FilterManager {

    private DebtRepository debtRepository;

    private List<Category> filteredCategories;

    @Inject
    public FilterManager(DebtRepository debtRepository) {
        this.debtRepository = debtRepository;
        filteredCategories = new ArrayList<>();
//        filteredCategories = debtRepository.getAllCategories().observeOn()
    }

    public List<Category> getFilteredCategories() {
        return filteredCategories;
    }


    public void setFilteredCategories(List<Category> selectedCategories) {
        filteredCategories = selectedCategories;
    }
}
