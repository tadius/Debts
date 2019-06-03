package com.tadiuzzz.debts.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
public class DebtWithPersonAndCategory {
    @Embedded
    private Debt debt;

    @Relation(parentColumn = "personId", entityColumn = "id")
    private List<Person> person;

    @Relation(parentColumn = "categoryId", entityColumn = "id")
    private List<Category> category;

    public Debt getDebt() {
        return debt;
    }

    public void setDebt(Debt debt) {
        this.debt = debt;
    }

    public List<Person> getPerson() {
        return person;
    }

    public void setPerson(List<Person> person) {
        this.person = person;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }
}
