package com.tadiuzzz.debts.ui.view;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.ui.adapter.ViewPagerAdapter;
import com.tadiuzzz.debts.ui.presentation.ViewPagerViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewPagerFragment extends Fragment {

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

        viewModel = ViewModelProviders.of(this).get(ViewPagerViewModel.class);

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
        viewModel.getShowFilterDialogEvent().observe(getViewLifecycleOwner(), o -> showFilterDialog());
        viewModel.getShowSortDialogEvent().observe(getViewLifecycleOwner(), sortBy -> showSortDialog((Integer) sortBy));
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
        switch (item.getItemId()) {
            case R.id.menu_sort:
                viewModel.clickedOnSortMenu();
                return true;
            case R.id.menu_filter:
                viewModel.clickedOnFilterMenu();
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

    private void showSortDialog(Integer sortBy) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Сортировка");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice);

        arrayAdapter.addAll(getResources().getStringArray(R.array.sort_array_with_arrows));

        builderSingle.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());

        builderSingle.setSingleChoiceItems(arrayAdapter, sortBy, (dialog, position) -> {

            String title = getResources().getStringArray(R.array.sort_array)[position];

            viewModel.pickedSortBy(position, title);

            dialog.dismiss();
        });
        builderSingle.show();
    }

    private void showFilterDialog() {
        Toast.makeText(getActivity(), "FILTER CLICKED", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(8);
    }
}
