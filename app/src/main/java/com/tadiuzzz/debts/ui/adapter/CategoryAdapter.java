package com.tadiuzzz.debts.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.Category;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simonov.vv on 03.06.2019.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private OnCategoryClickListener onCategoryClickListener;
    private List<Category> categories;

    public void setData(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);

        return new CategoryViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);

        holder.tvCategoryItemName.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvCategoryItemName) TextView tvCategoryItemName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(onCategoryClickListener != null && position != RecyclerView.NO_POSITION) {
                    onCategoryClickListener.onCategoryClick(categories.get(position));
                }
            });
        }
    }

    public interface OnCategoryClickListener{
        void onCategoryClick(Category category);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener onCategoryClickListener){
        this.onCategoryClickListener = onCategoryClickListener;
    }

}
