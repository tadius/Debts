package com.tadiuzzz.debts.ui;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tadiuzzz.debts.presentation.CategoriesViewModel;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.entity.Category;
import com.tadiuzzz.debts.presentation.EditCategoryViewModel;
import com.tadiuzzz.debts.ui.adapter.CategoryAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

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
        Toast.makeText(getActivity(), "Save", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnDeleteCategory)
    void onDeleteClick() {
        Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT).show();
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
