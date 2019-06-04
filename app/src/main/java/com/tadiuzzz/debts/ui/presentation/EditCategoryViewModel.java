package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditCategoryViewModel extends AndroidViewModel {

    DebtRepository debtRepository;

    public EditCategoryViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
    }

    public Maybe<Category> getCategoryByID(int id){
        return debtRepository.getCategoryByID(id);
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
