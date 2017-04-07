package com.itg.filehandler.presenter;

import java.io.File;



public interface FileFolderPresenter {
    void onInitItems();

    void onDeleteItem(File file, int item);
}
