package com.tadiuzzz.debts.ui.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.ui.presentation.BackupRestoreViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class BackupRestoreFragment extends Fragment {

    private final int REQUEST_PERMISSION_CODE = 222;

    private BackupRestoreViewModel viewModel;
    private List<String> listOfFiles;

    @BindView(R.id.tvLastBackupName)
    TextView tvLastBackupName;
    @BindView(R.id.btnBackupDB)
    Button btnBackupDB;
    @BindView(R.id.btnRestoreDB)
    Button btnRestoreDB;

    @OnClick(R.id.btnBackupDB)
    void onBackUpButtonClick() {
        viewModel.clickedOnBackupButton();
    }

    @OnClick(R.id.btnRestoreDB)
    void onRestoreButtonClick() {
        viewModel.clickedOnRestoreButton();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backup_restore_db, container, false);

        ButterKnife.bind(this, view);

        viewModel = ViewModelProviders.of(this).get(BackupRestoreViewModel.class);

        subscribeOnData();

        subscribeOnNavigationEvents();

        subscribeOnDialogsEvents();

        subscribeOnNotificationsEvents();

        return view;
    }

    private void subscribeOnData() {
        viewModel.getListOfFiles().observe(getViewLifecycleOwner(), list -> listOfFiles = list);
    }

    private void subscribeOnNotificationsEvents() {
        viewModel.getShowToastEvent().observe(getViewLifecycleOwner(), message -> showToast((String) message));
    }

    private void subscribeOnNavigationEvents() {
        viewModel.getNavigateToPreviousScreenEvent().observe(getViewLifecycleOwner(), o -> Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack());
    }

    private void subscribeOnDialogsEvents() {
        viewModel.getShowRequestPermissionsDialogEvent().observe(getViewLifecycleOwner(), o -> showRequestPermissionsDialog());
        viewModel.getShowSetNameOfBackupDialogEvent().observe(getViewLifecycleOwner(), o -> showSetNameOfBackupDialog());
        viewModel.getShowPickFileDialogEvent().observe(getViewLifecycleOwner(), o -> showPickFileDialog());
        viewModel.getShowConfirmRestoreDialogEvent().observe(getViewLifecycleOwner(), pathToFile -> showConfirmRestoreDialog((String)pathToFile));
        viewModel.getShowConfirmDeleteDialogEvent().observe(getViewLifecycleOwner(), pathToFile -> showConfirmDeleteDialog((String)pathToFile));
    }

    private void showConfirmRestoreDialog(String pathToFile) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Подтверждение восстановления");
        builderSingle.setMessage(String.format("%s \nПосле восстановления резервной копии, текущие данные будут удалены. Восстановить? ", pathToFile));

        builderSingle.setNegativeButton("Отмена", (dialog, which) -> {
            viewModel.clickedOnRestoreButton();
        });

        builderSingle.setPositiveButton("Восстановить", (dialog, which) -> {
            viewModel.confirmedFileRestore(pathToFile);
        });

        builderSingle.show();
    }

    private void showConfirmDeleteDialog(String pathToFile) {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Подтверждение удаления");
        builderSingle.setMessage(String.format("%s \nВы действительно хотите удалить резервную копию? ", pathToFile));

        builderSingle.setNegativeButton("Отмена", (dialog, which) -> {
            viewModel.clickedOnRestoreButton();
        });

        builderSingle.setPositiveButton("Удалить", (dialog, which) -> {
            viewModel.confirmedFileDelete(pathToFile);
        });

        builderSingle.show();
    }

    private void showRequestPermissionsDialog() {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.permissionsGranted();
            } else {
                viewModel.permissionsCanceled();
            }
        }
    }

    private void showSetNameOfBackupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Название резервной копии");
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Сохранить", (dialog, which) -> {
            String backupName = input.getText().toString();
            viewModel.enteredBackupName(backupName);
        });
        builder.show();
    }

    private void showPickFileDialog() {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_singlechoice);
        arrayAdapter.addAll(listOfFiles);

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Резервная копия для восстановления:");
        builderSingle.setSingleChoiceItems(arrayAdapter, 0, (dialog, which) -> {});

        builderSingle.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());

        builderSingle.setPositiveButton("Восстановить", (dialog, which) -> {
            int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
            viewModel.pickedFileToRestore(listOfFiles.get(selectedPosition));
        });

        builderSingle.setNeutralButton("Удалить", (dialog, which) -> {
            int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
            viewModel.pickedFileToDelete(listOfFiles.get(selectedPosition));
        });

        builderSingle.show();
    }

    private void showToast(String text) {
        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
