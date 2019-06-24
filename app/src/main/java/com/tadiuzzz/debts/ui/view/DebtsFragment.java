package com.tadiuzzz.debts.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
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

import com.tadiuzzz.debts.ui.presentation.DebtsViewModel;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.ui.adapter.DebtPOJOsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class DebtsFragment extends Fragment {

    private DebtsViewModel viewModel;
    private DebtPOJOsAdapter debtPOJOsAdapter;
    public static final String TAG = "logTag";
    @BindView(R.id.rvDebts)
    RecyclerView rvDebts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debts, container, false);

        ButterKnife.bind(this, view);

        viewModel = ViewModelProviders.of(this).get(DebtsViewModel.class);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int position = bundle.getInt("position");
            switch (position) {
                case 1:
                    subscribeOnDataIAmCreditor();
                    break;
                case 2:
                    subscribeOnDataIAmBorrower();
                    break;
            }
        }


        setupRecyclerView();

//        subscribeOnData();

        subscribeNavigationEvents();

//        setupFABanimation();

        viewModel.viewLoaded();

        return view;
    }

    private void setupRecyclerView() {
        debtPOJOsAdapter = new DebtPOJOsAdapter();
        rvDebts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvDebts.setAdapter(debtPOJOsAdapter);

        debtPOJOsAdapter.setOnDebtPOJOClickListener(debtPOJO -> viewModel.clickedOnDebtPOJO(debtPOJO));
    }

    private void subscribeOnDataIAmCreditor() {
        viewModel.getListOfDebtPOJOSIAmCreditor().observe(getViewLifecycleOwner(), debtPOJOS -> {
            debtPOJOsAdapter.setData(debtPOJOS);
            debtPOJOsAdapter.notifyDataSetChanged();
        });
    }

    private void subscribeOnDataIAmBorrower() {
        viewModel.getListOfDebtPOJOSIAmBorrower().observe(getViewLifecycleOwner(), debtPOJOS -> {
            debtPOJOsAdapter.setData(debtPOJOS);
            debtPOJOsAdapter.notifyDataSetChanged();
        });
    }

    private void subscribeNavigationEvents() {
        viewModel.getNavigateToEditDebtScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_viewPagerFragment_to_editDebtFragment));
    }


}
