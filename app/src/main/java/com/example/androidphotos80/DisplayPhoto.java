package com.example.androidphotos80;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
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
    private ImageView imageView;
    private Button previousButton, nextButton;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterDisplay adapter;

    private ArrayList<Photo> photoList;

    private int selectedPhotoIndex;
    private int selectedTagIndex;
    private int albumIndex;
    private Album currentAlbum;

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
        imageView = (ImageView)findViewById(R.id.imageView);

        System.out.println("DISPLAY PHOTO ON CREATE CALLED");

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

        selectedPhotoIndex = intent.getIntExtra("selectedPhotoIndex", 0);
        albumIndex = intent.getIntExtra("albumIndex", 0);
        currentAlbum = albumList.get(albumIndex);
        //System.out.println("Selected Photo: " + selectedPhoto + "Tags: " + selectedPhoto.getTags());

        imageView.setImageURI(Uri.parse(albumList.get(albumIndex).getPhotosList().get(selectedPhotoIndex).getPath()));

        recyclerView = findViewById(R.id.recyclerView3);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapterDisplay(this, albumList.get(albumIndex).getPhotosList().get(selectedPhotoIndex).getTags(), albumList);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        //TEST
        //System.out.println("TagList: " + selectedPhoto.getTags().toString());
        //System.out.println("AlbumList: " + albumList + " photoList: " + photoList + " currentAlbum: " + currentAlbum + " PhotoIndex: " + selectedPhotoIndex + " SelectedPhoto: " + selectedPhoto + " selectedTagList " + selectedPhoto.getTags().toString());

        adapter.setOnTagItemClickListener(position -> {
            selectedTagIndex = position;
            selectedTag = albumList.get(albumIndex).getPhotosList().get(selectedPhotoIndex).getTags().get(selectedTagIndex);
            //selectedTag = selectedPhoto.getTags().get(position);
            System.out.println("selected: " + selectedTag);
        });
    }

    public void addTagButton(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View searchLayout = getLayoutInflater().inflate(R.layout.search_dialog, null);
        builder.setView(searchLayout);
        builder.setTitle("Add Tag")
                .setCancelable(true)
                .setPositiveButton("Add Tag", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RadioGroup rg = searchLayout.findViewById(R.id.radioGroup);
                        EditText addText = searchLayout.findViewById(R.id.searchText);
                        String inputText = addText.getText().toString();
                        int radioID = rg.getCheckedRadioButtonId();
                        if(radioID == R.id.locationButton){
                            albumList.get(albumIndex).getPhotosList().get(selectedPhotoIndex).addTag((new Tag("location", inputText)));
                            //selectedPhoto.addTag(new Tag("location", inputText));
                            //adapter.notifyItemInserted(selectedPhoto.getTags().size() - 1);

                            adapter.notifyDataSetChanged();
                            DataRW.writeData(albumList, path);


                            /*
                            System.out.println(albumList.get(0).getPhotosList().get(0));
                            System.out.println(albumList.get(0).getPhotosList().get(0).getTags().toString());

                            System.out.println(selectedPhoto.getTags().toString());

                             */
                        }else if(radioID == R.id.personButton){
                            albumList.get(albumIndex).getPhotosList().get(selectedPhotoIndex).addTag((new Tag("person", inputText)));
                            //adapter.notifyItemInserted(selectedPhoto.getTags().size() - 1);
                            adapter.notifyDataSetChanged();
                            DataRW.writeData(albumList, path);
                        }else{
                            // No selection
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel Empty
                    }
                });


        AlertDialog addAlert = builder.create();
        addAlert.show();


    }

    public void deleteTagButton(View view){
        // TODO handling if nothing selected, Toast error. Also handling if empty list, other toast error

        //selectedPhoto.getTags().remove(selectedTagIndex);
        albumList.get(albumIndex).getPhotosList().get(selectedPhotoIndex).getTags().remove(selectedTagIndex);
        adapter.notifyItemRemoved(selectedTagIndex);
        DataRW.writeData(albumList, path);
        System.out.println("removed");
        adapter.notifyDataSetChanged();
    }

    public void previousButton(View view){
        if(selectedPhotoIndex - 1 == -1){
            selectedPhotoIndex = selectedPhotoIndex;
        }else{
            selectedPhotoIndex = selectedPhotoIndex - 1;
        }
        imageView.setImageURI(Uri.parse(albumList.get(albumIndex).getPhotosList().get(selectedPhotoIndex).getPath()));

        //ArrayList<Tag> newTagList = selectedPhoto.getTags();
        adapter = new RecyclerViewAdapterDisplay(this, albumList.get(albumIndex).getPhotosList().get(selectedPhotoIndex).getTags(), albumList);
        adapter.setOnTagItemClickListener(position -> {
            selectedTagIndex = position;
            selectedTag = albumList.get(albumIndex).getPhotosList().get(selectedPhotoIndex).getTags().get(selectedTagIndex);
            //selectedTag = selectedPhoto.getTags().get(position);
            System.out.println("selected: " + selectedTag);
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void nextButton(View view){
        if(selectedPhotoIndex + 1 == currentAlbum.getPhotosList().size()){
            selectedPhotoIndex = selectedPhotoIndex;
        }else{
            selectedPhotoIndex = selectedPhotoIndex + 1;
        }
        imageView.setImageURI(Uri.parse(albumList.get(albumIndex).getPhotosList().get(selectedPhotoIndex).getPath()));

        //ArrayList<Tag> newTagList = selectedPhoto.getTags();
        adapter = new RecyclerViewAdapterDisplay(this, albumList.get(albumIndex).getPhotosList().get(selectedPhotoIndex).getTags(), albumList);
        adapter.setOnTagItemClickListener(position -> {
            selectedTagIndex = position;
            selectedTag = albumList.get(albumIndex).getPhotosList().get(selectedPhotoIndex).getTags().get(selectedTagIndex);
            //selectedTag = selectedPhoto.getTags().get(position);
            System.out.println("selected: " + selectedTag);
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


}