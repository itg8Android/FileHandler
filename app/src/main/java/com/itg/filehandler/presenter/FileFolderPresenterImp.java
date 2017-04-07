package com.itg.filehandler.presenter;

import android.content.Context;
import android.util.Log;

import com.itg.filehandler.interactor.FileFolderModuleInteractor;
import com.itg.filehandler.interactor.FileFolderPreseterInteractor;
import com.itg.filehandler.module.FileFolderModuleImp;
import com.itg.filehandler.view.FileFolderView;

import java.io.File;
import java.util.ArrayList;



public class FileFolderPresenterImp implements FileFolderPresenter, FileFolderPreseterInteractor {


    private static final String TAG = FileFolderPresenterImp.class.getSimpleName();
    private FileFolderView view;
    FileFolderModuleInteractor interactor;

    public FileFolderPresenterImp(FileFolderView view) {
        this.view = view;
        interactor=new FileFolderModuleImp((Context)view);
    }

    @Override
    public void onInitItems() {
        interactor.onFileInit(this);
    }

    @Override
    public void onDeleteItem(File file, int item) {
        interactor.onFileDelete(this,file,item);
    }

    @Override
    public void onListOfFolderAvail(ArrayList<String> dirList, ArrayList<File> directoryList) {
        if(hasView()){
            view.onFolderStructureAvail(dirList,directoryList);
        }
    }

    private boolean hasView() {
        return view!=null;
    }

    @Override
    public void onNoFileAvail() {
        if(hasView())
            view.onNoFileAvail();
    }

    @Override
    public void onFail(Throwable t) {
        Log.d(TAG,"trowable err: "+t);
    }

    @Override
    public void onFileDeleted(int position) {
        if(hasView()){
            view.onFileDeleted(position);
        }
    }

    @Override
    public void onFileDeleteFail(int position) {
        if(hasView()){
            view.onFileDeleteFail(position);
        }
    }
}
