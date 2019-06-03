package com.tadiuzzz.debts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.tadiuzzz.debts.entity.Debt;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class DebtsViewModel extends AndroidViewModel {

    DebtRepository debtRepository;

    public DebtsViewModel(@NonNull Application application) {
        super(application);
        debtRepository = new DebtRepository(application);
    }

    public Flowable<List<Debt>> getDebts(){
        return debtRepository.getAllDebts();
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
