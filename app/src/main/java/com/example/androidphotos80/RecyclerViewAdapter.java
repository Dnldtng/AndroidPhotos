package com.example.androidphotos80;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidphotos80.model.Album;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<Album> albumList;
    private Context context;
    private String renameAlbumText;

    public RecyclerViewAdapter(List<Album> albumList, Context context) {
        this.albumList = albumList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent , false);
        ViewHolder holder  = new ViewHolder(view);
        return holder;
    }


    //Deprecated **
    public void showRenameDialog(){
        // Alert Dialog Stuff
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Rename Album");
        builder.setCancelable(true);
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                renameAlbumText = input.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = builder.create();

        alert.show();

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.albumName.setText(album.getName());

        // Alert Dialog Stuff
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Rename Album");
        builder.setCancelable(true);
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                renameAlbumText = input.getText().toString();
                Album albumToRename = albumList.get(position);
                albumToRename.renameAlbum(renameAlbumText);
                notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = builder.create();

        // Delete button listener for each album list item. TODO Add saving stuff
        holder.deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Album albumToDelete = albumList.get(position);
                albumList.remove(albumToDelete);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });

        holder.renameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView albumName;
        Button deleteButton;
        Button renameButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.textView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            renameButton = itemView.findViewById(R.id.renameButton);
        }
    }
}
