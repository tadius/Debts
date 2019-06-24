package com.tadiuzzz.debts.ui.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.ui.presentation.EditDebtViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditDebtFragment extends Fragment {

    private EditDebtViewModel viewModel;

    public static final String TAG = "logTag";

    @BindView(R.id.tvEditDebtId)
    TextView tvEditDebtId;
    @BindView(R.id.rbIAmCreditor)
    RadioButton rbIAmCreditor;
    @BindView(R.id.rbIAmBorrower)
    RadioButton rbIAmBorrower;
    @BindView(R.id.etEditDebtDescription)
    EditText etEditDebtDescription;
    @BindView(R.id.etEditDebtAmount)
    EditText etEditDebtAmount;
    @BindView(R.id.etEditDebtDateOfStart)
    EditText etEditDebtDateOfStart;
    @BindView(R.id.etEditDebtDateOfExpiration)
    EditText etEditDebtDateOfExpiration;
    @BindView(R.id.etEditDebtDateOfEnd)
    EditText etEditDebtDateOfEnd;
    @BindView(R.id.etEditDebtCategoryName)
    EditText etEditDebtCategoryName;
    @BindView(R.id.etEditDebtPersonName)
    EditText etEditDebtPersonName;
    @BindView(R.id.cbIsReturned)
    CheckBox cbIsReturned;
    @BindView(R.id.llDateOfEndContainer)
    LinearLayout llDateOfEndContainer;

    @BindView(R.id.btnSaveDebt)
    Button btnSaveDebt;
    @BindView(R.id.btnDeleteDebt)
    Button btnDeleteDebt;

    @OnClick(R.id.rbIAmCreditor)
    void onIAmCreditorClick() {
        viewModel.amIBorrowerClicked(rbIAmBorrower.isChecked());
    }

    @OnClick(R.id.rbIAmBorrower)
    void onIAmBorrowerClick() {
        viewModel.amIBorrowerClicked(rbIAmBorrower.isChecked());
    }

    @OnClick(R.id.etEditDebtDateOfStart)
    void onDateStartClick() {
        viewModel.pickDateOfStartClicked();
    }

    @OnClick(R.id.etEditDebtDateOfExpiration)
    void onDateExpirationClick() {
        viewModel.pickDateOfExpirationClicked();
    }

    @OnClick(R.id.etEditDebtDateOfEnd)
    void onDateEndClick() {
        viewModel.pickDateOfEndClicked();
    }

    @OnClick(R.id.etEditDebtCategoryName)
    void onCategoryClick() {
        viewModel.pickCategoryClicked();
    }

    @OnClick(R.id.etEditDebtPersonName)
    void onPersonClick() {
        viewModel.pickPersonClicked();
    }

    @OnClick(R.id.btnSaveDebt)
    void onSaveClick() {
        viewModel.saveButtonClicked();
    }

    @OnClick(R.id.btnDeleteDebt)
    void onDeleteClick() {
        viewModel.deleteButtonClicked();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_debt, container, false);

        ButterKnife.bind(this, view);

        viewModel = ViewModelProviders.of(this).get(EditDebtViewModel.class);

        setUpEditFieldsListeners();

        subscribeOnData();

        subscribeOnNavigationEvents();

        subscribeOnDialogsEvents();

        subscribeOnNotificationEvents();

        return view;
    }

    private void subscribeOnData() {
        viewModel.getCachedDebtPOJO().observe(this, debtPOJO -> setFields(debtPOJO));
    }

    private void setUpEditFieldsListeners() {
        etEditDebtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.changedTextDebtAmount(String.valueOf(etEditDebtAmount.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etEditDebtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.changedTextDebtDescription(String.valueOf(etEditDebtDescription.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbIsReturned.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.isReturnedCheckChanged(isChecked));
    }

    private void subscribeOnNotificationEvents() {
        viewModel.getShowToastEvent().observe(this, message -> showToast((String) message));
    }

    private void subscribeOnDialogsEvents() {
        viewModel.getShowPickDateOfStartDialogEvent().observe(this, o -> showDatePickerDialog((Calendar) o, viewModel.TYPE_OF_DATE_START));

        viewModel.getShowPickDateOfExpirationDialogEvent().observe(this, o -> showDatePickerDialog((Calendar) o, viewModel.TYPE_OF_DATE_EXPIRATION));

        viewModel.getShowPickDateOfEndDialogEvent().observe(this, o -> showDatePickerDialog((Calendar) o, viewModel.TYPE_OF_DATE_END));

        viewModel.getShowEndDateContainerEvent().observe(this, o -> showEndDateContainer((Boolean) o));
    }

    private void subscribeOnNavigationEvents() {
        viewModel.getNavigateToPreviousScreenEvent().observe(this, o -> {
            hideKeyboard(getActivity());
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
        });

        viewModel.getNavigateToPickPersonScreenEvent().observe(this, o -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("pickPerson", true);
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_editDebtFragment_to_personsFragment, bundle);
        });
        viewModel.getNavigateToPickCategoryScreenEvent().observe(this, o -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean("pickCategory", true);
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_editDebtFragment_to_categoriesFragment, bundle);
        });
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void setFields(DebtPOJO debtPOJO) {
        tvEditDebtId.setText(String.valueOf(debtPOJO.getDebt().getId()));
        if (debtPOJO.getDebt().amIBorrower()) {
            rbIAmBorrower.setChecked(true);
        } else {
            rbIAmCreditor.setChecked(true);
        }
        etEditDebtDescription.setText(debtPOJO.getDebt().getDescription());
        etEditDebtAmount.setText(String.valueOf(debtPOJO.getDebt().getAmount()));
        etEditDebtDateOfStart.setText(getStringDate(debtPOJO.getDebt().getDateOfStart()));
        etEditDebtDateOfExpiration.setText(getStringDate(debtPOJO.getDebt().getDateOfExpiration()));
        etEditDebtDateOfEnd.setText(getStringDate(debtPOJO.getDebt().getDateOfEnd()));
        cbIsReturned.setChecked(debtPOJO.getDebt().isReturned());
        etEditDebtCategoryName.setText(debtPOJO.getDebtCategory().getName());
        etEditDebtPersonName.setText(debtPOJO.getDebtPerson().getName());
    }

    private void showDatePickerDialog(Calendar calendar, int typeOfDate) {

        DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, year, month, day) -> viewModel.pickedDate(year, month, day, typeOfDate);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(), onDateSetListener, year, month, day);
        dialog.getWindow();
        dialog.show();
    }

    private void showEndDateContainer(boolean visibly) {
        if(visibly) {
            llDateOfEndContainer.setVisibility(View.VISIBLE);
        } else {
            llDateOfEndContainer.setVisibility(View.GONE);
        }
    }

    private String getStringDate(long inputDate) {
        Date date = new Date(inputDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return simpleDateFormat.format(date);
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
