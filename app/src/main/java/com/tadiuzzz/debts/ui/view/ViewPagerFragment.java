package com.tadiuzzz.debts.ui.view;

import android.app.AlertDialog;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.ui.adapter.ViewPagerAdapter;
import com.tadiuzzz.debts.ui.presentation.ViewModelProviderFactory;
import com.tadiuzzz.debts.ui.presentation.ViewPagerViewModel;
import com.tadiuzzz.debts.utils.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerFragment;

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

        subscribeOnData();

        subscribeOnDialogsEvents();

        subscribeOnNavigationEvents();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void subscribeOnData() {
//        viewModel.getListOfCategories().observe;
    }

    private void subscribeOnTitleChangeEvent() {
        //убираем тень между actionbar и tablayout
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);

        viewModel.getSortMenuCheckedItem().observe(getViewLifecycleOwner(), itemId -> menu.findItem(itemId).setChecked(true));
        viewModel.getSortMenuTitle().observe(getViewLifecycleOwner(), title -> menu.findItem(R.id.menu_sort).setTitle(title));
        viewModel.getSortMenuIcon().observe(getViewLifecycleOwner(), icon -> menu.findItem(R.id.menu_sort).setIcon(icon));

    }

    private void subscribeOnNavigationEvents() {
        viewModel.getNavigateToEditDebtScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_viewPagerFragment_to_editDebtFragment));
        viewModel.getNavigateToPersonsScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_viewPagerFragment_to_personsFragment));
        viewModel.getNavigateToCategoriesScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_viewPagerFragment_to_categoriesFragment));
        viewModel.getNavigateToBackupRestoreScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_viewPagerFragment_to_backupRestoreFragment));
        viewModel.getNavigateToAboutScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_viewPagerFragment_to_aboutAppFragment));
    }

    private void subscribeOnDialogsEvents() {
        viewModel.getShowFilterCategoryDialogEvent().observe(getViewLifecycleOwner(), o -> showFilterCategoryDialog());
//        viewModel.getShowFilterPersonDialogEvent().observe(getViewLifecycleOwner(), o -> showFilterPersonDialog());
    }

    private void showFilterCategoryDialog() {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_multichoice);
//        arrayAdapter.addAll(listOfFiles);

        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Резервная копия для восстановления:");
        builderSingle.setSingleChoiceItems(arrayAdapter, 0, (dialog, which) -> {});

        builderSingle.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());

        builderSingle.setPositiveButton("Восстановить", (dialog, which) -> {
            int selectedPosition = ((android.app.AlertDialog)dialog).getListView().getCheckedItemPosition();
//            viewModel.pickedFileToRestore(listOfFiles.get(selectedPosition));
        });

        builderSingle.setNeutralButton("Удалить", (dialog, which) -> {
            int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
//            viewModel.pickedFileToDelete(listOfFiles.get(selectedPosition));
        });

        builderSingle.show();
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
                    viewModel.clickedOnFilterActiveMenu();
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
