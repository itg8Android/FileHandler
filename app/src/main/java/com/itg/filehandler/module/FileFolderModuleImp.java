package com.itg.filehandler.module;

import android.content.Context;
import android.os.Environment;

import com.itg.filehandler.R;
import com.itg.filehandler.common.CommonMethod;
import com.itg.filehandler.interactor.FileFolderModuleInteractor;
import com.itg.filehandler.interactor.FileFolderPreseterInteractor;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;



public class FileFolderModuleImp implements FileFolderModuleInteractor {


    private File mainPath;
    private ArrayList<File> directoryList;
    private ArrayList<String> directoryNames;

    public FileFolderModuleImp(Context context) {
        mainPath = new File(Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.app_name));
        if (!mainPath.exists())
            mainPath.mkdir();
    }


    @Override
    public void onFileInit(FileFolderPreseterInteractor listner) {

        File[] tempDirectoryList = CommonMethod.getListOfDir(mainPath);

        if(tempDirectoryList==null || tempDirectoryList.length<=0){
            listner.onNoFileAvail();
            return;
        }
        directoryList = new ArrayList<File>();
        directoryNames = new ArrayList<String>();
        for (File file : tempDirectoryList) {
            directoryList.add(file);
            directoryNames.add(file.getName());
        }

        listner.onListOfFolderAvail(directoryNames,directoryList);
    }

    @Override
    public void onFileDelete(FileFolderPreseterInteractor listener,File file, int position) {
                if(file.exists() && file.isDirectory()){
                    deleteRecursive(file);
                    listener.onFileDeleted(position);
                }else {
                    listener.onFileDeleteFail(position);
                }
    }


    void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

            fileOrDirectory.delete();
    }

}
