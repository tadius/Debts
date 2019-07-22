package com.tadiuzzz.debts.ui.view;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.databinding.FragmentAboutAppBinding;
import com.tadiuzzz.debts.ui.presentation.AboutAppViewModel;
import com.tadiuzzz.debts.ui.presentation.ViewModelProviderFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerFragment;

/**
 * Created by Simonov.vv on 25.06.2019.
 */
public class AboutAppFragment extends DaggerFragment {

    private AboutAppViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    void onAuthorEmailClicked() {
        viewModel.clickedOnAuthorEmail();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_about_app, container, false);
        viewModel = ViewModelProviders.of(this, providerFactory).get(AboutAppViewModel.class);

        FragmentAboutAppBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about_app, container, false);
        binding.setModel(viewModel);


        subscribeToNavigationChanges();

        View view = binding.getRoot();

        return view;
    }

    private void subscribeToNavigationChanges() {
        viewModel.getNavigateSendEmailAppEvent().observe(this, email -> sendEmailToAuthor(email));
    }

    private void sendEmailToAuthor(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(String.format("mailto:%s", email))); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Моя зарплата");
        startActivity(Intent.createChooser(intent, "Отправить письмо: "));
    }

}
