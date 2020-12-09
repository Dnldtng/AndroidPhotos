package com.example.androidphotos80;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.androidphotos80.model.Album;
import com.example.androidphotos80.model.DataRW;
import com.example.androidphotos80.model.Photo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

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

    public void addButton(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 20);
    }

    public void deleteButton(View view){
        photoList.remove(selectedPhotoIndex);
        adapter.notifyItemRemoved(selectedPhotoIndex);
        DataRW.writeData(albumList, path);
        System.out.println("removed");
        adapter.notifyDataSetChanged();
    }

    public void displayButton(View view){
        Intent intent = new Intent (this, DisplayPhoto.class);
        intent.putExtra("photoList", (Serializable) photoList);
        intent.putExtra("selectedPhotoIndex", selectedPhotoIndex);
        startActivity(intent);
    }

    public void moveButton(View view){
        MoveDialog moveDialog = new MoveDialog();
        // Send selected photo and currentAlbum in bundle
        Bundle args = new Bundle();
        args.putSerializable("photo", selectedPhoto);
        args.putInt("albumIndex", albumIndex);
        moveDialog.setArguments(args);

        moveDialog.show(getSupportFragmentManager(), "Test");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Got our image, need to add it to photoList
        if(requestCode == 20 && resultCode == Activity.RESULT_OK){
            Uri photoUri = data.getData();
            System.out.println("This is the print: " + photoUri);
            Photo photoToAdd = new Photo(photoUri.toString());
            File photoFile = new File(photoUri.getPath());
            System.out.println("File PATH: " + photoFile.getAbsolutePath());
            photoToAdd.setCaption(photoFile.getName());
            photoList.add(photoToAdd);
            System.out.println(photoList.toString());
            System.out.println(selectedAlbum.getPhotosList().toString());
            // Save data
            DataRW.writeData(albumList, path);
            adapter.notifyDataSetChanged();
        }else{
            // Error dialog?
        }

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
        adapter.notifyDataSetChanged();
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
        albumList = (ArrayList<Album>) intent.getSerializableExtra("albums");

        /*
        try {
            albumList = DataRW.readData(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        */

        albumIndex = intent.getIntExtra("albumPosition", 0);
        recyclerView = findViewById(R.id.recyclerView2);

        //this was causing null pointer because no adapter attached
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedAlbum = albumList.get(albumIndex);

        System.out.println(selectedAlbum);

        photoList = selectedAlbum.getPhotosList();
        System.out.println(photoList.toString());


        //prob dont need
        //!!!omg this dumb ass line was fucking it up!
        //photoList = selectedAlbum.getPhotosList();


        adapter = new RecyclerViewAdapterPhotos(this, photoList);
        recyclerView.setAdapter(adapter);
        //had to replace with lambdas for some reason
        //adapter.setOnItemClickListener(new RecyclerViewAdapterPhotos.OnItemClickListener() {

        adapter.setOnItemClickListener(position -> {
            selectedPhotoIndex = position;
            selectedPhoto = photoList.get(position);
            System.out.println("selected" + selectedPhoto);
        });




    //TODO add a recyclerView for the photos, be able to first load/save the photos, then make it so we can click the cells, also need to make the buttons open up dialogs.

    }


}