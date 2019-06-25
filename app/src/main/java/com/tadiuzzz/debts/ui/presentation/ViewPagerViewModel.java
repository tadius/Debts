package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.tadiuzzz.debts.ui.SingleLiveEvent;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class ViewPagerViewModel extends AndroidViewModel {

    private SingleLiveEvent<Void> navigateToEditDebtScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToPersonsScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToCategoriesScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToBackupRestoreScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> navigateToAboutScreen = new SingleLiveEvent<>();
    private SingleLiveEvent<Void> showFilterDialog = new SingleLiveEvent<>();

    public ViewPagerViewModel(@NonNull Application application) {
        super(application);
    }

    public SingleLiveEvent getNavigateToEditDebtScreenEvent(){
        return navigateToEditDebtScreen;
    }

    public SingleLiveEvent getNavigateToPersonsScreenEvent(){
        return navigateToPersonsScreen;
    }

    public SingleLiveEvent getNavigateToCategoriesScreenEvent(){
        return navigateToCategoriesScreen;
    }

    public SingleLiveEvent getNavigateToBackupRestoreScreenEvent(){
        return navigateToBackupRestoreScreen;
    }
    public SingleLiveEvent getNavigateToAboutScreenEvent(){
        return navigateToAboutScreen;
    }

    public SingleLiveEvent getShowFilterDialogEvent(){
        return showFilterDialog;
    }

    public void clickedOnFilterMenu(){
        showFilterDialog.call();
    }

    public void clickedOnPersonsMenu(){
        navigateToPersonsScreen.call();
    }

    public void clickedOnCategoriesMenu(){
        navigateToCategoriesScreen.call();
    }

    public void clickedOnBackupRestoreMenu(){
        navigateToBackupRestoreScreen.call();
    }

    public void clickedOnAboutMenu(){
        navigateToAboutScreen.call();
    }

    public void clickedOnAddDebt() {
        navigateToEditDebtScreen.call();
    }
}
