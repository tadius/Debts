package com.tadiuzzz.debts.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;

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
    private OnDebtPOJOClickListener onDebtPOJOClickListener;

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

        holder.tvDebtItemPersonName.setText(debtPOJO.getDebtPerson().getName());
        holder.tvDebtItemCategoryName.setText(String.valueOf(debtPOJO.getDebtCategory().getName()));
        holder.tvDebtItemDescription.setText(String.valueOf(debtPOJO.getDebt().getDescription()));
        holder.tvDebtItemAmount.setText(String.valueOf(debtPOJO.getDebt().getAmount()));

        if (debtPOJO.getDebt().getDateOfExpiration() != 0) {
            holder.llDebtItemDateOfExpirationContainer.setVisibility(View.VISIBLE);
            holder.tvDebtItemDateOfExpiration.setText(simpleDateFormat.format(new Date(debtPOJO.getDebt().getDateOfExpiration())));
        } else {
            holder.llDebtItemDateOfExpirationContainer.setVisibility(View.INVISIBLE);
        }
        if (debtPOJO.getDebt().getDateOfStart() != 0) {
            holder.llDebtItemDateOfStartContainer.setVisibility(View.VISIBLE);
            holder.tvDebtItemDateOfStart.setText(simpleDateFormat.format(new Date(debtPOJO.getDebt().getDateOfStart())));
        } else {
            holder.llDebtItemDateOfStartContainer.setVisibility(View.INVISIBLE);
        }
        if (debtPOJO.getDebt().getDateOfEnd() != 0) {
            holder.llDebtItemDateOfEndContainer.setVisibility(View.VISIBLE);
            holder.tvDebtItemDateOfEnd.setText(simpleDateFormat.format(new Date(debtPOJO.getDebt().getDateOfEnd())));
        } else {
            holder.llDebtItemDateOfEndContainer.setVisibility(View.INVISIBLE);
        }

        holder.llDebtItemContainer.setBackgroundResource(R.color.debtItemBackground);

        Date dateNow = new Date();
        if (debtPOJO.getDebt().getDateOfExpiration() <= dateNow.getTime()) holder.llDebtItemContainer.setBackgroundResource(R.color.debtDateExpiredItemBackground);

        if (debtPOJO.getDebt().isReturned()) holder.llDebtItemContainer.setBackgroundResource(R.color.debtReturnedItemBackground);
    }

    @Override
    public int getItemCount() {
        return debtPOJOs == null ? 0 : debtPOJOs.size();
    }

    public class DebtPOJOViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llDebtItemContainer) LinearLayout llDebtItemContainer;
        @BindView(R.id.llDebtItemDateOfStartContainer) LinearLayout llDebtItemDateOfStartContainer;
        @BindView(R.id.tvDebtItemDateOfStart) TextView tvDebtItemDateOfStart;
        @BindView(R.id.llDebtItemDateOfExpirationContainer) LinearLayout llDebtItemDateOfExpirationContainer;
        @BindView(R.id.tvDebtItemDateOfExpiration) TextView tvDebtItemDateOfExpiration;
        @BindView(R.id.llDebtItemDateOfEndContainer) LinearLayout llDebtItemDateOfEndContainer;
        @BindView(R.id.tvDebtItemDateOfEnd) TextView tvDebtItemDateOfEnd;
        @BindView(R.id.tvDebtItemPersonName) TextView tvDebtItemPersonName;
        @BindView(R.id.tvDebtItemCategoryName) TextView tvDebtItemCategoryName;
        @BindView(R.id.tvDebtItemAmount) TextView tvDebtItemAmount;
        @BindView(R.id.tvDebtItemDescription) TextView tvDebtItemDescription;


        public DebtPOJOViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(onDebtPOJOClickListener != null && position != RecyclerView.NO_POSITION) {
                    onDebtPOJOClickListener.onDebtPOJOClick(debtPOJOs.get(position));
                }
            });

        }
    }

    public interface OnDebtPOJOClickListener{
        void onDebtPOJOClick(DebtPOJO debtPOJO);
    }

    public void setOnDebtPOJOClickListener(DebtPOJOsAdapter.OnDebtPOJOClickListener onDebtPOJOClickListener){
        this.onDebtPOJOClickListener = onDebtPOJOClickListener;
    }


}
