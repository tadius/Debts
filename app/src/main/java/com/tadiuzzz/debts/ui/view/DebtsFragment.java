package com.tadiuzzz.debts.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tadiuzzz.debts.ui.presentation.DebtsViewModel;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.ui.adapter.DebtPOJOsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class DebtsFragment extends Fragment {

    private DebtsViewModel viewModel;
    private DebtPOJOsAdapter debtPOJOsAdapter;
    public static final String TAG = "logTag";
    @BindView(R.id.rvDebts)
    RecyclerView rvDebts;
    @BindView(R.id.fbAddDebt)
    FloatingActionButton fbAddDebt;

    @OnClick(R.id.fbAddDebt)
    void onAddButtonClick() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_debtsFragment_to_editDebtFragment);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debts, container, false);

        ButterKnife.bind(this, view);

        viewModel = ViewModelProviders.of(this).get(DebtsViewModel.class);

        setupRecyclerView();

        subscribeOnData();

        subscribeNavigationEvents();

        setupFABanimation();

        viewModel.viewLoaded();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                viewModel.clickedOnFilterMenu();
                return true;
            case R.id.menu_persons:
                viewModel.clickedOnPersonsMenu();
                return true;
            case R.id.menu_categories:
                viewModel.clickedOnCategoriesMenu();
                return true;
            case R.id.menu_about:
                viewModel.clickedOnAboutMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupRecyclerView() {
        debtPOJOsAdapter = new DebtPOJOsAdapter();
        rvDebts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDebts.setAdapter(debtPOJOsAdapter);

        debtPOJOsAdapter.setOnDebtPOJOClickListener(debtPOJO -> viewModel.clickedOnDebtPOJO(debtPOJO));
    }

    private void subscribeOnData() {
        viewModel.getLiveDataDebtPOJOs().observe(this, debtPOJOS -> {
            debtPOJOsAdapter.setData(debtPOJOS);
            debtPOJOsAdapter.notifyDataSetChanged();
        });
    }

    private void subscribeNavigationEvents() {
        viewModel.getNavigateToEditDebtScreenEvent().observe(this, o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_debtsFragment_to_editDebtFragment));
        viewModel.getShowFilterDialogEvent().observe(this, o -> showFilterDialog());
        viewModel.getNavigateToPersonsScreenEvent().observe(this, o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_debtsFragment_to_personsFragment));
        viewModel.getNavigateToCategoriesScreenEvent().observe(this, o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_debtsFragment_to_categoriesFragment));
        viewModel.getNavigateToAboutScreenEvent().observe(this, o -> Toast.makeText(getActivity(), "ABOUT CLICKED", Toast.LENGTH_SHORT).show());
    }

    private void setupFABanimation() {
        //анимация FAB
        rvDebts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fbAddDebt.isShown())
                    fbAddDebt.show();
                else if (dy > 0 && fbAddDebt.isShown())
                    fbAddDebt.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void showFilterDialog() {
        Toast.makeText(getActivity(), "FILTER CLICKED", Toast.LENGTH_SHORT).show();
    }

}
