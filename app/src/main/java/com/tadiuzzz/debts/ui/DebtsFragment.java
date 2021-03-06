package com.tadiuzzz.debts.ui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.tadiuzzz.debts.DebtsViewModel;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.entity.Debt;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class DebtsFragment extends Fragment {

    private DebtsViewModel debtsViewModel;
    public static final String TAG = "logTag";
    private TextView tvDebts;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_debts, container, false);

        tvDebts = view.findViewById(R.id.tvDebts);

        debtsViewModel = ViewModelProviders.of(this).get(DebtsViewModel.class);

        debtsViewModel.getDebts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<Debt>>() {
            @Override
            public void onNext(List<Debt> debts) {
//                adapter.submitList(notes);
                for (Debt debt : debts) {
                    Log.i(TAG, "onNext: debtId: " + debt.getId());
                    tvDebts.setText(tvDebts.getText() + " - " + String.valueOf(debt.getId()));

                }

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

        addDebt();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                addDebt();
            }
        }, 5000);

        return view;
    }

    private void addDebt(){
        String description = "Debt 1";
        long amount = 100;
        long dateOfStart = 100000;
        long dateOfEnd = 0;
        long dateOfExpiration = 200000;
        boolean isReturned = false;
        boolean isActive = true;
        int categoryId = 1;
        int personId = 1;
        Debt debt = new Debt(description, amount, dateOfStart, dateOfEnd, dateOfExpiration, isReturned, isActive, categoryId, personId);

        debtsViewModel.insertDebt(debt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Log.i(TAG, "Debt inserted! debtId: " + debt.getId());
            }

            @Override
            public void onError(Throwable e) {
//                Log.i(TAG, "Error inserting! debtId: " + debt.getId());
                Log.i(TAG, "Error inserting! debtId: " + e.getLocalizedMessage());
            }
        });
    }
}
