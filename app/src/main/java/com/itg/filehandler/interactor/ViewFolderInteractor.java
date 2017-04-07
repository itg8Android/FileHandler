package com.itg.filehandler.interactor;

import com.itg.filehandler.category.FolderModel;
import com.itg.filehandler.model.FileItemModel;
import com.itg.filehandler.model.FolderItem;

import java.util.ArrayList;



public interface ViewFolderInteractor {
    void onFileListAvailable(ArrayList<FolderItem> fileList);
    void onFolderEmpty();
}
