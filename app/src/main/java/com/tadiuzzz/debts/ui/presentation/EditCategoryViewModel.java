package com.tadiuzzz.debts.ui.presentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.tadiuzzz.debts.data.DebtRepository;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.ui.SingleLiveEvent;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class EditCategoryViewModel extends AndroidViewModel {

    DebtRepository debtRepository;


    private final SingleLiveEvent<String> showToast = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> navigateToPreviousScreen = new SingleLiveEvent<>();

    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<Category> loadedLiveDataCategory = new MutableLiveData<>();

    public EditCategoryViewModel(@NonNull Application application) {
        super(application);

        title.setValue("Редактирование категории");

        debtRepository = new DebtRepository(application);
    }

    public void gotPickedCategory(int categoryId) { //Подгружаем из базы выбранную категорию в объект LiveData
        debtRepository.getCategoryByID(categoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableMaybeObserver<Category>() {
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
                });
    }

    public LiveData<String> getTitle() {
        return title;
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

    public void saveButtonClicked(String enteredCategoryName) {
        if (loadedLiveDataCategory.getValue() != null) {
            if (loadedLiveDataCategory.getValue().getName().equals(enteredCategoryName)) {
                showToast.callWithArgument("Название категории не изменилось!");
            } else if (enteredCategoryName.isEmpty()) {
                showToast.callWithArgument("Введите название категории!");
            } else {
                loadedLiveDataCategory.getValue().setName(enteredCategoryName);
                debtRepository.updateCategory(loadedLiveDataCategory.getValue())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                showToast.callWithArgument("Категория обновлена!");
                                navigateToPreviousScreen.call();
                            }

                            @Override
                            public void onError(Throwable e) {
                                showToast.callWithArgument("Ошибка при сохранении!");
                            }
                        });
            }
        } else {
            if (!enteredCategoryName.isEmpty()) {
                debtRepository.insertCategory(new Category(enteredCategoryName))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableCompletableObserver() {
                            @Override
                            public void onComplete() {
                                showToast.callWithArgument("Категория сохранена!");
                                navigateToPreviousScreen.call();
                            }

                            @Override
                            public void onError(Throwable e) {
                                showToast.callWithArgument("Ошибка при сохранении!");
                            }
                        });
            } else {
                showToast.callWithArgument("Введите название категории!");
            }
        }
    }

    public void deleteButtonClicked() {
        if (loadedLiveDataCategory.getValue() != null) {
            debtRepository.deleteCategory(loadedLiveDataCategory.getValue())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            showToast.callWithArgument("Категория удалена!");
                            navigateToPreviousScreen.call();
                        }

                        @Override
                        public void onError(Throwable e) {
                            showToast.callWithArgument("Ошибка при удалении!");
                        }
                    });
        } else {
            showToast.callWithArgument("Такой категории не существует!");
        }
    }

}
