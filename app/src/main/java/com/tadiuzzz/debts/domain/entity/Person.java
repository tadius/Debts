package com.tadiuzzz.debts.domain.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
@Entity(tableName = "person_table")
public class Person {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    @Ignore
    public Person() {
        this.name = "Неизвестная персона";
    }

    public Person(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
