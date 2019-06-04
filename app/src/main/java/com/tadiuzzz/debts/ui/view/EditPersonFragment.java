package com.tadiuzzz.debts.ui.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.entity.Category;
import com.tadiuzzz.debts.entity.Person;
import com.tadiuzzz.debts.ui.presentation.EditCategoryViewModel;
import com.tadiuzzz.debts.ui.presentation.EditPersonViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditPersonFragment extends Fragment {

    private EditPersonViewModel editPersonViewModel;
    private Person loadedPerson;

    public static final String TAG = "logTag";
    @BindView(R.id.tvEditPersonId)
    TextView tvEditPersonId;
    @BindView(R.id.etPersonFirstName)
    EditText etPersonFirstName;
    @BindView(R.id.etPersonSecondName)
    EditText etPersonSecondName;
    @BindView(R.id.btnSavePerson)
    Button btnSavePerson;
    @BindView(R.id.btnDeletePerson)
    Button btnDeletePerson;

    @OnClick(R.id.btnSavePerson)
    void onSaveClick() {

        String enteredPersonFirstName = etPersonFirstName.getText().toString().trim();
        String enteredPersonSecondName = etPersonSecondName.getText().toString().trim();
        if(loadedPerson != null) {
            if(loadedPerson.getFirstName().equals(enteredPersonFirstName) && loadedPerson.getSecondName().equals(enteredPersonSecondName)) {
                Toast.makeText(getActivity(), "Имя и фамилия не изменились!", Toast.LENGTH_SHORT).show();
            } else if (enteredPersonFirstName.isEmpty() || enteredPersonSecondName.isEmpty()) {
                Toast.makeText(getActivity(), "Введите имя и фамилию!", Toast.LENGTH_SHORT).show();
            } else {
                loadedPerson.setFirstName(enteredPersonFirstName);
                loadedPerson.setSecondName(enteredPersonSecondName);
                editPersonViewModel.updatePerson(loadedPerson)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), "Error saving!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            if (!enteredPersonFirstName.isEmpty() && !enteredPersonSecondName.isEmpty()) {
                editPersonViewModel.insertPerson(new Person(enteredPersonFirstName, enteredPersonSecondName))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(getActivity(), "Error saving!", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(getActivity(), "Введите имя и фамилию!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.btnDeletePerson)
    void onDeleteClick() {
        if(loadedPerson != null) {
            editPersonViewModel.deletePerson(loadedPerson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new DisposableCompletableObserver() {
                @Override
                public void onComplete() {
                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                }

                @Override
                public void onError(Throwable e) {
                    Toast.makeText(getActivity(), "Error deleting!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Такой персоны не существует!", Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_person, container, false);

        ButterKnife.bind(this, view);

        editPersonViewModel = ViewModelProviders.of(this).get(EditPersonViewModel.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int personId = bundle.getInt("personId");
            setFieldsFromDb(personId);
        }
        return view;
    }

    private void setFieldsFromDb(int personId) {
        editPersonViewModel.getPersonByID(personId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<Person>() {
                    @Override
                    public void onSuccess(Person person) {
                        Log.i(TAG, "onSuccess: " + person.getId());
                        loadedPerson = person;
                        tvEditPersonId.setText(String.valueOf(person.getId()));
                        etPersonFirstName.setText(person.getFirstName());
                        etPersonSecondName.setText(person.getSecondName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
