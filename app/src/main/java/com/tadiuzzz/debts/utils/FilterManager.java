package com.tadiuzzz.debts.utils;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * Created by Simonov.vv on 25.06.2019.
 */
public class FilterManager {

    private static final FilterManager INSTANCE = new FilterManager();
    private BehaviorSubject<List<Category>> changeListOfCategories = BehaviorSubject.create();
    private  DebtRepository repository;

    public static FilterManager getInstance(){
        return INSTANCE;
    }

    public void refreshListOfCategories() {
//        DebtRepository
//        changeListOfCategories.onNext(sortTitle);
    }

    public Observable<List<Category>> getListOfCategories() {
        return changeListOfCategories;
    }



}
