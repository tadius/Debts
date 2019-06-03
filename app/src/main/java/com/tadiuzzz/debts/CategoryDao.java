package com.tadiuzzz.debts;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tadiuzzz.debts.entity.Category;
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
public interface CategoryDao {

    @Insert
    Completable insert(Category category);

    @Update
    Completable update(Category category);

    @Delete
    Completable delete(Category category);

    @Query("DELETE FROM category_table")
    Completable deleteAllCategories();

    @Query("SELECT * FROM category_table ORDER BY id DESC")
    Flowable<List<Category>> getAllCategories();

    @Query("SELECT * FROM category_table WHERE id = :id")
    Maybe<Category> getCategoryById(int id);
}
