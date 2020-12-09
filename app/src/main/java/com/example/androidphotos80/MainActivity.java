package com.example.androidphotos80;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.androidphotos80.model.Album;
import com.example.androidphotos80.model.DataRW;
import com.example.androidphotos80.model.Photo;
import com.example.androidphotos80.model.Tag;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnNoteListener{


    // Dont think we need external storage actually
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private ArrayList<Album> albumList;
    private String[] names = {"album1", "album2"};
    private RecyclerView recyclerView;
    private String newAlbumText;
    private String path;
    RecyclerViewAdapter adapter;

    private void prepareTheList(){
        int count = 0;
        for(String name: names){
            Album album = new Album(name);
            albumList.add(album);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Photos");

        // This is where we will load in data
        // Temp file to check if data exists
        File tempFile = new File(getApplicationContext().getFilesDir(), "albums.dat");
        path = this.getApplicationContext().getFilesDir() + "/albums.dat";

        if(!tempFile.exists()){
            System.out.println("ERROR: File DNE");
            // Does not exist, make file
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            albumList = new ArrayList<Album>();
            DataRW.writeData(albumList, path);
            System.out.println("WROTE OUT");
        }else{
            try {
                albumList = (ArrayList<Album>) DataRW.readData(path);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("EXISTS!!, LOADED");
        }

        //TESTS
        System.out.println(albumList.toString());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(albumList,  this, this);
        recyclerView.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Alert Dialog Stuff
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");
        builder.setCancelable(true);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newAlbumText = input.getText().toString();
                // Check if album with name already exists
                for(Album a : albumList){
                    if(a.getName().equalsIgnoreCase(newAlbumText)){
                        Toast.makeText(getApplicationContext(), "Error: Album name already exists" , Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Album newAlbum = new Album(newAlbumText);
                albumList.add(newAlbum);
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

        final AlertDialog alert = builder.create();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the alert dialog
                input.setText("");
                alert.show();
            }
        });
    }

    public void searchButton(View view) {
        System.out.println("SEARCH BUTTON");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View searchLayout = getLayoutInflater().inflate(R.layout.search_dialog, null);
        builder.setView(searchLayout);
        builder.setTitle("Search by tag")
                .setCancelable(true)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Search logic -> go through album list, find photos with tags that match and get into photolist. Then display in new activity.
                        RadioGroup rg = searchLayout.findViewById(R.id.radioGroup);
                        EditText searchText = searchLayout.findViewById(R.id.searchText);
                        String inputText = searchText.getText().toString();
                        int radioID = rg.getCheckedRadioButtonId();
                        ArrayList<Photo> resultList = new ArrayList<Photo>();
                        if(radioID == R.id.locationButton){
                            // Location checked
                            System.out.println("Location");
                            // Check all location tags for search
                            for(Album a : albumList){
                                for(Photo p : a.getPhotosList()){
                                    if(p.validSearch("location", inputText)){
                                        resultList.add(p);
                                    }
                                }
                            }
                        }else if(radioID == R.id.personButton){
                            // Person checked
                            System.out.println("Person");
                            for(Album a : albumList){
                                for(Photo p : a.getPhotosList()){
                                    if(p.validSearch("person", inputText)){
                                        resultList.add(p);
                                    }
                                }
                            }
                        }else{
                            // TODO Add error toast dialog

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
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            albumList = DataRW.readData(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Need this or view doesnt update on adding a new album
        //adapter.notifyDataSetChanged();
    }


    @Override
    public void onNoteClick(int position) {
        //gives reference to item selected
        albumList.get(position);

        Intent intent = new Intent(this, OpenedAlbum.class);
        intent.putExtra("albums", (Serializable) albumList);
        intent.putExtra("albumPosition", position);
        startActivity(intent);

    }

}