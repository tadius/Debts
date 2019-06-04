package com.tadiuzzz.debts.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tadiuzzz.debts.presentation.CategoriesViewModel;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.entity.Category;
import com.tadiuzzz.debts.ui.adapter.CategoryAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class CategoriesFragment extends Fragment {

    private CategoriesViewModel categoriesViewModel;
    public static final String TAG = "logTag";
    @BindView(R.id.rvCategories)
    RecyclerView rvCategories;
    @BindView(R.id.fbAddCategory)
    FloatingActionButton fbAddCategory;

    @OnClick(R.id.fbAddCategory)
    void onAddButtonClick(){
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_categoriesFragment_to_editCategoryFragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        ButterKnife.bind(this, view);

        CategoryAdapter categoryAdapter = new CategoryAdapter();
        rvCategories.setAdapter(categoryAdapter);
        categoryAdapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(Category category) {

                Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putInt("categoryId", category.getId());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_categoriesFragment_to_editCategoryFragment, bundle);
            }
        });

        rvCategories.setLayoutManager(new LinearLayoutManager(getActivity()));

        categoriesViewModel = ViewModelProviders.of(this).get(CategoriesViewModel.class);

        categoriesViewModel.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<Category>>() {
                    @Override
                    public void onNext(List<Category> categories) {
                        categoryAdapter.setData(categories);
                        categoryAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i(TAG, "onError: ");

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete: ");

                    }
                });

        return view;
    }

}
