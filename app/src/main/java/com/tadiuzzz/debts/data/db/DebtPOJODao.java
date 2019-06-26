package com.tadiuzzz.debts.data.db;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.tadiuzzz.debts.domain.entity.Debt;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
@Dao
public interface DebtPOJODao {

    @Transaction @Query("SELECT * FROM debt_table ORDER BY dateOfEnd") //в обратном порядке добавить в конце DESC
    Flowable<List<DebtPOJO>> getAllDebtPOJOs();

    @Transaction @Query("SELECT * FROM debt_table WHERE id = :id")
    Maybe<DebtPOJO> getDebtPOJOById(int id);

}
