package com.tadiuzzz.debts.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Debt;
import com.tadiuzzz.debts.domain.entity.Person;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
@Database(entities = {Debt.class, Person.class, Category.class}, version = 1, exportSchema = false)
public abstract class DebtsDatabase extends RoomDatabase {

    private static DebtsDatabase inststance;

    public abstract DebtDao debtDao();
    public abstract PersonDao personDao();
    public abstract CategoryDao categoryDao();
    public abstract DebtPOJODao debtWithPersonAndCategoryDao();

    public static synchronized DebtsDatabase getInstance(Context context){
        if (inststance == null) {
            inststance = Room.databaseBuilder(context.getApplicationContext(), DebtsDatabase.class, "debts_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return inststance;
    }

}
