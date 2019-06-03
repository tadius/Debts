package com.tadiuzzz.debts.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.R2;
import com.tadiuzzz.debts.entity.Debt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simonov.vv on 03.06.2019.
 */
public class DebtsAdapter extends RecyclerView.Adapter<DebtsAdapter.DebtViewHolder> {

    private List<Debt> debts;

    public void setData(List<Debt> debts) {
        this.debts = debts;
    }

    @NonNull
    @Override
    public DebtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View debtView = LayoutInflater.from(parent.getContext()).inflate(R.layout.debt_item, parent, false);

        return new DebtViewHolder(debtView);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtViewHolder holder, int position) {
        Debt debt = debts.get(position);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        holder.tvDebtItemDateOfStart.setText(simpleDateFormat.format(new Date(debt.getDateOfStart())));
        holder.tvDebtItemDateOfExpiration.setText(simpleDateFormat.format(new Date(debt.getDateOfEnd())));
        holder.tvDebtItemDateOfEnd.setText(simpleDateFormat.format(new Date(debt.getDateOfExpiration())));
        holder.tvDebtItemPersonName.setText(String.valueOf(debt.getPersonId()));
        holder.tvDebtItemCategoryName.setText(String.valueOf(debt.getCategoryId()));
        holder.tvDebtItemAmount.setText(String.valueOf(debt.getAmount()));
        holder.tvDebtItemDescription.setText(String.valueOf(debt.getDescription()));
    }

    @Override
    public int getItemCount() {
        return debts == null ? 0 : debts.size();
    }

    public class DebtViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tvDebtItemDateOfStart) TextView tvDebtItemDateOfStart;
        @BindView(R2.id.tvDebtItemDateOfExpiration) TextView tvDebtItemDateOfExpiration;
        @BindView(R2.id.tvDebtItemDateOfEnd) TextView tvDebtItemDateOfEnd;
        @BindView(R2.id.tvDebtItemPersonName) TextView tvDebtItemPersonName;
        @BindView(R2.id.tvDebtItemCategoryName) TextView tvDebtItemCategoryName;
        @BindView(R2.id.tvDebtItemAmount) TextView tvDebtItemAmount;
        @BindView(R2.id.tvDebtItemDescription) TextView tvDebtItemDescription;


        public DebtViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
