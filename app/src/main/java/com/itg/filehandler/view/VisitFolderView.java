package com.itg.filehandler.view;

import com.itg.filehandler.model.FolderItem;

import java.util.ArrayList;
import java.util.List;



public interface VisitFolderView {
    void onFolderListAvail(ArrayList<FolderItem> names);

    void emptyFolder();
}
