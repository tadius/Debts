package com.tadiuzzz.debts.ui.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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

    private EditDebtViewModel editDebtViewModel;
//    private DebtPOJO editingDebtPOJO;
    private Calendar calendar;

    public static final String TAG = "logTag";

    @BindView(R.id.tvEditDebtId)
    TextView tvEditDebtId;
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
    @BindView(R.id.etEditDebtPersonNameAndSecondName)
    EditText etEditDebtPersonNameAndSecondName;
    @BindView(R.id.cbIsReturned)
    CheckBox cbIsReturned;

    @BindView(R.id.btnSaveDebt)
    Button btnSaveDebt;
    @BindView(R.id.btnDeleteDebt)
    Button btnDeleteDebt;

    @OnClick(R.id.etEditDebtDateOfStart)
    void onDateStartClick() {
        editDebtViewModel.pickDateOfStartClicked();
    }

    @OnClick(R.id.etEditDebtDateOfExpiration)
    void onDateExpirationClick() {
        editDebtViewModel.pickDateOfExpirationClicked();
    }

    @OnClick(R.id.etEditDebtDateOfEnd)
    void onDateEndClick() {
        editDebtViewModel.pickDateOfEndClicked();
    }

    @OnClick(R.id.etEditDebtCategoryName)
    void onCategoryClick() {
        editDebtViewModel.pickCategoryClicked();
    }

    @OnClick(R.id.etEditDebtPersonNameAndSecondName)
    void onPersonClick() {
        editDebtViewModel.pickPersonClicked();
    }

    @OnClick(R.id.btnSaveDebt)
    void onSaveClick() {

    }

    @OnClick(R.id.btnDeleteDebt)
    void onDeleteClick() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_debt, container, false);

        ButterKnife.bind(this, view);
        editDebtViewModel = ViewModelProviders.of(this).get(EditDebtViewModel.class);
        editDebtViewModel.navigateToPickPersonScreen().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("pickPerson", true);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_editDebtFragment_to_personsFragment, bundle);
            }
        });
        editDebtViewModel.navigateToPickCategoryScreen().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("pickCategory", true);
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_editDebtFragment_to_categoriesFragment, bundle);
            }
        });
        editDebtViewModel.showPickDateOfStartDialog().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                showDatePickerDialog((Calendar) o, editDebtViewModel.TYPE_OF_DATE_START);
            }
        });

        editDebtViewModel.showPickDateOfExpirationDialog().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                showDatePickerDialog((Calendar) o, editDebtViewModel.TYPE_OF_DATE_EXPIRATION);
            }
        });

        editDebtViewModel.showPickDateOfEndDialog().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                showDatePickerDialog((Calendar) o, editDebtViewModel.TYPE_OF_DATE_END);
            }
        });

        etEditDebtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editDebtViewModel.changedTextDebtAmount(String.valueOf(etEditDebtAmount.getText()));
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
                editDebtViewModel.changedTextDebtDescription(String.valueOf(etEditDebtDescription.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        editDebtViewModel.getLiveDataCachedDebtPOJO().observe(this, new Observer<DebtPOJO>() {
            @Override
            public void onChanged(DebtPOJO debtPOJO) {
                setFields(debtPOJO);
            }
        });

    }

    private void setFields(DebtPOJO debtPOJO) {
        tvEditDebtId.setText(String.valueOf(debtPOJO.getDebt().getId()));
        etEditDebtDescription.setText(debtPOJO.getDebt().getDescription());
        etEditDebtAmount.setText(String.valueOf(debtPOJO.getDebt().getAmount()));
        etEditDebtDateOfStart.setText(getStringDate(debtPOJO.getDebt().getDateOfStart()));
        etEditDebtDateOfExpiration.setText(getStringDate(debtPOJO.getDebt().getDateOfExpiration()));
        etEditDebtDateOfEnd.setText(getStringDate(debtPOJO.getDebt().getDateOfEnd()));
        cbIsReturned.setChecked(debtPOJO.getDebt().isReturned());
        etEditDebtCategoryName.setText(debtPOJO.getDebtCategory().getName());
        etEditDebtPersonNameAndSecondName.setText(debtPOJO.getDebtPerson().getFullName());
    }

    public void showDatePickerDialog(Calendar calendar, int typeOfDate) {

        // чтобы вызывался календарь с датой, которая была выбрана ранее, а не сбрасывалась
//        if (dateOfPayment != 0) {
//            calendar.setTimeInMillis(dateOfPayment);
//        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                editDebtViewModel.pickedDate(year, month, day, typeOfDate);
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(getContext(), onDateSetListener, year, month, day);
        dialog.getWindow();
        dialog.show();
    }

    public String getStringDate(Calendar cal) {
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        if (day.length() <= 1) {
            day = "0" + day;
        }
        if (month.length() <= 1) {
            month = "0" + month;
        }
        String stringDate = day + "." + month + "." + year;
        return stringDate;
    }

//    private void setFieldsFromDb(int debtId) {
//        editDebtViewModel.getDebtPOJOByID(debtId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableMaybeObserver<DebtPOJO>() {
//                    @Override
//                    public void onSuccess(DebtPOJO debtPOJO) {
//                        Log.i(TAG, "onSuccess: " + debtPOJO.getDebt().getId());
//                        editingDebtPOJO = debtPOJO;
//                        CacheEditing.getInstance().putDebtPOJOToCache(debtPOJO);
//                        tvEditDebtId.setText(String.valueOf(debtPOJO.getDebt().getId()));
//                        etEditDebtDescription.setText(debtPOJO.getDebt().getDescription());
//                        etEditDebtAmount.setText(String.valueOf(debtPOJO.getDebt().getAmount()));
//                        etEditDebtDateOfStart.setText(getStringDate(debtPOJO.getDebt().getDateOfStart()));
//                        etEditDebtDateOfExpiration.setText(getStringDate(debtPOJO.getDebt().getDateOfExpiration()));
//                        etEditDebtDateOfEnd.setText(getStringDate(debtPOJO.getDebt().getDateOfEnd()));
//                        etEditDebtCategoryName.setText(debtPOJO.getDebtCategory().getName());
//                        etEditDebtPersonNameAndSecondName.setText(debtPOJO.getDebtPerson().getFirstName() + " " + debtPOJO.getDebtPerson().getSecondName());//TODO %s String format
//                        cbIsReturned.setChecked(debtPOJO.getDebt().isReturned());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i(TAG, "onError: " + e.getLocalizedMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

    private String getStringDate(long inputDate) {
        Date date = new Date(inputDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String resultDate = simpleDateFormat.format(date);
        return resultDate;
    }

}
