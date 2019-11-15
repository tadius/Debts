package com.tadiuzzz.debts.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.tadiuzzz.debts.databinding.FragmentDebtsBinding;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.ui.adapter.DebtPOJOAdapter;
import com.tadiuzzz.debts.ui.adapter.OnDebtPOJOClickListener;
import com.tadiuzzz.debts.ui.presentation.DebtsViewModel;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.ui.presentation.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class DebtsFragment extends DaggerFragment implements OnDebtPOJOClickListener {

    @Inject
    ViewModelProviderFactory providerFactory;

    private DebtsViewModel viewModel;
    private DebtPOJOAdapter debtPOJOAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this, providerFactory).get(DebtsViewModel.class);

        FragmentDebtsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_debts, container, false);
        binding.setViewmodel(viewModel);

        debtPOJOAdapter = new DebtPOJOAdapter();
        debtPOJOAdapter.setOnDebtPOJOClickListener(this);
        binding.setAdapter(debtPOJOAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int position = bundle.getInt("position");
            switch (position) {
                case 1:
                    viewModel.setAmIBorrower(false);
                    break;
                case 2:
                    viewModel.setAmIBorrower(true);
                    break;
            }
        }

        subscribeOnData();

        subscribeOnNavigationEvents();

        return binding.getRoot();
    }

    private void subscribeOnData() {
        viewModel.getListOfDebtPOJOS().observe(getViewLifecycleOwner(), debtPOJOS -> {
            debtPOJOAdapter.setData(debtPOJOS);
        });
    }

    private void subscribeOnNavigationEvents() {
        viewModel.getNavigateToEditDebtScreenEvent().observe(getViewLifecycleOwner(), debtPojo -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("debtPojo", debtPojo);
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_viewPagerFragment_to_editDebtFragment, bundle);
        });
       }

    @Override
    public void onDebtPOJOClick(DebtPOJO debtPOJO) {
        viewModel.clickedOnDebtPOJO(debtPOJO);
    }

}
