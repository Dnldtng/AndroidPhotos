package com.example.androidphotos80;

import android.content.Context;
import android.net.Uri;
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
    private Context context;
    private List<Photo> photoList;

    public RecyclerViewAdapterPhotos( Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Need to make the layout list item and attach below to view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_photolistitem, parent , false);
        RecyclerViewAdapterPhotos.ViewHolder holder  = new RecyclerViewAdapterPhotos.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Load image into imageview
        Photo photo = photoList.get(position);
        // URI to bitmap
        holder.thumbnail.setImageURI(Uri.parse(photo.getPath()));
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView thumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.photoThumbnail);

        }

    }
}
