package com.tadiuzzz.debts.data;

import com.tadiuzzz.debts.domain.entity.DebtPOJO;

/**
 * Created by Simonov.vv on 13.06.2019.
 */
public class CacheEditing {

    private static final CacheEditing INSTANCE = new CacheEditing();

    private DebtPOJO debtPOJO;

    private CacheEditing(){}

    public static CacheEditing getInstance(){
        return INSTANCE;
    }

    public DebtPOJO getCachedDebtPOJO() {
        return debtPOJO;
    }

    public void putDebtPOJOToCache(DebtPOJO debtPOJO) {
        this.debtPOJO = debtPOJO;
    }

    public void clearCachedDebtPOJO(){
        this.debtPOJO = null;
    }
}
