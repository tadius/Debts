package com.tadiuzzz.debts.ui.view;

import android.os.Bundle;
import android.view.Gravity;
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
import androidx.lifecycle.Observer;
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

    private EditCategoryViewModel editCategoryViewModel;

    public static final String TAG = "logTag";
    @BindView(R.id.tvEditCategoryId)
    TextView tvEditCategoryId;
    @BindView(R.id.etCategoryName)
    EditText etCategoryName;
    @BindView(R.id.btnSaveCategory)
    Button btnSaveCategory;
    @BindView(R.id.btnDeleteCategory)
    Button btnDeleteCategory;

    @OnClick(R.id.btnSaveCategory)
    void onSaveClick() {
        String enteredCategoryName = etCategoryName.getText().toString().trim();
        editCategoryViewModel.saveButtonClicked(enteredCategoryName);
    }

    @OnClick(R.id.btnDeleteCategory)
    void onDeleteClick() {
        editCategoryViewModel.deleteButtonClicked();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_category, container, false);

        ButterKnife.bind(this, view);

        editCategoryViewModel = ViewModelProviders.of(this).get(EditCategoryViewModel.class);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int categoryId = bundle.getInt("categoryId");
            editCategoryViewModel.pickedCategory(categoryId);
        }

        editCategoryViewModel.getLiveDataCategory().observe(this, new Observer<Category>() {
            @Override
            public void onChanged(Category category) {
                setFields(category);
            }
        });

        // Подписываемся на состояние действия тоста
        editCategoryViewModel.showToast().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                showToast((String) o);
            }
        });

        // Подписываемся на состояние действия навигации
        editCategoryViewModel.navigateToPreviousScreen().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
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

    private void setFields(Category category) {
        tvEditCategoryId.setText(String.valueOf(category.getId()));
        etCategoryName.setText(category.getName());
    }

}
