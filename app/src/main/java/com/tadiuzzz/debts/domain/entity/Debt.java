package com.tadiuzzz.debts.domain.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Simonov.vv on 30.05.2019.
 */
@Entity(tableName = "debt_table")
public class Debt {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private double amount;
    private long dateOfStart;
    private long dateOfEnd;
    private long dateOfExpiration;
    private boolean isReturned;
    private boolean isActive;
    private boolean amIBorrower;
    private int categoryId;
    private int personId;

    @Ignore
    public Debt(){

    }

    public Debt(String description,
                double amount,
                long dateOfStart,
                long dateOfEnd,
                long dateOfExpiration,
                boolean isReturned,
                boolean isActive,
                boolean amIBorrower,
                int categoryId,
                int personId) {

        this.description = description;
        this.amount = amount;
        this.dateOfStart = dateOfStart;
        this.dateOfEnd = dateOfEnd;
        this.dateOfExpiration = dateOfExpiration;
        this.isReturned = isReturned;
        this.isActive = isActive;
        this.amIBorrower = amIBorrower;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getDateOfStart() {
        return dateOfStart;
    }

    public void setDateOfStart(long dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public long getDateOfEnd() {
        return dateOfEnd;
    }

    public void setDateOfEnd(long dateOfEnd) {
        this.dateOfEnd = dateOfEnd;
    }

    public long getDateOfExpiration() {
        return dateOfExpiration;
    }

    public void setDateOfExpiration(long dateOfExpiration) {
        this.dateOfExpiration = dateOfExpiration;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean amIBorrower() {
        return amIBorrower;
    }

    public void setIAmBorrower(boolean amIBorrower) {
        this.amIBorrower = amIBorrower;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}
