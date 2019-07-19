package com.tadiuzzz.debts;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.michaelflisar.changelog.ChangelogBuilder;
import com.michaelflisar.changelog.internal.ChangelogDialogFragment;
import com.tadiuzzz.debts.ui.presentation.ViewModelProviderFactory;
import com.tadiuzzz.debts.utils.workers.BackupWorker;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    private static final String BACKUP_WORK_TAG = "BACKUP_WORK_TAG";

    NavController navController;

    public static final int REQUEST_CODE_PERMISSIONS = 2;

    private MainViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this, providerFactory).get(MainViewModel.class);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        showChangelog();

        NavigationUI.setupActionBarWithNavController(this, navController);

        //This is the subclass of our WorkRequest
        final PeriodicWorkRequest workRequest
                = new PeriodicWorkRequest.Builder(BackupWorker.class, 3, TimeUnit.HOURS)
                .build();
        //создаем уникальный Work, чтобы не создавался новый Work после каждого запуска приложения
        WorkManager.getInstance().enqueueUniquePeriodicWork(BACKUP_WORK_TAG, ExistingPeriodicWorkPolicy.KEEP, workRequest);

    }

    private void showChangelog() {
        int versionCode = BuildConfig.VERSION_CODE;

        ChangelogDialogFragment builder = new ChangelogBuilder()
                .withUseBulletList(true) // true if you want to show bullets before each changelog row, false otherwise
                .withMinVersionToShow(versionCode)     // provide a number and the log will only show changelog rows for versions equal or higher than this number
                .withManagedShowOnStart(true)  // library will take care to show activity/dialog only if the changelog has new infos and will only show this new infos
                .withRateButton(true) // enable this to show a "rate app" button in the dialog => clicking it will open the play store; the parent activity or target fragment can also implement IChangelogRateHandler to handle the button click
                .withTitle("Список изменений") // provide a custom title if desired, default one is "Changelog <VERSION>"
                .withOkButtonLabel("ОК") // provide a custom ok button text if desired, default one is "OK"
                .withRateButtonLabel("Поставить оценку") // provide a custom rate button text if desired, default one is "Rate"
                .buildAndShowDialog(this, false); // second parameter defines, if the dialog has a dark or light theme
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.popBackStack();
        return true;
    }
}
