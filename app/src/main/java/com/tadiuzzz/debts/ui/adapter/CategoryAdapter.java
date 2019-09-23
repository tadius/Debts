package com.tadiuzzz.debts.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.databinding.CategoryItemBinding;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.ui.adapter.viewholder.CategoryViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Simonov.vv on 03.06.2019.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private OnCategoryClickListener onCategoryClickListener;
    private List<Category> categories = new ArrayList<>();

    private ObservableField<Boolean> isDataEmpty = new ObservableField<>();

    public ObservableField<Boolean> getIsDataEmpty() {
        return isDataEmpty;
    }

    public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener){
        this.onCategoryClickListener = onCategoryClickListener;
    }

    public void setData(List<Category> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
        isDataEmpty.set(categories.isEmpty());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CategoryItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_item, parent, false);
        binding.setListener(onCategoryClickListener);

        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

}
