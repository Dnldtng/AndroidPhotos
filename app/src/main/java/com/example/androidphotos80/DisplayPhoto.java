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
        albumList = (ArrayList<Album>) intent.getSerializableExtra("albumList");
        photoList = (ArrayList<Photo>) intent.getSerializableExtra("photoList");
        currentAlbum = (Album) intent.getSerializableExtra("currentAlbum");
        selectedPhotoIndex = intent.getIntExtra("selectedPhotoIndex", 0);
        selectedPhoto = photoList.get(selectedPhotoIndex);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageURI(Uri.parse(selectedPhoto.getPath()));

        recyclerView = findViewById(R.id.recyclerView3);
        tagList = selectedPhoto.getTags();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapterDisplay(this, tagList,albumList);
        recyclerView.setAdapter(adapter);

        adapter.setOnTagItemClickListener(position -> {
            selectedTagIndex = position;
            selectedTag = tagList.get(position);
            System.out.println("selected: " + selectedTag);
        });
    }
    public void addTagButton(View view){

        final String[] tagTypes = {"location", "person"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Tag type");
        builder.setCancelable(true);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setSingleChoiceItems(tagTypes,0, (dialogInterface, i) -> {
            typeSelected = tagTypes[i];
        });
                
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Tag newTag = new Tag(typeSelected, input.getText().toString());
                // Check if album with name already exists
                for(Tag t : tagList){

                    if(t.getValue().equalsIgnoreCase(newTag.getValue()) && t.getName().equalsIgnoreCase(newTag.getName())){
                        Toast.makeText(getApplicationContext(), "Error: Tag already exists" , Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                tagList.add(newTag);
                // Save data
                DataRW.writeData(albumList, path);

                try {
                    adapter.updateList(albumList);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                // adapter.notifyItemInserted(albumList.size() - 1);
                //adapter.notifyDataSetChanged();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog addDialog = builder.create();
        addDialog.show();


/*
        Tag test1 = new Tag(2, "donald");
        Tag test2 = new Tag(1, "edison");
        tagList.add(test1);
        tagList.add(test2);
        adapter.notifyItemInserted(0);
        adapter.notifyItemInserted(1);
        System.out.println("added");
       // selectedPhoto.addTag();

 */
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