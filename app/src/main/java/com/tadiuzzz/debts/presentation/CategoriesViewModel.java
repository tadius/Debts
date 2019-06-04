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
public class CategoriesViewModel extends AndroidViewModel {

    DebtRepository debtRepository;

    public CategoriesViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
    }

    public Flowable<List<Category>> getCategories(){
        return debtRepository.getAllCategories();
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
