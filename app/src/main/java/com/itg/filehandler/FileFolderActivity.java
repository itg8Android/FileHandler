package com.itg.filehandler;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.itg.filehandler.category.VisitFolderActivity;
import com.itg.filehandler.common.CommonMethod;
import com.itg.filehandler.presenter.FileFolderPresenter;
import com.itg.filehandler.presenter.FileFolderPresenterImp;
import com.itg.filehandler.view.FileFolderView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class FileFolderActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, FileFolderView, FolderStructureAdapter.FolderItemClickListener {

    private static final int RC_STORAGE_PERMISSION = 101;
    private static final String TAG = FileFolderActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listFolder)
    RecyclerView listFolder;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private boolean storagePermission;

    private FileFolderPresenter presenter;
    private FolderStructureAdapter adapter;
    private ArrayList<File> directorylist;
    private AlertDialog alertDialog;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_folder);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getPermissionForStorage();
        presenter = new FileFolderPresenterImp(this);

        listFolder.setLayoutManager(new GridLayoutManager(this, 2));
        presenter.onInitItems();
//        adapter=new FolderStructureAdapter();
//        listFolder.setAdapter(adapter);
    }

    @AfterPermissionGranted(RC_STORAGE_PERMISSION)
    private void getPermissionForStorage() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            setStoragePermission(true);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage), RC_STORAGE_PERMISSION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void onPrepareFileInfo() {

    }

    public boolean isStoragePermission() {
        return storagePermission;
    }

    public void setStoragePermission(boolean storagePermission) {
        this.storagePermission = storagePermission;
        onPrepareFileInfo();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == RC_STORAGE_PERMISSION) {
            setStoragePermission(true);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == RC_STORAGE_PERMISSION) {
            setStoragePermission(false);
        }
    }

    @Override
    public void onFolderStructureAvail(ArrayList<String> list, ArrayList<File> directoryList) {
        adapter = new FolderStructureAdapter(list);
        adapter.setListener(this);
        this.directorylist = directoryList;
        listFolder.setAdapter(adapter);
    }

    @Override
    public void onNoFileAvail() {
        Log.d(TAG, "no file attach");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnu_add_new) {
            showDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {


        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Create category");
        alert.setMessage("Please enter the name of a category you want to create.");
        alert.setCancelable(false);
// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        input.setHint("Category name");

        alert.setPositiveButton("Ok", null);


        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                alertDialog.dismiss();
            }
        });
        alertDialog = alert.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //Dismiss once everything is OK.
                        String value = input.getText().toString();
                        // Do something with value!
                        if (TextUtils.isEmpty(value)) {
                            input.setError("Please enter name.");
                        } else {

                            //This is where you would put your make directory code
                            File photos = new File(new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name)), value);
                            if (!photos.exists()) {
                                //noinspection ResultOfMethodCallIgnored
                                photos.mkdir();
                                presenter.onInitItems();
                                alertDialog.dismiss();
                            }
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    @Override
    public void onFolderItemClicked(String fName, int position) {
        File selectedFile = directorylist.get(position);
        if (selectedFile.exists() && selectedFile.getName().equalsIgnoreCase(fName)) {
//            Log.d(TAG, "selected file is : " + selectedFile.getName());
            intent=new Intent(this, VisitFolderActivity.class);
            intent.putExtra(CommonMethod.PATH,directorylist.get(position).getAbsolutePath());
            startActivity(intent);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

//        try {
//            position = adapter;
//        } catch (Exception e) {
//            Log.d(TAG, e.getLocalizedMessage(), e);
//            return super.onContextItemSelected(item);
//        }
        int position=-1;
        switch (item.getGroupId()) {
            case 1:
                Log.d(TAG,"Rename clicked "+item.getOrder());
                position=item.getOrder();
                // do your stuff
                renameInDialog(position);
                break;
            case 2:
                Log.d(TAG,"Delete clicked" + item.getOrder());
                // do your stuff
                position=item.getOrder();
                deleteInDialog(position);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void renameInDialog(int item) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final File oldFile=directorylist.get(item);
        String name=adapter.getItem(item)!=null?adapter.getItem(item):"";
        alert.setTitle("Rename category");
        alert.setMessage("What would you like to rename.");
        alert.setCancelable(false);
// Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        input.setHint("Category name");
        input.setText(name);

        alert.setPositiveButton("Ok", null);


        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                alertDialog.dismiss();
            }
        });
        alertDialog = alert.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //Dismiss once everything is OK.
                        String value = input.getText().toString();
                        // Do something with value!
                        if (TextUtils.isEmpty(value)) {
                            input.setError("Please enter name.");
                        } else {

                            //This is where you would put your make directory code
                            File photos = new File(new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name)), value);
                            if (!photos.exists() && oldFile.exists()) {
                                //noinspection ResultOfMethodCallIgnored
                                oldFile.renameTo(photos);
                                presenter.onInitItems();
                                alertDialog.dismiss();
                            }else {
                                input.setError("Name already exist. Please choose another one.");
                            }
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }


    private void deleteInDialog(final int item) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final File oldFile=directorylist.get(item);
        String name=adapter.getItem(item)!=null?adapter.getItem(item):"";
        alert.setTitle("Delete "+name);
        alert.setMessage("Deleting this category subject to remove all file inside it. Would you like to continue?");
        alert.setCancelable(false);
// Set an EditText view to get user input

        alert.setPositiveButton("Ok", null);


        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                alertDialog.dismiss();
            }
        });
        alertDialog = alert.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        presenter.onDeleteItem(oldFile, item);
                    }
                });
            }
        });
        alertDialog.show();
    }


    @Override
    public void onFileDeleted(int position) {
        if(alertDialog!=null && alertDialog.isShowing())
            alertDialog.dismiss();

        adapter.removeItem(position);
        directorylist.remove(position);
    }

    @Override
    public void onFileDeleteFail(int position) {
        if(alertDialog!=null && alertDialog.isShowing())
            alertDialog.dismiss();

        Snackbar.make(listFolder,"Invalid Operation",Snackbar.LENGTH_LONG).show();
    }
}

