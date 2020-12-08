package com.example.androidphotos80;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidphotos80.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class DisplayPhoto extends AppCompatActivity {

    private TextView mTextView;
    private List<Photo> photoList = new ArrayList<>();
    private int selectedPhotoIndex;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_photos);
        mTextView = (TextView) findViewById(R.id.text);
        Intent intent = getIntent();
        photoList = (ArrayList<Photo>) intent.getSerializableExtra("photoList");
        selectedPhotoIndex = intent.getIntExtra("selectedPhotoIndex", 0);

        Photo selectedPhoto = photoList.get(selectedPhotoIndex);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageURI(Uri.parse(selectedPhoto.getPath()));
    }
}