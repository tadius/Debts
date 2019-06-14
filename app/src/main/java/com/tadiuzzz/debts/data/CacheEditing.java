package com.tadiuzzz.debts.data;

import com.tadiuzzz.debts.domain.entity.DebtPOJO;

import java.util.Observable;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by Simonov.vv on 13.06.2019.
 */
public class CacheEditing {

    private static final CacheEditing INSTANCE = new CacheEditing();

    private DebtPOJO debtPOJO;

    private CacheEditing(){
        debtPOJO = new DebtPOJO();
    }

    public static CacheEditing getInstance(){
        return INSTANCE;
    }

    public Maybe<DebtPOJO> getCachedDebtPOJO() {
        return Maybe.just(debtPOJO);
    }

    public void putDebtPOJOToCache(DebtPOJO debtPOJO) {
        this.debtPOJO = debtPOJO;
    }

    public void clearCachedDebtPOJO(){
        this.debtPOJO = new DebtPOJO();
    }
}
