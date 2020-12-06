package com.example.androidphotos80;

import android.content.Intent;
import android.os.Bundle;

import com.example.androidphotos80.model.Album;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedAlbum = albumList.get(albumIndex);
        //prepareTheList();
        //Todo make another recyclerViewAdapter class?!
        RecyclerViewAdapterPhotos adapter = new RecyclerViewAdapter(,  this, this);
        recyclerView.setAdapter(adapter);

        //TODO add a recyclerView for the photos, be able to first load/save the photos, then make it so we can click the cells, also need to make the buttons open up dialogs.

    }
}