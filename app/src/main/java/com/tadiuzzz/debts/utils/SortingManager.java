package com.tadiuzzz.debts.utils;

import com.tadiuzzz.debts.domain.entity.DebtPOJO;

import java.util.Comparator;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static com.tadiuzzz.debts.utils.Constants.SORT_BY_AMOUNT;
import static com.tadiuzzz.debts.utils.Constants.SORT_BY_AMOUNT_DESC;
import static com.tadiuzzz.debts.utils.Constants.SORT_BY_DATE_OF_END;
import static com.tadiuzzz.debts.utils.Constants.SORT_BY_DATE_OF_END_DESC;
import static com.tadiuzzz.debts.utils.Constants.SORT_BY_DATE_OF_EXPIRATION;
import static com.tadiuzzz.debts.utils.Constants.SORT_BY_DATE_OF_EXPIRATION_DESC;
import static com.tadiuzzz.debts.utils.Constants.SORT_BY_DATE_OF_START;
import static com.tadiuzzz.debts.utils.Constants.SORT_BY_DATE_OF_START_DESC;
import static com.tadiuzzz.debts.utils.Constants.SORT_MENU_ICON;
import static com.tadiuzzz.debts.utils.Constants.SORT_MENU_ICON_DESC;

/**
 * Created by Simonov.vv on 27.06.2019.
 */
public class SortingManager {

    private static final SortingManager INSTANCE = new SortingManager();

    private BehaviorSubject<Comparator<DebtPOJO>> changeObservable = BehaviorSubject.create();
    private BehaviorSubject<Integer> changeSortIcon = BehaviorSubject.create();
    private BehaviorSubject<String> changeSortTitle = BehaviorSubject.create();

    private Comparator<DebtPOJO> sortingComparator;

    private int sortBy = 0;

    private Integer sortIcon = SORT_MENU_ICON;
    private String sortTitle = "";

    private SortingManager(){
        refreshSortingComparator();
    }

    public static SortingManager getInstance(){
        return INSTANCE;
    }

    public Integer getSortBy() {
        return sortBy;
    }

    public void setSortBy(int sortBy, String sortTitle) {
        this.sortBy = sortBy;
        this.sortTitle = sortTitle;
        refreshSortingComparator();
    }

    public void refreshSortingComparator() {
        switch (sortBy) {
            case SORT_BY_DATE_OF_START:
                sortingComparator = (debtPOJO1, debtPOJO2) -> (Long.compare(debtPOJO1.getDebt().getDateOfStart(), debtPOJO2.getDebt().getDateOfStart()));
                sortIcon = SORT_MENU_ICON;
                break;
            case SORT_BY_DATE_OF_START_DESC:
                sortingComparator = (debtPOJO1, debtPOJO2) -> (Long.compare(debtPOJO2.getDebt().getDateOfStart(), debtPOJO1.getDebt().getDateOfStart()));
                sortIcon = SORT_MENU_ICON_DESC;
                break;
            case SORT_BY_DATE_OF_EXPIRATION:
                sortingComparator = (debtPOJO1, debtPOJO2) -> (Long.compare(debtPOJO1.getDebt().getDateOfExpiration(), debtPOJO2.getDebt().getDateOfExpiration()));
                sortIcon = SORT_MENU_ICON;
                break;
            case SORT_BY_DATE_OF_EXPIRATION_DESC:
                sortingComparator = (debtPOJO1, debtPOJO2) -> (Long.compare(debtPOJO2.getDebt().getDateOfExpiration(), debtPOJO1.getDebt().getDateOfExpiration()));
                sortIcon = SORT_MENU_ICON_DESC;
                break;
            case SORT_BY_DATE_OF_END:
                sortingComparator = (debtPOJO1, debtPOJO2) -> (Long.compare(debtPOJO1.getDebt().getDateOfEnd(), debtPOJO2.getDebt().getDateOfEnd()));
                sortIcon = SORT_MENU_ICON;
                break;
            case SORT_BY_DATE_OF_END_DESC:
                sortingComparator = (debtPOJO1, debtPOJO2) -> (Long.compare(debtPOJO2.getDebt().getDateOfEnd(), debtPOJO1.getDebt().getDateOfEnd()));
                sortIcon = SORT_MENU_ICON_DESC;
                break;
            case SORT_BY_AMOUNT:
                sortingComparator = (debtPOJO1, debtPOJO2) -> (Double.compare(debtPOJO1.getDebt().getAmount(), debtPOJO2.getDebt().getAmount()));
                sortIcon = SORT_MENU_ICON;
                break;
            case SORT_BY_AMOUNT_DESC:
                sortingComparator = (debtPOJO1, debtPOJO2) -> (Double.compare(debtPOJO2.getDebt().getAmount(), debtPOJO1.getDebt().getAmount()));
                sortIcon = SORT_MENU_ICON_DESC;
                break;
            default:
                sortingComparator = (debtPOJO1, debtPOJO2) -> (Long.compare(debtPOJO1.getDebt().getDateOfExpiration(), debtPOJO2.getDebt().getDateOfExpiration()));
                sortIcon = SORT_MENU_ICON;
                break;
        }
        changeObservable.onNext(sortingComparator);
        changeSortIcon.onNext(sortIcon);
        changeSortTitle.onNext(sortTitle);
    }

    public Observable<Comparator<DebtPOJO>> getComparator() {
        return changeObservable;
    }

    public Observable<Integer> getSortIcon() {
        return changeSortIcon;
    }

    public Observable<String> getSortTitle() {
        return changeSortTitle;
    }

}
