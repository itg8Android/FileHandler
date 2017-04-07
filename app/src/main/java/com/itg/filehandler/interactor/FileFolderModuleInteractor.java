package com.itg.filehandler.interactor;

import java.io.File;



public interface FileFolderModuleInteractor {
    void onFileInit(FileFolderPreseterInteractor listner);
    void onFileDelete(FileFolderPreseterInteractor listener,File file, int position);
}
