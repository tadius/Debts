package com.tadiuzzz.debts.ui.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.MenuPopupWindow;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.adapter.ViewPagerAdapter;
import com.tadiuzzz.debts.ui.presentation.ViewModelProviderFactory;
import com.tadiuzzz.debts.ui.presentation.ViewPagerViewModel;
import com.tadiuzzz.debts.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerFragment;

import static android.app.AlertDialog.*;
import static com.tadiuzzz.debts.utils.Constants.*;

public class ViewPagerFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    private ViewPagerViewModel viewModel;

    private ViewPagerAdapter viewPagerAdapter;

    private Menu menu;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.fbAddDebt)
    FloatingActionButton fbAddDebt;

    @OnClick(R.id.fbAddDebt)
    void onAddButtonClick() {
        viewModel.clickedOnAddDebt();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        setHasOptionsMenu(true);

        viewModel = ViewModelProviders.of(this, providerFactory).get(ViewPagerViewModel.class);

        ButterKnife.bind(this, view);

        setupViewPager();

        subscribeOnDialogsEvents();

        subscribeOnNavigationEvents();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void subscribeOnTitleChangeEvent() {
        //убираем тень между actionbar и tablayout
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);

        viewModel.getSortMenuCheckedItem().observe(getViewLifecycleOwner(), itemId -> menu.findItem(itemId).setChecked(true));
        viewModel.getSortMenuTitle().observe(getViewLifecycleOwner(), title -> menu.findItem(R.id.menu_sort).setTitle(title));
        viewModel.getSortMenuIcon().observe(getViewLifecycleOwner(), icon -> menu.findItem(R.id.menu_sort).setIcon(icon));

        viewModel.getFilterMenuIcon().observe(getViewLifecycleOwner(), icon -> menu.findItem(R.id.menu_filter).setIcon(icon));

        viewModel.getIsShowOnlyActive().observe(getViewLifecycleOwner(), isChecked -> menu.findItem(R.id.filter_active).setChecked(isChecked));

    }

    private void subscribeOnNavigationEvents() {
        viewModel.getNavigateToEditDebtScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_viewPagerFragment_to_editDebtFragment));
        viewModel.getNavigateToPersonsScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_viewPagerFragment_to_personsFragment));
        viewModel.getNavigateToCategoriesScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_viewPagerFragment_to_categoriesFragment));
        viewModel.getNavigateToBackupRestoreScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_viewPagerFragment_to_backupRestoreFragment));
        viewModel.getNavigateToAboutScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_viewPagerFragment_to_aboutAppFragment));
    }

    private void subscribeOnDialogsEvents() {
        viewModel.getShowFilterCategoryDialogEvent().observe(getViewLifecycleOwner(), categoryBooleanPair -> {
            if (categoryBooleanPair.second) showFilterCategoryDialog(categoryBooleanPair.first);
        });
        viewModel.getShowFilterPersonDialogEvent().observe(getViewLifecycleOwner(), personBooleanPair -> {
            if (personBooleanPair.second) showFilterPersonDialog(personBooleanPair.first);
        });
    }

    private void showFilterCategoryDialog(List<Category> filteredCategories) {

        viewModel.getListOfCategories().observe(getViewLifecycleOwner(), listOfCategories -> {

            ArrayList<Category> selectedCategories = new ArrayList<>();
            String[] allCategories = new String[listOfCategories.size()];
            boolean[] checkedCategories = new boolean[listOfCategories.size()];

            //Заполняем массив checkedCategories исходя из переданного списка фильтрованных категорий (чтобы установить галочки)
            for (int i = 0; i < listOfCategories.size(); i++) {
                for (Category filteredCategory : filteredCategories) {
                    if (filteredCategory.getId() == listOfCategories.get(i).getId())
                        checkedCategories[i] = true;
                }
            }

            //Приводим список объектов Категория к списку названий категорий
            for (int i = 0; i < listOfCategories.size(); i++) {
                allCategories[i] = listOfCategories.get(i).getName();
            }

            Builder builder = new Builder(getActivity())
                    .setTitle("Фильтр по категориям:")
                    .setMultiChoiceItems(allCategories, checkedCategories, (dialog, which, isChecked) -> checkedCategories[which] = isChecked)
                    .setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("ОК", (dialog, which) -> {
                        for (int i = 0; i < checkedCategories.length; i++) {
                            if (checkedCategories[i]) {
                                selectedCategories.add(listOfCategories.get(i));
                            }
                        }
                        viewModel.selectedFilteredCategories(selectedCategories);
                    })
                    .setNeutralButton("Отметить все", (dialog, which) -> {
                        viewModel.clearCategoriesFilter();
                    })
                    .setOnDismissListener(dialog -> viewModel.canceledFilterCategoryDialog());

            builder.show();
        });
    }

    private void showFilterPersonDialog(List<Person> filteredPersons) {
        viewModel.getListOfPersons().observe(getViewLifecycleOwner(), listOfPersons -> {

            ArrayList<Person> selectedPersons = new ArrayList<>();
            String[] allPersons = new String[listOfPersons.size()];
            boolean[] checkedPersons = new boolean[listOfPersons.size()];

            //Заполняем массив checkedPersons исходя из переданного списка фильтрованных категорий (чтобы установить галочки)
            for (int i = 0; i < listOfPersons.size(); i++) {
                for (Person filteredPerson : filteredPersons) {
                    if (filteredPerson.getId() == listOfPersons.get(i).getId())
                        checkedPersons[i] = true;
                }
            }

            //Приводим список объектов Категория к списку названий категорий
            for (int i = 0; i < listOfPersons.size(); i++) {
                allPersons[i] = listOfPersons.get(i).getName();
            }

            Builder builder = new Builder(getActivity())
                    .setTitle("Фильтр по персонам:")
                    .setMultiChoiceItems(allPersons, checkedPersons, (dialog, which, isChecked) -> checkedPersons[which] = isChecked)
                    .setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("ОК", (dialog, which) -> {
                        for (int i = 0; i < checkedPersons.length; i++) {
                            if (checkedPersons[i]) {
                                selectedPersons.add(listOfPersons.get(i));
                            }
                        }
                        viewModel.selectedFilteredPersons(selectedPersons);
                    })
                    .setNeutralButton("Отметить все", (dialog, which) -> {
                        viewModel.clearPersonsFilter();
                    })
                    .setOnDismissListener(dialog -> viewModel.canceledFilterPersonDialog());

            builder.show();
        });
    }

    private void setupViewPager() {
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        subscribeOnTitleChangeEvent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getGroupId() == R.id.group_sort) {
            item.setChecked(true);
            viewModel.pickedSortBy(item.getItemId(), item.getTitle().toString());
            return true;
        } else {
            switch (item.getItemId()) {
                case R.id.filter_active:
                    if(item.isChecked()) {
                        viewModel.clickedOnFilterActiveMenu(false);
                    } else {
                        viewModel.clickedOnFilterActiveMenu(true);
                    }
                    return true;
                case R.id.filter_category:
                    viewModel.clickedOnFilterCategoryMenu();
                    return true;
                case R.id.filter_person:
                    viewModel.clickedOnFilterPersonMenu();
                    return true;
                case R.id.menu_persons:
                    viewModel.clickedOnPersonsMenu();
                    return true;
                case R.id.menu_categories:
                    viewModel.clickedOnCategoriesMenu();
                    return true;
                case R.id.menu_backup_restore:
                    viewModel.clickedOnBackupRestoreMenu();
                    return true;
                case R.id.menu_about:
                    viewModel.clickedOnAboutMenu();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(8);
    }
}
