package com.tadiuzzz.debts;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tadiuzzz.debts.entity.Debt;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
@Dao
public interface DebtDao {

    @Insert
    Completable insert(Debt debt);

    @Update
    Completable update(Debt debt);

    @Delete
    Completable delete(Debt debt);

    @Query("DELETE FROM debt_table")
    Completable deleteAllDebts();

    @Query("SELECT * FROM debt_table ORDER BY dateOfStart DESC")
    Flowable<List<Debt>> getAllDebts();

    @Query("SELECT * FROM debt_table WHERE id = :id")
    Maybe<Debt> getDebtById(int id);

}
