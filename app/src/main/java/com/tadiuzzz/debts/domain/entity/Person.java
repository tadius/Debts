package com.tadiuzzz.debts.domain.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
@Entity(tableName = "person_table")
public class Person implements Parcelable {
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

    /*
     * Parcelable
     */

    @Ignore
    protected Person(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
