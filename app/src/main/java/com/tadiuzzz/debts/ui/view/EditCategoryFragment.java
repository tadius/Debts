package com.tadiuzzz.debts.ui.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.ui.presentation.EditCategoryViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditCategoryFragment extends Fragment {

    private EditCategoryViewModel viewModel;

    public static final String TAG = "logTag";
    @BindView(R.id.etCategoryName)
    EditText etCategoryName;
    @BindView(R.id.btnSaveCategory)
    Button btnSaveCategory;
    @BindView(R.id.btnDeleteCategory)
    Button btnDeleteCategory;

    @OnClick(R.id.btnSaveCategory)
    void onSaveClick() {
        String enteredCategoryName = etCategoryName.getText().toString().trim();
        viewModel.saveButtonClicked(enteredCategoryName);
    }

    @OnClick(R.id.btnDeleteCategory)
    void onDeleteClick() {
        viewModel.deleteButtonClicked();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_category, container, false);

        ButterKnife.bind(this, view);

        viewModel = ViewModelProviders.of(this).get(EditCategoryViewModel.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int categoryId = bundle.getInt("categoryId");
            viewModel.gotPickedCategory(categoryId); //передаем id объекта во ViewModel, который пришел в Bundle для инициализации LiveData
        }

        subscribeOnData();

        subscribeOnNavigationEvents();

        subscribeOnNotificationEvents();

        subscribeOnDialogsEvents();

        return view;
    }

    private void subscribeOnNotificationEvents() {
        viewModel.getShowToastEvent().observe(getViewLifecycleOwner(), message -> showToast((String) message));
    }

    private void subscribeOnNavigationEvents() {
        viewModel.getNavigateToPreviousScreenEvent().observe(getViewLifecycleOwner(), o -> {
            hideKeyboard(getActivity());
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
        });
    }

    private void subscribeOnDialogsEvents() {
        viewModel.getShowConfirmDeleteDialogEvent().observe(getViewLifecycleOwner(), show -> {if (show) showConfirmDeleteDialog();});
    }

    private void subscribeOnData() {
        viewModel.getCategory().observe(getViewLifecycleOwner(), category -> setFields(category));
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void showConfirmDeleteDialog() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Подтверждение удаления");
        builderSingle.setMessage("Вы действительно хотите удалить эту категорию? ");

        builderSingle.setNegativeButton("Отмена", (dialog, which) -> {
            viewModel.canceledDelete();
            dialog.dismiss();
        });

        builderSingle.setPositiveButton("Удалить", (dialog, which) -> {
            viewModel.confirmedDelete();
        });

        builderSingle.show();
    }

    private void setFields(Category category) {
        etCategoryName.setText(category.getName());
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
