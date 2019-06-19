package com.tadiuzzz.debts.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tadiuzzz.debts.ui.presentation.CategoriesViewModel;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.ui.adapter.CategoryAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class CategoriesFragment extends Fragment {

    private CategoriesViewModel viewModel;
    private CategoryAdapter categoryAdapter;
    private boolean isPickingCategory;
    public static final String TAG = "logTag";
    @BindView(R.id.rvCategories)
    RecyclerView rvCategories;
    @BindView(R.id.fbAddCategory)
    FloatingActionButton fbAddCategory;

    @OnClick(R.id.fbAddCategory)
    void onAddButtonClick() {
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_categoriesFragment_to_editCategoryFragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) isPickingCategory = bundle.getBoolean("pickCategory", false);

        viewModel = ViewModelProviders.of(this).get(CategoriesViewModel.class);

        setupRecyclerView();

        subscribeOnData();

        subscribeNavigationEvents();

        setupFABanimation();

        return view;
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter();

        rvCategories.setAdapter(categoryAdapter);

        rvCategories.setLayoutManager(new LinearLayoutManager(getActivity()));

        categoryAdapter.setOnCategoryClickListener(category -> viewModel.clickedOnCategory(category, isPickingCategory));
    }

    private void subscribeOnData() {
        viewModel.getLiveDataCategories().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                categoryAdapter.setData(categories);
                categoryAdapter.notifyDataSetChanged();
            }
        });
    }

    private void subscribeNavigationEvents() {
        viewModel.navigateToEditCategoryScreen().observe(this, o -> {
            Category category = (Category) o;
            Bundle bundle = new Bundle();
            bundle.putInt("categoryId", category.getId());
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_categoriesFragment_to_editCategoryFragment, bundle);
        });

        viewModel.navigateToPreviousScreen().observe(this, o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack());
    }

    private void setupFABanimation() {
        //анимация FAB
        rvCategories.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && !fbAddCategory.isShown())
                    fbAddCategory.show();
                else if (dy > 0 && fbAddCategory.isShown())
                    fbAddCategory.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

}
