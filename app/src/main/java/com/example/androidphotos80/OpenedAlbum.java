package com.example.androidphotos80;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.androidphotos80.model.Album;
import com.example.androidphotos80.model.DataRW;
import com.example.androidphotos80.model.Photo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.OpenableColumns;
import android.view.View;
import android.view.contentcapture.DataShareWriteAdapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class OpenedAlbum extends AppCompatActivity {
    private ArrayList<Album> albumList;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterPhotos adapter;
    private Album selectedAlbum;
    private int albumIndex;
    private ArrayList<Photo> photoList;
    private Photo selectedPhoto;
    private int selectedPhotoIndex;
    private String path;
    private Spinner albumSpinner;


    public void addButton(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    public void deleteButton(View view){
        if(albumList.get(albumIndex).getPhotosList().isEmpty()){
            Toast.makeText(getApplicationContext(), "Error: No Photos to delete", Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedPhotoIndex>albumList.get(albumIndex).getPhotosList().size()-1 || selectedPhotoIndex<0){
            Toast.makeText(getApplicationContext(), "Error: Nothing selected to delete", Toast.LENGTH_SHORT).show();
            return;
        }
        photoList.remove(selectedPhotoIndex);
        adapter.notifyItemRemoved(selectedPhotoIndex);
        DataRW.writeData(albumList, path);
        System.out.println("removed");
        adapter.notifyDataSetChanged();
    }

    public void displayButton(View view){
        Intent displayIntent = new Intent (this, DisplayPhoto.class);
        try {
            albumList = DataRW.readData(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        displayIntent.putExtra("albumList", albumList);
        displayIntent.putExtra("photoList", albumList.get(albumIndex).getPhotosList());
        displayIntent.putExtra("selectedPhotoIndex", selectedPhotoIndex);
        displayIntent.putExtra("albumIndex", albumIndex);
        //displayIntent.putExtra("currentAlbum", selectedAlbum);
        startActivity(displayIntent);
    }

    public void moveButton(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View moveLayout = getLayoutInflater().inflate(R.layout.move_dialog, null);

        // selectedPhoto can be null if it is the user does not select anything and tries to move
        // the first photo without clicking it. Null check here and default to first in list
        if(selectedPhoto == null){
            selectedPhoto = albumList.get(albumIndex).getPhotosList().get(0);
        }

        Album currentAlbum = albumList.get(albumIndex);
        ArrayList<Album> destinationList = new ArrayList<Album>(albumList);
        // Get rid of current album for destination list
        destinationList.remove(albumIndex);
        albumSpinner = (Spinner) view.findViewById(R.id.albumSpinner);

        ArrayAdapter<Album> adapter = new ArrayAdapter<Album>(this, android.R.layout.simple_spinner_item, destinationList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        albumSpinner.setAdapter(adapter);

        builder.setView(moveLayout)
                .setTitle("Move Photo")
                .setCancelable(true)
                .setPositiveButton("Move Photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Album destinationAlbum = (Album) albumSpinner.getSelectedItem();
                        // Add photo
                        destinationAlbum.addPhoto(selectedPhoto);
                        //DataRW.writeData(albumList, path);

                        // Remove photo
                        currentAlbum.getPhotosList().remove(selectedPhotoIndex);

                        DataRW.writeData(albumList, path);

                        // Calls moveUpdate method in openedAlbum to notify
                        System.out.println("Current Album: " + currentAlbum + ", albumIndex: " + albumIndex +  ", selectedPhotoIndex: " + selectedPhotoIndex +
                                ", selectedPhoto: " + selectedPhoto + ", destinationAlbum: " + destinationAlbum );

                        //((OpenedAlbum)getActivity()).moveUpdate(currentAlbumIndex, selectedPhotoIndex, selectedPhoto, destinationAlbum);
                        System.out.println(selectedPhotoIndex + " " + currentAlbum);

                        //((OpenedAlbum)getActivity()).moveUpdate(selectedPhotoIndex, currentAlbum);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //cancel empty
                    }
                });
        AlertDialog moveAlert = builder.create();
        moveAlert.show();
    }

    public void moveUpdate(int photoIndex, Album albumRemove){
        //destAlbum.addPhoto(addPhoto);

        // Try removing photo here instead??

        /*
        Album albumRemove = albumList.get(albumIndex);
        albumRemove.getPhotosList().remove(photoIndex);
        DataRW.writeData(albumList, path);
        */

        //adapter.notifyItemRemoved(photoIndex);
        adapter.notifyItemRangeChanged(photoIndex, photoList.size());
        //adapter.notifyDataSetChanged();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Got our image, need to add it to photoList
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            Uri photoUri = data.getData();
            System.out.println("This is the print: " + photoUri);
            Photo photoToAdd = new Photo(photoUri.toString());
            File photoFile = new File(photoUri.getPath());
            System.out.println("File PATH: " + photoFile.getAbsolutePath());
            photoToAdd.setCaption(photoFile.getName());
            photoToAdd.setCaption(getFileName(photoUri));

            // Check if photo already exists in any album
            for(Album a : albumList){
                for(Photo p : a.getPhotosList()){
                    if(photoToAdd.equals(p)){
                        // Duplicate found, check if in current album or other
                        if(selectedAlbum.equals(a)){
                            // Photo is already in current album, error dialog
                            Toast.makeText(this, "Error: Photo already in album" , Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            // Photo is in other album, get the reference to that photo object instead to get tags
                            photoToAdd = p;
                        }
                    }
                }
            }

            photoList.add(photoToAdd);
            System.out.println(photoList.toString());
            System.out.println(selectedAlbum.getPhotosList().toString());
            // Save data
            System.out.println("ACTIVITY RESULT WRITE");
            DataRW.writeData(albumList, path);
            adapter.notifyDataSetChanged();
        }else{
            // Error dialog?
        }



    }


    // Extract file name from uri
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /*
    @Override
    public void onRestart(){
        super.onRestart();
        try {
            albumList = DataRW.readData(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Need this or view doesnt update on adding a new album
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            albumList = DataRW.readData(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        // Need this or view doesnt update on adding a new album
        //adapter.notifyDataSetChanged();
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        path = this.getApplicationContext().getFilesDir() + "/albums.dat";

        Intent intent = getIntent();

        //albumList = (ArrayList<Album>) intent.getSerializableExtra("albums");



        try {
            albumList = DataRW.readData(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



        albumIndex = intent.getIntExtra("albumPosition", 0);
        recyclerView = findViewById(R.id.recyclerView2);

        //this was causing null pointer because no adapter attached
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedAlbum = albumList.get(albumIndex);
        //getSupportActionBar().setTitle(selectedAlbum.getName());
        System.out.println("INSIDE ALBUM :" + selectedAlbum);
        photoList = selectedAlbum.getPhotosList();
        System.out.println(photoList.toString());

        adapter = new RecyclerViewAdapterPhotos(this, photoList);
        recyclerView.setAdapter(adapter);
        //had to replace with lambdas for some reason
        //adapter.setOnItemClickListener(new RecyclerViewAdapterPhotos.OnItemClickListener() {

        adapter.setOnItemClickListener(position -> {
            selectedPhotoIndex = position;
            selectedPhoto = photoList.get(position);
            System.out.println("selected" + selectedPhoto);
        });

    }


}