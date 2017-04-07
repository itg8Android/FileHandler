package com.itg.filehandler.module;

import com.itg.filehandler.interactor.ViewFolderInteractor;

import java.io.File;



public interface VieFolderModule {
    void onStartListingFiles(File file, ViewFolderInteractor listener);
}
