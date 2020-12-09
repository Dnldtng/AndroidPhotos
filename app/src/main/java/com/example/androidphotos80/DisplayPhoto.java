package com.example.androidphotos80;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidphotos80.model.Album;
import com.example.androidphotos80.model.DataRW;
import com.example.androidphotos80.model.Photo;
import com.example.androidphotos80.model.Tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DisplayPhoto extends AppCompatActivity {

    private ArrayList<Album> albumList;
    private TextView mTextView;
    private List<Photo> photoList;
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
    private String typeSelected = "person";
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_photos);
        mTextView = (TextView) findViewById(R.id.text);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);

        path = this.getApplicationContext().getFilesDir() + "/albums.dat";

        Intent intent = getIntent();
        //albumList = (ArrayList<Album>) intent.getSerializableExtra("albumList");

        try {
            albumList = DataRW.readData(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        photoList = (ArrayList<Photo>) intent.getSerializableExtra("photoList");
        //currentAlbum = (Album) intent.getSerializableExtra("currentAlbum");
        selectedPhotoIndex = intent.getIntExtra("selectedPhotoIndex", 0);
        selectedPhoto = photoList.get(selectedPhotoIndex);
        System.out.println("Selected Photo: " + selectedPhoto);
        System.out.println(albumList);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageURI(Uri.parse(selectedPhoto.getPath()));


        recyclerView = findViewById(R.id.recyclerView3);
        tagList = selectedPhoto.getTags();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapterDisplay(this, tagList, albumList);
        recyclerView.setAdapter(adapter);

        //TEST
        System.out.println("TagList: " + selectedPhoto.getTags().toString());
        System.out.println("AlbumList: " + albumList + " photoList: " + photoList + " currentAlbum: " + currentAlbum + " PhotoIndex: " + selectedPhotoIndex + " SelectedPhoto: " + selectedPhoto + " selectedTagList " + selectedPhoto.getTags().toString());

        adapter.setOnTagItemClickListener(position -> {
            selectedTagIndex = position;
            selectedTag = tagList.get(position);
            System.out.println("selected: " + selectedTag);
        });
    }

    public void addTagButton(View view){
        System.out.println("ADD BUTTON");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View searchLayout = getLayoutInflater().inflate(R.layout.search_dialog, null);
        builder.setView(searchLayout);
        builder.setTitle("Search by tag")
                .setCancelable(true)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Search logic -> go through album list, find photos with tags that match and get into photolist. Then display in new activity.
                        RadioGroup rg = searchLayout.findViewById(R.id.radioGroup);
                        EditText searchText = searchLayout.findViewById(R.id.searchText);
                        String inputText = searchText.getText().toString();
                        int radioID = rg.getCheckedRadioButtonId();
                        if(radioID == R.id.locationButton){
                            // Location checked
                            System.out.println("Location");
                            Tag tempTag = new Tag("location", inputText);
                            // Check if album with name already exists
                            for(Tag t : tagList) {
                                if (t.equals(tempTag)) {
                                    Toast.makeText(getApplicationContext(), "Error: Tag already exists", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            //tagList.add(tempTag);
                            //selectedPhoto.addTag(tempTag);
                            selectedPhoto.getTags().add(tempTag);
                            System.out.println("Added tag " + tempTag + " To Photo: " + selectedPhoto);
                            // Save data
                            DataRW.writeData(albumList, path);

                        }else if(radioID == R.id.personButton){
                            // Person checked
                            System.out.println("Person");
                            Tag tempTag = new Tag("person", inputText);
                            // Check if album with name already exists
                            for(Tag t : tagList) {
                                if (t.equals(tempTag)) {
                                    Toast.makeText(getApplicationContext(), "Error: Tag already exists", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            //tagList.add(tempTag);
                            //selectedPhoto.addTag(tempTag);
                            selectedPhoto.getTags().add(tempTag);
                            System.out.println(selectedPhoto.getTags().toString());
                            // Save data
                            DataRW.writeData(albumList, path);
                        }else{
                                // Nothing?
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            // Empty cancel
                    }
                });


        AlertDialog searchAlert = builder.create();
        searchAlert.show();
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

        //tagList.clear();
        ArrayList<Tag> newTags = selectedPhoto.getTags();
        //tagList = newTags;
        adapter.notifyDataSetChanged();

    }

    public void nextButton(View view){
        if(selectedPhotoIndex + 1 == currentAlbum.getPhotosList().size()){
            selectedPhotoIndex = selectedPhotoIndex;
        }else{
            selectedPhotoIndex = selectedPhotoIndex + 1;
        }
        selectedPhoto = photoList.get(selectedPhotoIndex);
        imageView.setImageURI(Uri.parse(selectedPhoto.getPath()));

       // tagList.clear();
        ArrayList<Tag> newTags = selectedPhoto.getTags();
        //tagList = newTags;
        adapter.notifyDataSetChanged();
    }



}