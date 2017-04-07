package com.itg.filehandler.category;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.itg.filehandler.R;
import com.itg.filehandler.common.CommonMethod;
import com.itg.filehandler.common.RealPathUtil;
import com.itg.filehandler.model.FolderItem;
import com.itg.filehandler.presenter.ViewFolderPresenter;
import com.itg.filehandler.presenter.ViewFolderPresenterImp;
import com.itg.filehandler.view.VisitFolderView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VisitFolderActivity extends AppCompatActivity implements FolderPathAdapter.FolderPathClickListener, VisitFolderView, OnFIleSelectedListener, View.OnClickListener {

    private static final int RC_CHOOSE_FILE = 102;
    private static final String TAG = VisitFolderActivity.class.getSimpleName();
    @BindView(R.id.recycler_folder_path)
    RecyclerView recyclerFolderPath;
    @BindView(R.id.btn_add_file)
    CardView btnAddFile;
    @BindView(R.id.framecontainer)
    FrameLayout framecontainer;
    List<FolderModel> folderStructure = new ArrayList<>();
    FolderPathAdapter adapter;

    ViewFolderPresenter presenter;

    FragmentManager fm;
    private FragmentTransaction ft;
    private File file;
    private RecyclerView.LayoutManager mLinearManager;
    private boolean mIsList=true;
    private MenuItem mnuChangeLayout;
    private FileListFragment fragment;
    private boolean gridClicked;
    private File paremtfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_folder);
        ButterKnife.bind(this);

        presenter = new ViewFolderPresenterImp(this);

        initRecyclerview();

        getIntentFromFile();

        btnAddFile.setOnClickListener(this);
    }

    private void initRecyclerview() {
        mLinearManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
         adapter = new FolderPathAdapter(folderStructure);
        adapter.setListener(this);
        recyclerFolderPath.setLayoutManager(mLinearManager);
        recyclerFolderPath.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_visit_folder,menu);
        mnuChangeLayout=menu.findItem(R.id.mnu_layout);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnu_add_new:
                showAddNewDialog();
                break;
            case R.id.mnu_layout:
                gridClicked=!gridClicked;
                changeFragmentsRecyclerviewLayout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeFragmentsRecyclerviewLayout() {
        if(fragment!=null){
            if(gridClicked){
                mnuChangeLayout.setIcon(R.drawable.ic_art_track_black_24dp);
            }else {
                mnuChangeLayout.setIcon(R.drawable.ic_dashboard_black_24dp);
            }
            fragment.onLayoutChangeClicked(gridClicked);
        }
    }

    private void showAddNewDialog() {
    }

    private void getIntentFromFile() {
        if (getIntent().hasExtra(CommonMethod.PATH)) {
            paremtfile = new File(getIntent().getStringExtra(CommonMethod.PATH));
            addToAdapter(paremtfile);
            presenter.onInitFileList(paremtfile);
        }
    }

    private void addToAdapter(File file) {
        FolderModel model = new FolderModel();
        model.setLabel(file.getName());
        model.setPath(file);
        folderStructure.add(model);
        adapter.notifyDataSetChanged();
        recyclerFolderPath.scrollToPosition(folderStructure.size() - 1);
    }

    @Override
    public void onFolderpathClicked(int position, FolderModel model) {

    }

    boolean isFirstTime;

    @Override
    public void onFolderListAvail(ArrayList<FolderItem> names) {

        if (fm == null) {
            isFirstTime=true;
            fm = getSupportFragmentManager();
        }else {
            isFirstTime=false;
        }

        fragment=FileListFragment.newInstance(names);

        ft = fm.beginTransaction().replace(R.id.framecontainer,fragment , "FT");

        if(!isFirstTime)
            ft.addToBackStack(null);

        ft.commit();
    }

    @Override
    public void emptyFolder() {

    }

    @Override
    public void onFileItemSelected(FolderItem item, int position) {
        file = new File(item.getFilePath());
        if (item.getType() == CommonMethod.FOLDER) {
            addToAdapter(file);
            paremtfile=file;
            presenter.onInitFileList(file);
        }else
            openFile(file);

    }

    private void openFile(File file) {

    }

    @Override
    public void onBackPressed() {
        adapter.removeLast();
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_add_file) {
            openFileUsingSAF();
        }
    }

    private void openFileUsingSAF() {
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, RC_CHOOSE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CHOOSE_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.d(TAG,"Uri : "+uri.toString());
            String realPath;
            // SDK < API11
//            if (Build.VERSION.SDK_INT < 11)
//                realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());
//
//                // SDK >= 11 && SDK < 19
//            else if (Build.VERSION.SDK_INT < 19)
//                realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());
//
//                // SDK > 19 (Android 4.4)
//            else
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            realPath = ImageFilePath.getPath(getApplicationContext(), uri);
            Log.i("Image File Path", ""+realPath);

//            realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
//
//            Log.d(TAG, "real path: - " + realPath);
            showDialogForRealPath(realPath);
        }
    }

    private void showDialogForRealPath(String realPath) {
        if(realPath!=null){
            final File file=new File(realPath);
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("File "+file.getName());
            alert.setMessage("Would you like to copy it or move it?");
            alert.setCancelable(false);
            final File newFile=new File(folderStructure.get(folderStructure.size()-1).getPath().getAbsolutePath()+File.separator+file.getName());
            alert.setPositiveButton("Move", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(!newFile.exists()){
                      boolean b =  file.renameTo(newFile);
                        presenter.onInitFileList(paremtfile);
                        if(b){
                            Log.d(TAG,"Renamed ffile move file");
                        }else
                            Log.d(TAG,"Uhmmmmhhumm");
                    }
                }
            });


            alert.setNegativeButton("Copy", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.


                        try {
                            FileUtils.copyFile(file, newFile);
                            presenter.onInitFileList(paremtfile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                }
            });

            alert.show();
        }
    }

}
