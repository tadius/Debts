package com.tadiuzzz.debts.ui.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.presentation.EditPersonViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditPersonFragment extends Fragment {

    private EditPersonViewModel viewModel;

    public static final String TAG = "logTag";
    @BindView(R.id.etPersonName)
    EditText etPersonName;
    @BindView(R.id.btnSavePerson)
    Button btnSavePerson;
    @BindView(R.id.btnDeletePerson)
    Button btnDeletePerson;

    @OnClick(R.id.btnSavePerson)
    void onSaveClick() {
        String enteredPersonName = etPersonName.getText().toString().trim();

        viewModel.saveButtonClicked(enteredPersonName);
    }

    @OnClick(R.id.btnDeletePerson)
    void onDeleteClick() {
        viewModel.deleteButtonClicked();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_person, container, false);

        ButterKnife.bind(this, view);

        viewModel = ViewModelProviders.of(this).get(EditPersonViewModel.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int personId = bundle.getInt("personId");
            viewModel.gotPickedPerson(personId); //передаем id объекта во ViewModel, который пришел в Bundle для инициализации LiveData
        }

        setupTitle();

        subscribeOnData();

        subscribeOnNavigationEvents();

        subscribeOnNotificationEvents();

        return view;
    }

    private void setupTitle() {
        viewModel.getTitle().observe(getViewLifecycleOwner(), title -> getActivity().setTitle(title));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void subscribeOnData() {
        viewModel.getPerson().observe(getViewLifecycleOwner(), person -> setFields(person));
    }

    private void subscribeOnNavigationEvents() {
        viewModel.getNavigateToPreviousScreenEvent().observe(getViewLifecycleOwner(), o -> {
            hideKeyboard(getActivity());
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
        });
    }

    private void subscribeOnNotificationEvents() {
        viewModel.getShowToastEvent().observe(getViewLifecycleOwner(), message -> showToast((String) message));
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void setFields(Person person) {
        etPersonName.setText(person.getName());
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
