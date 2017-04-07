package com.itg.filehandler.category;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itg.filehandler.R;
import com.itg.filehandler.model.FolderItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {

    private ArrayList<FolderItem> fileList;
    private boolean mIsList;
    private OnFileItemClickListener listener;


    interface OnFileItemClickListener{
        void onFileItemSelected(FolderItem item, int position);


    }


    FileListAdapter(ArrayList<FolderItem> fileList, boolean mIsList) {
        this.fileList = fileList;
        this.mIsList = mIsList;
    }

    public void setListener(OnFileItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(!mIsList ? R.layout.rv_file_list_item: R.layout.rv_file_folder_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtFileName.setText(fileList.get(position).getFileName());
        holder.imgPreview.setImageResource(holder.getResourceByExt(fileList.get(position).getFileName()));

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_preview)
        ImageView imgPreview;
        @BindView(R.id.txt_file_name)
        TextView txtFileName;
//        @BindView(R.id.txt_item_or_size)
//        TextView txtItemOrSize;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        private int getResourceByExt(String fileName) {
            String filenameArray[] = fileName.split("\\.");
            String extension = filenameArray[filenameArray.length-1];
            if(extension.contains("jpg") || extension.contains("jpeg"))
                return R.drawable.ic_jpg_flat;
            else if(extension.contains("txt")){
                return R.drawable.ic_txt_flat;
            }else if(extension.contains("doc"))
                return R.drawable.ic_doc_flat;
            else if(extension.contains("docx"))
                return R.drawable.ic_docx_flat;
            else if(extension.contains("xls"))
                return R.drawable.ic_xls_flat;
            else if(extension.contains("xlsx")){
                return R.drawable.ic_xlsx_flat;
            }else if(extension.contains("png"))
                return R.drawable.ic_png_flat;
            else if(extension.contains("ppt")){
                return R.drawable.ic_ppt_flat;
            }else if(extension.contains("pptx")){
                return R.drawable.ic_pptx_flat;
            }else if(extension.contains("mp3")){
                return R.drawable.ic_mp3_flat;
            }else if(extension.contains("mp4"))
                return R.drawable.ic_mp4_flat;
//            else if(extension.contains("pdf"))
//                return R.drawable
            else{
                return R.drawable.ic_no_preview_flat;
            }
        }

        @Override
        public void onClick(View view) {
            listener.onFileItemSelected(fileList.get(getAdapterPosition()), getAdapterPosition());
        }
    }
}
