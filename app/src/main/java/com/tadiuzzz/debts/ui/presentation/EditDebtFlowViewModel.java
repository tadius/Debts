package com.tadiuzzz.debts.ui.presentation;

import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.domain.entity.DebtPOJO;

public class EditDebtFlowViewModel extends ViewModel {

    private DebtPOJO tempDebtPojo = new DebtPOJO();


    public DebtPOJO getTempDebtPojo() {
        return tempDebtPojo;
    }

    public void setTempDebtPojo(DebtPOJO tempDebtPojo) {
        this.tempDebtPojo = tempDebtPojo;
    }


}
