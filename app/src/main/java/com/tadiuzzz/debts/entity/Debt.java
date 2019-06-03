package com.tadiuzzz.debts.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
@Entity(tableName = "debt_table")
public class Debt {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private long amount;
    private long dateOfStart;
    private long dateOfEnd;
    private long dateOfExpiration;
    private boolean isReturned;
    private boolean isActive;
    private int categoryId;
    private int personId;

    public Debt(String description,
                long amount,
                long dateOfStart,
                long dateOfEnd,
                long dateOfExpiration,
                boolean isReturned,
                boolean isActive,
                int categoryId,
                int personId) {

        this.description = description;
        this.amount = amount;
        this.dateOfStart = dateOfStart;
        this.dateOfEnd = dateOfEnd;
        this.dateOfExpiration = dateOfExpiration;
        this.isReturned = isReturned;
        this.isActive = isActive;
        this.categoryId = categoryId;
        this.personId = personId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public long getAmount() {
        return amount;
    }

    public long getDateOfStart() {
        return dateOfStart;
    }

    public long getDateOfEnd() {
        return dateOfEnd;
    }

    public long getDateOfExpiration() {
        return dateOfExpiration;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getPersonId() {
        return personId;
    }
}
