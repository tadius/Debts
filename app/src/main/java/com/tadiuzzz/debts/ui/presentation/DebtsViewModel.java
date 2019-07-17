package com.tadiuzzz.debts.ui.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.domain.entity.DebtPOJO;
import com.tadiuzzz.debts.ui.SingleLiveEvent;
import com.tadiuzzz.debts.utils.FilterManager;
import com.tadiuzzz.debts.utils.SortingManager;

import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class DebtsViewModel extends ViewModel {

    private CompositeDisposable disposables;

    private DebtRepository debtRepository;
    private SortingManager sortingManager;
    private FilterManager filterManager;

    private SingleLiveEvent<Void> navigateToEditDebtScreen = new SingleLiveEvent<>();

    private MutableLiveData<List<DebtPOJO>> listOfDebtPOJOS = new MutableLiveData<>();
    private boolean amIBorrower;

    @Inject
    public DebtsViewModel(DebtRepository debtRepository, SortingManager sortingManager, FilterManager filterManager) {

        this.debtRepository = debtRepository;
        this.sortingManager = sortingManager;
        this.filterManager = filterManager;

        disposables = new CompositeDisposable();

        disposables.add(sortingManager.getComparator().subscribeWith(new DisposableObserver<Comparator<DebtPOJO>>() {
            @Override
            public void onNext(Comparator<DebtPOJO> comparator) {
                loadAllDebtPOJOs(comparator);
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {}
        }));

    }

    private void loadAllDebtPOJOs(Comparator<DebtPOJO> comparator) {

        disposables.add(debtRepository.getAllDebtPOJOs()
                .flatMap(debtPOJOS -> Flowable.fromIterable(debtPOJOS)
                        .filter(debtPOJO -> debtPOJO.getDebt().amIBorrower() == amIBorrower)
                        .filter(debtPOJO -> {
                            for (Category filteredCategory : filterManager.getFilteredCategories()) {
                                if(filteredCategory.getId() == debtPOJO.getDebtCategory().getId()) return true;
                            }
                            return false;
                        })
                        .toSortedList(comparator)
                        .toFlowable()
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<DebtPOJO>>() {
                    @Override
                    public void onNext(List<DebtPOJO> debtPOJOS) {
                        listOfDebtPOJOS.setValue(debtPOJOS);
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                }));

    }

    public LiveData<List<DebtPOJO>> getListOfDebtPOJOS() { //Предоставляем объект LiveData View для подписки
        return listOfDebtPOJOS;
    }

    public SingleLiveEvent getNavigateToEditDebtScreenEvent() {
        return navigateToEditDebtScreen;
    }

    public void clickedOnDebtPOJO(DebtPOJO debtPOJO) {
        debtRepository.putDebtPOJOtoCache(debtPOJO);

        navigateToEditDebtScreen.call();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public void setAmIBorrower(boolean amIBorrower) {
        this.amIBorrower = amIBorrower;
    }

}
