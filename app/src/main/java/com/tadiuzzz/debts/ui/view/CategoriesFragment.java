package com.tadiuzzz.debts.ui.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tadiuzzz.debts.databinding.FragmentCategoriesBinding;
import com.tadiuzzz.debts.ui.adapter.OnCategoryClickListener;
import com.tadiuzzz.debts.ui.presentation.CategoriesViewModel;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.ui.adapter.CategoryAdapter;
import com.tadiuzzz.debts.ui.presentation.ViewModelProviderFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerFragment;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class CategoriesFragment extends DaggerFragment implements OnCategoryClickListener {

    @Inject
    ViewModelProviderFactory providerFactory;

    private CategoriesViewModel viewModel;
    private CategoryAdapter categoryAdapter;
    private boolean isPickingCategory;
    public static final String TAG = "logTag";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = ViewModelProviders.of(this, providerFactory).get(CategoriesViewModel.class);

        FragmentCategoriesBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false);
        binding.setModel(viewModel);

        categoryAdapter = new CategoryAdapter();
        categoryAdapter.setOnCategoryClickListener(this);
        binding.setAdapter(categoryAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) isPickingCategory = bundle.getBoolean("pickCategory", false);

        subscribeOnData();

        subscribeOnNavigationEvents();

        return binding.getRoot();
    }

    private void subscribeOnData() {
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            categoryAdapter.setData(categories);
        });
    }

    private void subscribeOnNavigationEvents() {
        viewModel.getNavigateToEditCategoryScreenEvent().observe(getViewLifecycleOwner(), o -> {
            if(o != null) {
                Category category = (Category) o;
                Bundle bundle = new Bundle();
                bundle.putInt("categoryId", category.getId());
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_categoriesFragment_to_editCategoryFragment, bundle);
            } else {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_categoriesFragment_to_editCategoryFragment);
            }

        });

        viewModel.getNavigateToPreviousScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack());
    }

    @Override
    public void onCategoryClick(Category category) {
        viewModel.clickedOnCategory(category, isPickingCategory);
    }
}
