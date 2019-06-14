package com.tadiuzzz.debts.ui.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.adapter.PersonAdapter;
import com.tadiuzzz.debts.ui.presentation.PersonsViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class PersonsFragment extends Fragment {

    private PersonsViewModel personsViewModel;
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

        Bundle bundle = getArguments();
        if(bundle != null){
            isPickingPerson = bundle.getBoolean("pickPerson", false);
        }

        PersonAdapter personAdapter = new PersonAdapter();
        rvPersons.setAdapter(personAdapter);
        personAdapter.setOnPersonClickListener(new PersonAdapter.OnPersonClickListener() {
            @Override
            public void onPersonClick(Person person) {
                personsViewModel.clickedOnPerson(person, isPickingPerson);
            }
        });

        rvPersons.setLayoutManager(new LinearLayoutManager(getActivity()));

        personsViewModel = ViewModelProviders.of(this).get(PersonsViewModel.class);

        personsViewModel.getLiveDataPersons().observe(this, new Observer<List<Person>>() {
            @Override
            public void onChanged(List<Person> persons) {
                personAdapter.setData(persons);
                personAdapter.notifyDataSetChanged();
            }
        });

        personsViewModel.navigateToEditPersonScreen().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                Person person = (Person) o;
                Bundle bundle = new Bundle();
                bundle.putInt("personId", person.getId());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_personsFragment_to_editPersonFragment, bundle);
            }
        });

        personsViewModel.navigateToPreviousScreen().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
            }
        });

        return view;
    }

}
