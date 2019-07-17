package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditCategoryViewModel extends ViewModel {

    private CompositeDisposable disposables;

    private DebtRepository debtRepository;

    private final SingleLiveEvent<String> showToast = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();

    private final MutableLiveData<Boolean> showConfirmDeleteDialog = new MutableLiveData<>();

    private MutableLiveData<Category> loadedLiveDataCategory = new MutableLiveData<>();

    @Inject
    public EditCategoryViewModel(DebtRepository debtRepository) {
        this.debtRepository = debtRepository;

        disposables = new CompositeDisposable();

    }

    public void gotPickedCategory(int categoryId) { //Подгружаем из базы выбранную категорию в объект LiveData
        loadData(categoryId);
    }

    private void loadData(int categoryId) {
        disposables.add(debtRepository.getCategoryByID(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableMaybeObserver<Category>() {
                    @Override
                    public void onSuccess(Category category) {
                        loadedLiveDataCategory.setValue(category);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    public LiveData<Category> getCategory() {
        return loadedLiveDataCategory;
    }

    public SingleLiveEvent getShowToastEvent() {
        return showToast;
    }

    public SingleLiveEvent getNavigateToPreviousScreenEvent() {
        return navigateToPreviousScreen;
    }

    public LiveData<Boolean> getShowConfirmDeleteDialogEvent() {
        return showConfirmDeleteDialog;
    }

    public void saveButtonClicked(String enteredCategoryName) {
        if (loadedLiveDataCategory.getValue() != null) {
            if (loadedLiveDataCategory.getValue().getName().equals(enteredCategoryName)) {
                showToast.callWithArgument("Название категории не изменилось!");
            } else if (enteredCategoryName.isEmpty()) {
                showToast.callWithArgument("Введите название категории!");
            } else {
                loadedLiveDataCategory.getValue().setName(enteredCategoryName);
                updateCategory(enteredCategoryName);
            }
        } else {
            if (!enteredCategoryName.isEmpty()) {
                insertCategory(enteredCategoryName);
            } else {
                showToast.callWithArgument("Введите название категории!");
            }
        }
    }

    private void updateCategory(String enteredCategoryName) {
        disposables.add(debtRepository.updateCategory(loadedLiveDataCategory.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        showToast.callWithArgument("Категория обновлена!");
                        navigateToPreviousScreen.call();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast.callWithArgument("Ошибка при сохранении!");
                    }
                }));
    }

    private void insertCategory(String enteredCategoryName) {
        disposables.add(debtRepository.insertCategory(new Category(enteredCategoryName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        showToast.callWithArgument("Категория сохранена!");
                        navigateToPreviousScreen.call();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast.callWithArgument("Ошибка при сохранении!");
                    }
                }));
    }

    public void deleteButtonClicked() {
        if (loadedLiveDataCategory.getValue() != null) {
            showConfirmDeleteDialog.postValue(true);
        } else {
            showToast.callWithArgument("Такой категории не существует!");
        }
    }

    public void canceledDelete() {
        showConfirmDeleteDialog.postValue(false);
    }

    public void confirmedDelete() {
        if (loadedLiveDataCategory.getValue() != null) {
            deleteCategory();
            showConfirmDeleteDialog.postValue(false);
        }
    }

    private void deleteCategory() {
        disposables.add(debtRepository.deleteCategory(loadedLiveDataCategory.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        showToast.callWithArgument("Категория удалена!");
                        navigateToPreviousScreen.call();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast.callWithArgument("Ошибка при удалении!");
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
