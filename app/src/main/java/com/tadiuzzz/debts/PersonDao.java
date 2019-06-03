package com.tadiuzzz.debts;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tadiuzzz.debts.entity.Debt;
import com.tadiuzzz.debts.entity.Person;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
@Dao
public interface PersonDao {

    @Insert
    Completable insert(Person person);

    @Update
    Completable update(Person person);

    @Delete
    Completable delete(Person person);

    @Query("DELETE FROM person_table")
    Completable deleteAllPersons();

    @Query("SELECT * FROM person_table ORDER BY id")
    Flowable<List<Person>> getAllPersons();

    @Query("SELECT * FROM person_table WHERE id = :id")
    Maybe<Person> getPersonById(int id);

}
