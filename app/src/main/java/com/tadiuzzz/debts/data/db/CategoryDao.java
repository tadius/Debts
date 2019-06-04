package com.tadiuzzz.debts.data.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tadiuzzz.debts.domain.entity.Category;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

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

    @Query("SELECT * FROM category_table ORDER BY id")
    Flowable<List<Category>> getAllCategories();

    @Query("SELECT * FROM category_table WHERE id = :id")
    Maybe<Category> getCategoryById(int id);
}
