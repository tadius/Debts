package com.tadiuzzz.debts.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tadiuzzz.debts.CategoriesViewModel;
import com.tadiuzzz.debts.DebtsViewModel;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.R2;
import com.tadiuzzz.debts.entity.Category;
import com.tadiuzzz.debts.entity.Debt;
import com.tadiuzzz.debts.entity.DebtPOJO;
import com.tadiuzzz.debts.entity.Person;
import com.tadiuzzz.debts.ui.adapter.CategoryAdapter;
import com.tadiuzzz.debts.ui.adapter.DebtPOJOsAdapter;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class CategoriesFragment extends Fragment {

    private CategoriesViewModel categoriesViewModel;
    public static final String TAG = "logTag";
    @BindView(R.id.rvCategories) RecyclerView rvCategories;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        ButterKnife.bind(this, view);

        CategoryAdapter categoryAdapter = new CategoryAdapter();
        rvCategories.setAdapter(categoryAdapter);

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
