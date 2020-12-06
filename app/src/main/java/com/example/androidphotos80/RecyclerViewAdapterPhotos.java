package com.example.androidphotos80;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidphotos80.model.Album;
import com.example.androidphotos80.model.Photo;

import java.util.List;

public class RecyclerViewAdapterPhotos extends RecyclerView.Adapter<RecyclerViewAdapterPhotos.ViewHolder> {

    private List<Album> albumList;
    private OnItemClickListener mListener;
    private Context context;
    private Album album;
    private List<Photo> photoList;


    public RecyclerViewAdapterPhotos(List<Photo> photoList, Context context) {
        this.albumList = albumList;
        //this.mListener = listener;
        this.context = context;
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Need to make the layout list item and attach below to view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_photolistitem, parent , false);
        RecyclerViewAdapterPhotos.ViewHolder holder  = new RecyclerViewAdapterPhotos.ViewHolder(view, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Load image into imageview
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbnail;

        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.photoThumbnail);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            // All of our buttons connecting to the stuff

        }

    }
}
