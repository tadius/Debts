package com.tadiuzzz.debts;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.tadiuzzz.debts.entity.Debt;
import com.tadiuzzz.debts.entity.DebtWithPersonAndCategory;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
@Dao
public interface DebtWithPersonAndCategoryDao {

    @Transaction @Query("SELECT * FROM debt_table ORDER BY dateOfStart DESC")
    Flowable<List<DebtWithPersonAndCategory>> getAllDebtsWithPersonAndCategory();

}
