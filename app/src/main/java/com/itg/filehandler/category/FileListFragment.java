package com.itg.filehandler.category;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itg.filehandler.R;
import com.itg.filehandler.model.FolderItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FileListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileListFragment extends Fragment implements FileListAdapter.OnFileItemClickListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.recycler_list)
    RecyclerView recyclerList;
    Unbinder unbinder;
//    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private ArrayList<FolderItem> fileList;
    private Context context;
    private OnFIleSelectedListener listener;
    private RecyclerView.LayoutManager mLinearManager;
    private boolean mIsList;
//    private String mParam2;


    public FileListFragment() {
        // Required empty public constructor
    }


    public static FileListFragment newInstance(ArrayList<FolderItem> list) {
        FileListFragment fragment = new FileListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fileList = getArguments().getParcelableArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRecyclerview(mIsList);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFIleSelectedListener)
            listener= (OnFIleSelectedListener) context;
        else {
            throw new IllegalStateException("please implement OnFIleSelectedListener in VisitFolderActivity");
        }
        this.context=context;
    }

    private void initRecyclerview(boolean mIsList) {

        setDynamicLayoutManager(mIsList);
        recyclerList.setLayoutManager(mLinearManager);
        FileListAdapter adapter = new FileListAdapter(fileList, mIsList);
        adapter.setListener(this);
        recyclerList.setAdapter(adapter);
    }


    private void setDynamicLayoutManager(boolean mIsList) {
        if(!mIsList){
            mLinearManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }else {
            mLinearManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onFileItemSelected(FolderItem item, int position) {
        listener.onFileItemSelected(item,position);
    }

    public void onLayoutChangeClicked(boolean isList) {
        initRecyclerview(isList);
    }
}
