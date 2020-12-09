package com.example.androidphotos80;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidphotos80.model.Album;
import com.example.androidphotos80.model.DataRW;
import com.example.androidphotos80.model.Photo;
import com.example.androidphotos80.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class DisplayPhoto extends AppCompatActivity {

    private TextView mTextView;
    private List<Photo> photoList = new ArrayList<>();
    private int selectedPhotoIndex;
    private ImageView imageView;
    private Button previousButton, nextButton;
    private Photo selectedPhoto;
    private Album currentAlbum;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterDisplay adapter;
    private ArrayList<Tag> tagList;
    private int selectedTagIndex;
    private Tag selectedTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_photos);
        mTextView = (TextView) findViewById(R.id.text);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);


        Intent intent = getIntent();
        photoList = (ArrayList<Photo>) intent.getSerializableExtra("photoList");
        currentAlbum = (Album) intent.getSerializableExtra("currentAlbum");
        selectedPhotoIndex = intent.getIntExtra("selectedPhotoIndex", 0);
        selectedPhoto = photoList.get(selectedPhotoIndex);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageURI(Uri.parse(selectedPhoto.getPath()));

        recyclerView = findViewById(R.id.recyclerView3);
        tagList = selectedPhoto.getTags();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapterDisplay(this, tagList);
        recyclerView.setAdapter(adapter);

        adapter.setOnTagItemClickListener(position -> {
            selectedTagIndex = position;
            selectedTag = tagList.get(position);
            System.out.println("selected" + selectedTag);
        });
    }
    public void addTagButton(View view){
        Tag test1 = new Tag(2, "donald");
        Tag test2 = new Tag(1, "edison");
        tagList.add(test1);
        tagList.add(test2);
        adapter.notifyItemInserted(0);
        adapter.notifyItemInserted(1);
        System.out.println("added");
       // selectedPhoto.addTag();
    }

    public void deleteTagButton(View view){
        tagList.remove(selectedTagIndex);
        adapter.notifyItemRemoved(selectedTagIndex);
        //DataRW.writeData(albumList, path);
        System.out.println("removed");
        adapter.notifyDataSetChanged();
    }

    public void previousButton(View view){
        if(selectedPhotoIndex - 1 < 0){
            selectedPhotoIndex = selectedPhotoIndex;
        }else{
            selectedPhotoIndex = selectedPhotoIndex - 1;
        }
        selectedPhoto = photoList.get(selectedPhotoIndex);
        imageView.setImageURI(Uri.parse(selectedPhoto.getPath()));
    }

    public void nextButton(View view){
        if(selectedPhotoIndex + 1 == currentAlbum.getPhotosList().size()){
            selectedPhotoIndex = selectedPhotoIndex;
        }else{
            selectedPhotoIndex = selectedPhotoIndex + 1;
        }
        selectedPhoto = photoList.get(selectedPhotoIndex);
        imageView.setImageURI(Uri.parse(selectedPhoto.getPath()));
    }



}