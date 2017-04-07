package com.itg.filehandler.category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itg.filehandler.R;

import java.util.Deque;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



class FolderPathAdapter extends RecyclerView.Adapter<FolderPathAdapter.ViewHolder> {



    private List<FolderModel> models;
    FolderPathClickListener listener;

    interface FolderPathClickListener{
        void onFolderpathClicked(int position, FolderModel model);
    }

    FolderPathAdapter(List<FolderModel> models) {
        this.models = models;
    }

    void setListener(FolderPathClickListener listener) {
        this.listener = listener;
    }

    void removeLast(){
        if(models.size()>1){
            models.remove(models.size()-1);
        }
        notifyDataSetChanged();
    }

    public void removeFromPosition(int position){
        for(int i=position; i<models.size(); i++){
            models.remove(i);
        }
        notifyItemRangeChanged(position,models.size());
    }

    public void add(FolderModel model){
        models.add(model);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_folder_path, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtFolderName.setText(models.get(position).getLabel());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txt_folderName)
        TextView txtFolderName;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onFolderpathClicked(getAdapterPosition(),models.get(getAdapterPosition()));
        }
    }
}
