package com.example.androidphotos80;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.example.androidphotos80.model.Album;
import com.example.androidphotos80.model.DataRW;
import com.example.androidphotos80.model.Photo;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import javax.xml.xpath.XPath;

public class MoveDialog extends AppCompatDialogFragment {

    public Button confirmButton, cancelButton;
    public Spinner albumSpinner;
    public ArrayList<Album> albumList;
    public Album currentAlbum, destinationAlbum;
    private String path;
    private int currentAlbumIndex;
    private Photo selectedPhoto;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.move_dialog, null);

        currentAlbumIndex = getArguments().getInt("albumIndex");
        selectedPhoto = (Photo) getArguments().getSerializable("photo");
        path = this.getContext().getFilesDir() + "/albums.dat";

        try {
            albumList = DataRW.readData(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ArrayList<Album> destinationList = new ArrayList<Album>(albumList);
        // Get rid of current album for destination list
        destinationList.remove(currentAlbumIndex);

        albumSpinner = (Spinner) view.findViewById(R.id.albumSpinner);

        ArrayAdapter<Album> adapter = new ArrayAdapter<Album>(getContext(), android.R.layout.simple_spinner_item, destinationList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        albumSpinner.setAdapter(adapter);

        builder.setView(view)
                .setTitle("Select Destination")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Move", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        destinationAlbum = (Album) albumSpinner.getSelectedItem();
                        // Add photo
                        destinationAlbum.addPhoto(selectedPhoto);

                        // Remove photo
                        currentAlbum = albumList.get(currentAlbumIndex);
                        currentAlbum.getPhotosList().remove(selectedPhoto);
                        DataRW.writeData(albumList, path);

                    }
                });

        return builder.create();
    }

}
