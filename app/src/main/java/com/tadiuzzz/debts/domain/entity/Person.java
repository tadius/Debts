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
    private String firstName;
    private String secondName;

    @Ignore
    public Person() {
        this.firstName = "Неизвестная";
        this.secondName = "персона";
    }

    public Person(String firstName, String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getSecondName();
    }
}
