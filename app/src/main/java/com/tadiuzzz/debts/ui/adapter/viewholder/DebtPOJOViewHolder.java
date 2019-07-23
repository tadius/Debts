package com.tadiuzzz.debts.ui.adapter.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.tadiuzzz.debts.databinding.DebtItemBinding;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;

/**
 * Created by Simonov.vv on 23.07.2019.
 */
public class DebtPOJOViewHolder extends RecyclerView.ViewHolder {

    DebtItemBinding binding;

    public DebtPOJOViewHolder(DebtItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(DebtPOJO debtPOJO) {
        binding.setDebtPOJO(debtPOJO);
        binding.executePendingBindings();
    }
}
