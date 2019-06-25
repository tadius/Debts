package com.tadiuzzz.debts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Simonov.vv on 20.06.2019.
 */
public class FileUtils {

    public static List<String> getListOfFiles(String path) {
        File folder = new File(path);
        List<String> listOfFiles;
        if (folder.exists()) {
            final File[] files = folder.listFiles();

            //сортируем в обратном порядке по дате (сначала новые)
            Arrays.sort(files, (file1, file2) -> Long.compare(file2.lastModified(), file1.lastModified()));

        ArrayList<String> allFilesNames = new ArrayList<>();

        for (File file : files) {
            allFilesNames.add(file.getName());
        }
        listOfFiles = allFilesNames;
    } else

    {
        listOfFiles = new ArrayList<>();
    }
        return listOfFiles;
}

    public static void createFoldersForBackup(String fullPath) {
        File namedFolder = new File(fullPath);
        boolean successMakeNamedDir = true;
        if (!namedFolder.exists())
            successMakeNamedDir = namedFolder.mkdirs();
    }

    public static void copyFile(String fromFile, String toFile) throws IOException {
        File file = new File(fromFile);

        FileInputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = new FileOutputStream(toFile);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

}