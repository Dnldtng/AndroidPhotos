package com.example.androidphotos80;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.androidphotos80.model.Album;
import com.example.androidphotos80.model.Photo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleBiFunction;

public class OpenedAlbum extends AppCompatActivity {
    private List<Album> albumList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Album selectedAlbum;
    private int albumIndex;
    private List<Photo> photoList = new ArrayList<>();

    public void addButton(View view){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, 20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Got our image, need to add it to photoList
        if(requestCode == 20 && resultCode == Activity.RESULT_OK){
            Uri photoUri = data.getData();
            System.out.println(photoUri);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened_album);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        albumList = (ArrayList<Album>) intent.getSerializableExtra("albums");
        albumIndex = intent.getIntExtra("albumPosition", 0);
        recyclerView = findViewById(R.id.recyclerView);

        selectedAlbum = albumList.get(albumIndex);

        RecyclerViewAdapterPhotos adapter = new RecyclerViewAdapterPhotos(this, photoList);
        recyclerView.setAdapter(adapter);

        //TODO add a recyclerView for the photos, be able to first load/save the photos, then make it so we can click the cells, also need to make the buttons open up dialogs.

    }
}