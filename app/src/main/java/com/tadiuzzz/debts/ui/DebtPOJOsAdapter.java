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
import com.tadiuzzz.debts.entity.DebtPOJO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Simonov.vv on 03.06.2019.
 */
public class DebtPOJOsAdapter extends RecyclerView.Adapter<DebtPOJOsAdapter.DebtPOJOViewHolder> {

    private List<DebtPOJO> debtPOJOs;

    public void setData(List<DebtPOJO> debtPOJOs) {
        this.debtPOJOs = debtPOJOs;
    }

    @NonNull
    @Override
    public DebtPOJOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View debtView = LayoutInflater.from(parent.getContext()).inflate(R.layout.debt_item, parent, false);

        return new DebtPOJOViewHolder(debtView);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtPOJOViewHolder holder, int position) {
        DebtPOJO debtPOJO = debtPOJOs.get(position);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        holder.tvDebtItemDateOfStart.setText(simpleDateFormat.format(new Date(debtPOJO.getDebt().getDateOfStart())));
        holder.tvDebtItemDateOfExpiration.setText(simpleDateFormat.format(new Date(debtPOJO.getDebt().getDateOfEnd())));
        holder.tvDebtItemDateOfEnd.setText(simpleDateFormat.format(new Date(debtPOJO.getDebt().getDateOfExpiration())));
        holder.tvDebtItemPersonName.setText(debtPOJO.getDebtPerson().getFirstName() + " " + debtPOJO.getDebtPerson().getSecondName());
        holder.tvDebtItemCategoryName.setText(String.valueOf(debtPOJO.getDebtCategory().getName()));
        holder.tvDebtItemAmount.setText(String.valueOf(debtPOJO.getDebt().getAmount()));
        holder.tvDebtItemDescription.setText(String.valueOf(debtPOJO.getDebt().getDescription()));
    }

    @Override
    public int getItemCount() {
        return debtPOJOs == null ? 0 : debtPOJOs.size();
    }

    public class DebtPOJOViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.tvDebtItemDateOfStart) TextView tvDebtItemDateOfStart;
        @BindView(R2.id.tvDebtItemDateOfExpiration) TextView tvDebtItemDateOfExpiration;
        @BindView(R2.id.tvDebtItemDateOfEnd) TextView tvDebtItemDateOfEnd;
        @BindView(R2.id.tvDebtItemPersonName) TextView tvDebtItemPersonName;
        @BindView(R2.id.tvDebtItemCategoryName) TextView tvDebtItemCategoryName;
        @BindView(R2.id.tvDebtItemAmount) TextView tvDebtItemAmount;
        @BindView(R2.id.tvDebtItemDescription) TextView tvDebtItemDescription;


        public DebtPOJOViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }


}
