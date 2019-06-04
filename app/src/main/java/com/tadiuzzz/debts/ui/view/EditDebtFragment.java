package com.tadiuzzz.debts.ui.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Debt;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.ui.presentation.EditCategoryViewModel;
import com.tadiuzzz.debts.ui.presentation.EditDebtViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

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
public class EditDebtFragment extends Fragment {

    private EditDebtViewModel editDebtViewModel;
    private DebtPOJO loadedDebtPOJO;

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

    @OnClick(R.id.btnSaveDebt)
    void onSaveClick() {

//        String enteredDebtName = etDebtName.getText().toString().trim();
//        if(loadedCategory != null) {
//            if(loadedCategory.getName().equals(enteredCategoryName)) {
//                Toast.makeText(getActivity(), "Название категории не изменилось!", Toast.LENGTH_SHORT).show();
//            } else if (enteredCategoryName.isEmpty()) {
//                Toast.makeText(getActivity(), "Введите название категории!", Toast.LENGTH_SHORT).show();
//            } else {
//                loadedCategory.setName(enteredCategoryName);
//                editCategoryViewModel.updateCategory(loadedCategory)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableCompletableObserver() {
//                    @Override
//                    public void onComplete() {
//                        Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
//                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(getActivity(), "Error saving!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        } else {
//            if (!enteredCategoryName.isEmpty()) {
//                editCategoryViewModel.insertCategory(new Category(enteredCategoryName))
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new DisposableCompletableObserver() {
//                            @Override
//                            public void onComplete() {
//                                Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
//                                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Toast.makeText(getActivity(), "Error saving!", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            } else {
//                Toast.makeText(getActivity(), "Введите название категории!", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    @OnClick(R.id.btnDeleteDebt)
    void onDeleteClick() {
//        if(loadedCategory != null) {
//            editCategoryViewModel.deleteCategory(loadedCategory)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(new DisposableCompletableObserver() {
//                @Override
//                public void onComplete() {
//                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
//                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    Toast.makeText(getActivity(), "Error deleting!", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(getActivity(), "Такой категории не существует!", Toast.LENGTH_SHORT).show();
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_debt, container, false);

        ButterKnife.bind(this, view);

        editDebtViewModel = ViewModelProviders.of(this).get(EditDebtViewModel.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int debtId = bundle.getInt("debtId");
            setFieldsFromDb(debtId);
        }
        return view;
    }

    private void setFieldsFromDb(int debtId) {
        editDebtViewModel.getDebtPOJOByID(debtId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<DebtPOJO>() {
                    @Override
                    public void onSuccess(DebtPOJO debtPOJO) {
                        Log.i(TAG, "onSuccess: " + debtPOJO.getDebt().getId());
                        loadedDebtPOJO = debtPOJO;
                        tvEditDebtId.setText(String.valueOf(debtPOJO.getDebt().getId()));
                        etEditDebtDescription.setText(debtPOJO.getDebt().getDescription());
                        etEditDebtAmount.setText(String.valueOf(debtPOJO.getDebt().getAmount()));
                        etEditDebtDateOfStart.setText(getStringDate(debtPOJO.getDebt().getDateOfStart()));
                        etEditDebtDateOfExpiration.setText(getStringDate(debtPOJO.getDebt().getDateOfExpiration()));
                        etEditDebtDateOfEnd.setText(getStringDate(debtPOJO.getDebt().getDateOfEnd()));
                        etEditDebtCategoryName.setText(debtPOJO.getDebtCategory().getName());
                        etEditDebtPersonNameAndSecondName.setText(debtPOJO.getDebtPerson().getFirstName() + " " + debtPOJO.getDebtPerson().getSecondName());//TODO %s String format
                        cbIsReturned.setChecked(debtPOJO.getDebt().isReturned());
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

    private String getStringDate(long inputDate){
        Date date = new Date(inputDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String resultDate = simpleDateFormat.format(date);
        return resultDate;
    }

}
