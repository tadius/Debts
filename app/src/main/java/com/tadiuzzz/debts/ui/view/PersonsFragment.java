package com.tadiuzzz.debts.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.adapter.PersonAdapter;
import com.tadiuzzz.debts.ui.presentation.PersonsViewModel;
import com.tadiuzzz.debts.ui.presentation.ViewModelProviderFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerFragment;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class PersonsFragment extends DaggerFragment {

    @Inject
    ViewModelProviderFactory providerFactory;

    private PersonsViewModel viewModel;
    private PersonAdapter personAdapter;
    private boolean isPickingPerson;
    public static final String TAG = "logTag";
    @BindView(R.id.rvPersons)
    RecyclerView rvPersons;
    @BindView(R.id.fbAddPerson)
    FloatingActionButton fbAddPerson;

    @OnClick(R.id.fbAddPerson)
    void onAddButtonClick(){
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_personsFragment_to_editPersonFragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persons, container, false);

        ButterKnife.bind(this, view);

        viewModel = ViewModelProviders.of(this, providerFactory).get(PersonsViewModel.class);

        Bundle bundle = getArguments();
        if(bundle != null) isPickingPerson = bundle.getBoolean("pickPerson", false);

        setupRecyclerView();

        subscribeOnData();

        subscribeOnNavigationEvents();

        setupFABanimation();

        return view;
    }

    private void setupRecyclerView() {
        personAdapter = new PersonAdapter();
        rvPersons.setAdapter(personAdapter);
        rvPersons.setLayoutManager(new LinearLayoutManager(getActivity()));

        personAdapter.setOnPersonClickListener(person -> viewModel.clickedOnPerson(person, isPickingPerson));
    }

    private void subscribeOnData() {
        viewModel.getPersons().observe(getViewLifecycleOwner(), persons -> {
            personAdapter.setData(persons);
            personAdapter.notifyDataSetChanged();
        });
    }

    private void subscribeOnNavigationEvents() {
        viewModel.getNavigateToEditPersonScreenEvent().observe(getViewLifecycleOwner(), o -> {
            Person person = (Person) o;
            Bundle bundle = new Bundle();
            bundle.putInt("personId", person.getId());
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_personsFragment_to_editPersonFragment, bundle);
        });

        viewModel.getNavigateToPreviousScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack());
    }

    private void setupFABanimation() {
        //анимация FAB
        rvPersons.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fbAddPerson.isShown())
                    fbAddPerson.show();
                else if (dy > 0 && fbAddPerson.isShown())
                    fbAddPerson.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
}
