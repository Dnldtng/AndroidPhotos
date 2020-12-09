package com.example.androidphotos80;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidphotos80.model.Album;
import com.example.androidphotos80.model.Photo;

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
    }

    public void previousButton(View view){
        if(selectedPhotoIndex - 1 < 0){
            selectedPhotoIndex = selectedPhotoIndex;
        }else{
            selectedPhotoIndex = selectedPhotoIndex - 1;
        }
        imageView.setImageURI(Uri.parse(selectedPhoto.getPath()));
    }

    public void nextButton(View view){
        if(selectedPhotoIndex + 1 == currentAlbum.getPhotosList().size()){
            selectedPhotoIndex = selectedPhotoIndex;
        }else{
            selectedPhotoIndex = selectedPhotoIndex + 1;
        }
        imageView.setImageURI(Uri.parse(selectedPhoto.getPath()));
    }



}