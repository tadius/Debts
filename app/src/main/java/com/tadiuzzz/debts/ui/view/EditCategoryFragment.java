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
import com.tadiuzzz.debts.ui.presentation.EditCategoryViewModel;

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
public class EditCategoryFragment extends Fragment {

    private EditCategoryViewModel editCategoryViewModel;
    private Category loadedCategory;

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
        if(loadedCategory != null) {
            if(loadedCategory.getName().equals(enteredCategoryName)) {
                Toast.makeText(getActivity(), "Название категории не изменилось!", Toast.LENGTH_SHORT).show();
            } else if (enteredCategoryName.isEmpty()) {
                Toast.makeText(getActivity(), "Введите название категории!", Toast.LENGTH_SHORT).show();
            } else {
                loadedCategory.setName(enteredCategoryName);
                editCategoryViewModel.updateCategory(loadedCategory)
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
            if (!enteredCategoryName.isEmpty()) {
                editCategoryViewModel.insertCategory(new Category(enteredCategoryName))
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
                Toast.makeText(getActivity(), "Введите название категории!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.btnDeleteCategory)
    void onDeleteClick() {
        if(loadedCategory != null) {
            editCategoryViewModel.deleteCategory(loadedCategory)
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
            Toast.makeText(getActivity(), "Такой категории не существует!", Toast.LENGTH_SHORT).show();
        }
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
            setFieldsFromDb(categoryId);
        }
        return view;
    }

    private void setFieldsFromDb(int categoryId) {
        editCategoryViewModel.getCategoryByID(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<Category>() {
                    @Override
                    public void onSuccess(Category category) {
                        Log.i(TAG, "onSuccess: " + category.getId());
                        loadedCategory = category;
                        tvEditCategoryId.setText(String.valueOf(category.getId()));
                        etCategoryName.setText(category.getName());
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
