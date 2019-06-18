package com.tadiuzzz.debts.ui.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Person;
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

        editPersonViewModel.saveButtonClicked(enteredPersonFirstName, enteredPersonSecondName);
    }

    @OnClick(R.id.btnDeletePerson)
    void onDeleteClick() {
        editPersonViewModel.deleteButtonClicked();
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
            editPersonViewModel.gotPickedPerson(personId); //передаем id объекта во ViewModel, который пришел в Bundle для инициализации LiveData
        }

        //Подписываемся на объект LiveData
        editPersonViewModel.getLiveDataPerson().observe(this, new Observer<Person>() {
            @Override
            public void onChanged(Person person) {
                setFields(person);
            }
        });

        // Подписываемся на состояние действия тоста
        editPersonViewModel.showToast().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                showToast((String) o);
            }
        });

        // Подписываемся на состояние действия навигации
        editPersonViewModel.navigateToPreviousScreen().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                hideKeyboard(getActivity());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
            }
        });
        return view;
    }

    void showToast(String text) {
        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void setFields(Person person) {
        tvEditPersonId.setText(String.valueOf(person.getId()));
        etPersonFirstName.setText(person.getFirstName());
        etPersonSecondName.setText(person.getSecondName());

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
