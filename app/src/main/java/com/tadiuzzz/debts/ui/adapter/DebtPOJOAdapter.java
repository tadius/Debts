package com.tadiuzzz.debts.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.databinding.DebtItemBinding;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.ui.adapter.viewholder.DebtPOJOViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simonov.vv on 03.06.2019.
 */
public class DebtPOJOAdapter extends RecyclerView.Adapter<DebtPOJOViewHolder> {

    private List<DebtPOJO> debtPOJOs = new ArrayList<>();
    private OnDebtPOJOClickListener onDebtPOJOClickListener;

    public void setOnDebtPOJOClickListener(OnDebtPOJOClickListener onDebtPOJOClickListener){
        this.onDebtPOJOClickListener = onDebtPOJOClickListener;
    }

    public void setData(List<DebtPOJO> debtPOJOs) {
        this.debtPOJOs.clear();
        this.debtPOJOs.addAll(debtPOJOs);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DebtPOJOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        DebtItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.debt_item, parent, false);
        binding.setListener(onDebtPOJOClickListener);

        return new DebtPOJOViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtPOJOViewHolder holder, int position) {
        holder.bind(debtPOJOs.get(position));
    }

    @Override
    public int getItemCount() {
        return debtPOJOs.size();
    }

}
