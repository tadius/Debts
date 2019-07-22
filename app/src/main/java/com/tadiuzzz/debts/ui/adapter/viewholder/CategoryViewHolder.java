package com.tadiuzzz.debts.ui.adapter.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.tadiuzzz.debts.databinding.CategoryItemBinding;
import com.tadiuzzz.debts.domain.entity.Category;

/**
 * Created by Simonov.vv on 22.07.2019.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder {

    CategoryItemBinding binding;

    public CategoryViewHolder(CategoryItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Category category) {
        binding.setCategory(category);
        binding.executePendingBindings();
    }
}