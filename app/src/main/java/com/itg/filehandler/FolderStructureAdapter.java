package com.itg.filehandler;


import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

class FolderStructureAdapter extends RecyclerView.Adapter<FolderStructureAdapter.ViewHolder> {


    private ArrayList<String> list;
    private FolderItemClickListener listener;

    public String getItem(int position) {
        return list.get(position);
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRangeChanged(position,list.size()-1);
    }


    public interface FolderItemClickListener{
        void onFolderItemClicked(String fName,int position);
    }

    public FolderStructureAdapter(ArrayList<String> list) {

        this.list = list;
    }

    public void setListener(FolderItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtFolderName.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }





    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        @BindView(R.id.txt_folder_name)
        TextView txtFolderName;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onFolderItemClicked(list.get(getAdapterPosition()),getAdapterPosition());

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.setHeaderTitle(list.get(getAdapterPosition()));
            menu.add(1, view.getId(), getAdapterPosition(), "Rename");//groupId, itemId, order, title
            menu.add(2, view.getId(), getAdapterPosition(), "Delete");
        }



    }



}
