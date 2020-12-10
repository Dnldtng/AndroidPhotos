package com.example.androidphotos80;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toolbar;

import com.example.androidphotos80.model.Photo;

import java.util.ArrayList;

public class searchResults extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Photo> resultPhotos;
    private RecyclerViewAdapterSearch adapter;
    private int searchFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Get intent result Photos for display
        Intent intent = getIntent();
        searchFlag = intent.getIntExtra("flag", 0);

        if(searchFlag == 1){
            // Location Search
        }else if(searchFlag == 2){
            // Person search
        }

        recyclerView = findViewById(R.id.recyclerViewSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewAdapterSearch(this, resultPhotos);
        recyclerView.setAdapter(adapter);


    }
}