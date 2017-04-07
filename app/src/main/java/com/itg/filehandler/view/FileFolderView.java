package com.itg.filehandler.view;

import com.itg.filehandler.model.FileItemModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public interface FileFolderView {
    void onFolderStructureAvail(ArrayList<String> list, ArrayList<File> directoryList);

    void onNoFileAvail();

    void onFileDeleted(int position);

    void onFileDeleteFail(int position);
}
