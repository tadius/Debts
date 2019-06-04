package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Debt;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditDebtViewModel extends AndroidViewModel {

    DebtRepository debtRepository;

    public EditDebtViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
    }

    public Maybe<DebtPOJO> getDebtPOJOByID(int id){
        return debtRepository.getDebtPOJOByID(id);
    }

    public Completable insertDebt(Debt debt){
        return debtRepository.insertDebt(debt);
    }

    public Completable updateDebt(Debt debt){
        return debtRepository.updateDebt(debt);
    }

    public Completable deleteDebt(Debt debt){
        return debtRepository.deleteDebt(debt);
    }

}
