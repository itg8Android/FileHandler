package com.itg.filehandler.presenter;

import com.itg.filehandler.category.VisitFolderActivity;
import com.itg.filehandler.interactor.ViewFolderInteractor;
import com.itg.filehandler.model.FolderItem;
import com.itg.filehandler.module.VieFolderModule;
import com.itg.filehandler.module.ViewFolderModuleImp;
import com.itg.filehandler.view.VisitFolderView;

import java.io.File;
import java.util.ArrayList;


public class ViewFolderPresenterImp implements ViewFolderPresenter,ViewFolderInteractor {


    private VisitFolderView view;
    private VieFolderModule module;

    public ViewFolderPresenterImp(VisitFolderView view) {
        this.view = view;
        module=new ViewFolderModuleImp();
    }

    private boolean hasView(){
        return view!=null;
    }

    @Override
    public void onInitFileList(File file) {
        module.onStartListingFiles(file,this);
    }

    @Override
    public void onFileListAvailable(ArrayList<FolderItem> fileList) {
        if(hasView())
            view.onFolderListAvail(fileList);
    }

    @Override
    public void onFolderEmpty() {
        if(hasView())
            view.emptyFolder();
    }
}
