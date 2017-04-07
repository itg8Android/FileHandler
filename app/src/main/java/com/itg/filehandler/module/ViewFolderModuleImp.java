package com.itg.filehandler.module;

import com.itg.filehandler.common.CommonMethod;
import com.itg.filehandler.interactor.ViewFolderInteractor;
import com.itg.filehandler.model.FolderItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ViewFolderModuleImp implements VieFolderModule {

    @Override
    public void onStartListingFiles(File file, ViewFolderInteractor listener) {
        File[] listOfFile = CommonMethod.getListOfDir(file);

        if (listOfFile == null || listOfFile.length <= 0) {
            listener.onFolderEmpty();
            return;
        }

        ArrayList<FolderItem> itemsList = new ArrayList<>();
        ArrayList<FolderItem> folderList = new ArrayList<>();
        ArrayList<FolderItem> fileList = new ArrayList<>();
        for (File item :
                listOfFile) {
            FolderItem fItem = new FolderItem();
            fItem.setFileName(item.getName());
            fItem.setFilePath(item.getAbsolutePath());
            if (item.isDirectory()) {
                fItem.setType(CommonMethod.FOLDER);
                folderList.add(fItem);
            } else {
                fItem.setType(CommonMethod.FILE);
                fItem.setSize(String.valueOf(item.length() / 1024));
                fileList.add(fItem);
            }

        }

        if (folderList.size() > 0) {
            sort(folderList);
            itemsList.addAll(folderList);
        }

        if (fileList.size() > 0) {
            sort(fileList);
            itemsList.addAll(fileList);
        }
        listener.onFileListAvailable(itemsList);
    }

    private void sort(ArrayList<FolderItem> list) {
        Collections.sort(list, new Comparator<FolderItem>() {
            @Override
            public int compare(FolderItem folderItem, FolderItem t1) {
                return folderItem.getFileName().compareToIgnoreCase(t1.getFileName());
            }
        });
    }
}
