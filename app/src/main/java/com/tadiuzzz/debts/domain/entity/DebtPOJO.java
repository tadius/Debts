package com.tadiuzzz.debts.domain.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
public class DebtPOJO implements Parcelable {
    @Embedded
    private Debt debt;

    @Relation(parentColumn = "personId", entityColumn = "id")
    private List<Person> person;

    @Relation(parentColumn = "categoryId", entityColumn = "id")
    private List<Category> category;

    public DebtPOJO() {

    }

    public DebtPOJO(Debt debt, Person person, Category category) {
        this.debt = debt;
        this.person = new ArrayList<>();
        this.person.add(person);
        this.category = new ArrayList<>();
        this.category.add(category);
    }

    public Person getDebtPerson() {
        if (!person.isEmpty()) {
            return person.get(0);
        } else {
            return new Person();
        }

    }

    public Category getDebtCategory() {
        if (!category.isEmpty()) {
            return category.get(0);
        } else {
            return new Category();
        }
    }

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

    /*
     * Parcelable
     */
    @Ignore
    protected DebtPOJO(Parcel in) {
        debt = in.readParcelable(Debt.class.getClassLoader());
        person = in.createTypedArrayList(Person.CREATOR);
        category = in.createTypedArrayList(Category.CREATOR);
    }

    public static final Creator<DebtPOJO> CREATOR = new Creator<DebtPOJO>() {
        @Override
        public DebtPOJO createFromParcel(Parcel in) {
            return new DebtPOJO(in);
        }

        @Override
        public DebtPOJO[] newArray(int size) {
            return new DebtPOJO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(debt, flags);
        dest.writeTypedList(person);
        dest.writeTypedList(category);
    }
}
