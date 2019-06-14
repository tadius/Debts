package com.tadiuzzz.debts.ui.view;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tadiuzzz.debts.data.CacheEditing;
import com.tadiuzzz.debts.ui.presentation.DebtsViewModel;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.Debt;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.domain.entity.Person;
import com.tadiuzzz.debts.ui.adapter.DebtPOJOsAdapter;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.rvDebts)
    RecyclerView rvDebts;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_debts, container, false);

        ButterKnife.bind(this, view);
        CacheEditing.getInstance().clearCachedDebtPOJO();

        DebtPOJOsAdapter debtPOJOsAdapter = new DebtPOJOsAdapter();
        rvDebts.setAdapter(debtPOJOsAdapter);

        debtPOJOsAdapter.setOnDebtPOJOClickListener(new DebtPOJOsAdapter.OnDebtPOJOClickListener() {
            @Override
            public void onDebtPOJOClick(DebtPOJO debtPOJO) {
                debtsViewModel.clickedOnDebtPOJO(debtPOJO);
            }
        });

        rvDebts.setLayoutManager(new LinearLayoutManager(getActivity()));

        debtsViewModel = ViewModelProviders.of(this).get(DebtsViewModel.class);

        debtsViewModel.getLiveDataDebtPOJOs().observe(this, new Observer<List<DebtPOJO>>() {
            @Override
            public void onChanged(List<DebtPOJO> debtPOJOS) {
                debtPOJOsAdapter.setData(debtPOJOS);
                debtPOJOsAdapter.notifyDataSetChanged();
            }
        });

        debtsViewModel.navigateToEditDebtScreen().observe(this, new Observer() {
            @Override
            public void onChanged(Object o) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.action_debtsFragment_to_editDebtFragment);
            }
        });

        return view;
    }












    // *********************************************************
    // *********************тестовые методы*********************
    // *********************************************************

    private void addRandomPerson() {
        Random random = new Random();
        int i = random.nextInt(100);
        Person person = new Person("Name" + i, "SecondName" + i);
        debtsViewModel.insertPerson(person)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Log.i(TAG, "Person inserted!");
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void addRandomCategory() {
        Random random = new Random();
        int i = random.nextInt(100);
        Category category = new Category("Category" + i);
        debtsViewModel.insertCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                Log.i(TAG, "Category inserted!");
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void addRandomDebt(int personId, int categoryId) {
        Random random = new Random();
        int i = random.nextInt(100);
        String description = "Debt " + i;
        long amount = 5000L + i;
        long dateOfStart = 1343805819061L * i;
        long dateOfEnd = 1380583119061L * i;
        long dateOfExpiration = 1343128059061L * i;
        boolean isReturned = false;
        boolean isActive = true;
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
