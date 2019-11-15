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

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.databinding.FragmentPersonsBinding;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.adapter.OnPersonClickListener;
import com.tadiuzzz.debts.ui.adapter.PersonAdapter;
import com.tadiuzzz.debts.ui.presentation.PersonsViewModel;
import com.tadiuzzz.debts.ui.presentation.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class PersonsFragment extends DaggerFragment implements OnPersonClickListener {

    @Inject
    ViewModelProviderFactory providerFactory;

    private PersonsViewModel viewModel;
    private PersonAdapter personAdapter;
    private DebtPOJO tempDebtPojo;
    public static final String TAG = "logTag";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this, providerFactory).get(PersonsViewModel.class);

        FragmentPersonsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_persons, container, false);
        binding.setModel(viewModel);

        personAdapter = new PersonAdapter();
        personAdapter.setOnPersonClickListener(this);
        binding.setAdapter(personAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            tempDebtPojo = bundle.getParcelable("debtPojo");
        }

        subscribeOnData();

        subscribeOnNavigationEvents();

        return binding.getRoot();
    }

    private void subscribeOnData() {
        viewModel.getPersons().observe(getViewLifecycleOwner(), persons -> {
            personAdapter.setData(persons);
        });
    }

    private void subscribeOnNavigationEvents() {
        viewModel.getNavigateToEditPersonScreenEvent().observe(getViewLifecycleOwner(), o -> {
            if(o != null) {
                Person person = (Person) o;
                Bundle bundle = new Bundle();
                bundle.putInt("personId", person.getId());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_personsFragment_to_editPersonFragment, bundle);
            } else {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_personsFragment_to_editPersonFragment);
            }
        });

        viewModel.getNavigateToPreviousScreenEvent().observe(getViewLifecycleOwner(), debtPOJO -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("debtPojo", debtPOJO);
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_personsFragment_to_editDebtFragment, bundle);
        });    }

    @Override
    public void onPersonClick(Person person) {
        viewModel.clickedOnPerson(person, tempDebtPojo);
    }
}
