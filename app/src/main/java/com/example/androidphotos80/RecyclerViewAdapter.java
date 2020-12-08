package com.example.androidphotos80;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
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
import com.example.androidphotos80.model.DataRW;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<Album> albumList;
    private OnNoteListener mOnNoteListener;
    private Context context;
    private String renameAlbumText;
    private String path;

    public RecyclerViewAdapter(ArrayList<Album> albumList, OnNoteListener onNoteListener, Context context) {
        this.albumList = albumList;
        this.mOnNoteListener = onNoteListener;
        this.context = context;
        path = context.getFilesDir() + "/albums.dat";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent , false);
        ViewHolder holder  = new ViewHolder(view, mOnNoteListener);
        return holder;
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
                // Save data
                DataRW.writeData(albumList, path);
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
                // Save data
                DataRW.writeData(albumList, path);
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



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView albumName;
        Button deleteButton;
        Button renameButton;
        OnNoteListener onNoteListener;


        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            albumName = itemView.findViewById(R.id.textView);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            renameButton = itemView.findViewById(R.id.renameButton);

            itemView.setOnClickListener(this);
            this.onNoteListener = onNoteListener;
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}

