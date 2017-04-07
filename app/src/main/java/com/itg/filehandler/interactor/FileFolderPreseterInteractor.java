package com.itg.filehandler.interactor;

import java.io.File;
import java.util.ArrayList;

public interface    FileFolderPreseterInteractor {
    void onListOfFolderAvail(ArrayList<String> dirList, ArrayList<File> directoryList);
    void onNoFileAvail();
    void onFail(Throwable t);
    void onFileDeleted(int position);
    void onFileDeleteFail(int position);
}
