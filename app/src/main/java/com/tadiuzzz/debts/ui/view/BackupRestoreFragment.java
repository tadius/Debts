package com.tadiuzzz.debts.ui.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tadiuzzz.debts.FileUtils;
import com.tadiuzzz.debts.Permissions;
import com.tadiuzzz.debts.R;
import com.tadiuzzz.debts.domain.entity.Category;
import com.tadiuzzz.debts.ui.adapter.CategoryAdapter;
import com.tadiuzzz.debts.ui.presentation.CategoriesViewModel;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Simonov.vv on 31.05.2019.
 */
public class BackupRestoreFragment extends Fragment {

     public static final String TAG = "logTag";

    public static final String DATABASE_NAME = "debts_database";
    public static final String DATABASE_NAME_SHM = "debts_database-shm";
    public static final String DATABASE_NAME_WAL = "debts_database-wal";
    String outFileName = "";

    @BindView(R.id.tvLastBackupName)
    TextView tvLastBackupName;
    @BindView(R.id.btnBackupDB)
    Button btnBackupDB;
    @BindView(R.id.btnRestoreDB)
    Button btnRestoreDB;

    @OnClick(R.id.btnBackupDB)
    void onBackUpButtonClick() {
//        checkPermissions();
        performBackup(outFileName);
    }

    @OnClick(R.id.btnRestoreDB)
    void onRestoreButtonClick() {
//        checkPermissions();
        performRestore();
        // do restore
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_backup_restore_db, container, false);

        ButterKnife.bind(this, view);

        outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + File.separator;

        return view;
    }



    public void performBackup(final String outFileName) {

        Permissions.verifyStoragePermissions(getActivity());

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getActivity().getResources().getString(R.string.app_name));

        boolean success = true;
        if (!folder.exists())
            success = folder.mkdirs();
        if (success) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Backup Name");
            final EditText input = new EditText(getActivity());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("Save", (dialog, which) -> {
                String m_Text = input.getText().toString();
//                String out = outFileName + m_Text + ".db";
                String out = outFileName;
                backup(out);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        } else
            Toast.makeText(getActivity(), "Unable to create directory. Retry", Toast.LENGTH_SHORT).show();
    }

    public void performRestore() {

        Permissions.verifyStoragePermissions(getActivity());

        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + getActivity().getResources().getString(R.string.app_name));
        if (folder.exists()) {

            final File[] files = folder.listFiles();

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item);
            for (File file : files)
                arrayAdapter.add(file.getName());

            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
            builderSingle.setTitle("Restore:");
            builderSingle.setNegativeButton(
                    "cancel",
                    (dialog, which) -> dialog.dismiss());
            builderSingle.setAdapter(
                    arrayAdapter,
                    (dialog, which) -> {
                        try {
                            importDB(files[which].getPath());
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
                        }
                    });
            builderSingle.show();
        } else
            Toast.makeText(getActivity(), "Backup folder not present.\nDo a backup before a restore!", Toast.LENGTH_SHORT).show();
    }

    public void backup(String outFileName) {

        //database path
        final String inFileName = getActivity().getDatabasePath(DATABASE_NAME).toString();

        write(inFileName, outFileName+DATABASE_NAME);

        final String inFileNameshm = getActivity().getDatabasePath(DATABASE_NAME_SHM).toString();

        write(inFileNameshm, outFileName+DATABASE_NAME_SHM);

        final String inFileNamewal = getActivity().getDatabasePath(DATABASE_NAME_WAL).toString();

        write(inFileNamewal, outFileName+DATABASE_NAME_WAL);

//        try {
//            File dbFile = new File(inFileName);
//            FileInputStream fis = new FileInputStream(dbFile);
//
//            // Open the empty db as the output stream
//            OutputStream output = new FileOutputStream(outFileName);
//
//            // Transfer bytes from the input file to the output file
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = fis.read(buffer)) > 0) {
//                output.write(buffer, 0, length);
//            }
//
//            // Close the streams
//            output.flush();
//            output.close();
//            fis.close();
//
//            Toast.makeText(getActivity(), "Backup Completed", Toast.LENGTH_SHORT).show();
//
//        } catch (Exception e) {
//            Toast.makeText(getActivity(), "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
    }

    private void write(String from, String to) {
        try {
            File dbFile = new File(from);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(to);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            Toast.makeText(getActivity(), "Backup Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void importDB(String inFileName) {

        final String outFileName = getActivity().getDatabasePath(DATABASE_NAME).toString();

        write(inFileName, outFileName);

        final String outFileNameshm = getActivity().getDatabasePath(DATABASE_NAME_SHM).toString();

        write(inFileName+"-shm", outFileNameshm);

        final String outFileNamewal = getActivity().getDatabasePath(DATABASE_NAME_WAL).toString();

        write(inFileName+"-wal", outFileNamewal);



//        try {
//
//            File dbFile = new File(inFileName);
//            FileInputStream fis = new FileInputStream(dbFile);
//
//            // Open the empty db as the output stream
//            OutputStream output = new FileOutputStream(outFileName);
//
//            // Transfer bytes from the input file to the output file
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = fis.read(buffer)) > 0) {
//                output.write(buffer, 0, length);
//            }
//
//            // Close the streams
//            output.flush();
//            output.close();
//            fis.close();
//
//            Toast.makeText(getActivity(), "Import Completed", Toast.LENGTH_SHORT).show();
//
//        } catch (Exception e) {
//            Toast.makeText(getActivity(), "Unable to import database. Retry", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
    }

    private void checkPermissions() {
        int REQUEST_CODE = 222;
        int readPermissionStatus = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermissionStatus = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (readPermissionStatus == PackageManager.PERMISSION_GRANTED && writePermissionStatus == PackageManager.PERMISSION_GRANTED) {
//            doBackup();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE);
        }
    }
}
