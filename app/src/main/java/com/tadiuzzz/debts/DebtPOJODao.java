package com.tadiuzzz.debts;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.tadiuzzz.debts.entity.DebtPOJO;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
@Dao
public interface DebtPOJODao {

    @Transaction @Query("SELECT * FROM debt_table ORDER BY dateOfStart") //в обратном порядке добавить в конце DESC
    Flowable<List<DebtPOJO>> getAllDebtPOJOs();

}
