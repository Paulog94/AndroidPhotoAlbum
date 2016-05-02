package group53.android53;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.jar.Manifest;

public class ImageGallery extends AppCompatActivity {

    public static final int ADD_PHOTO_REQUEST_CODE = 4;
    public static final int REQUEST_CODE_PERMISSION = 5;

    private static ArrayList<Album> AlbumList = new ArrayList<Album>();
    private static String filename = "AlbumList.bin";
    private int index;
    private GridView gridView;
    private GridViewAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        Bundle b = getIntent().getExtras();
        AlbumList = (ArrayList<Album>) b.getSerializable("AlbumList");
        index = b.getInt("index");

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, AlbumList.get(index).getPhotoList());
        gridView.setAdapter(gridAdapter);


        Toast.makeText(getApplicationContext(),
                "Opens " + AlbumList.get(index).getName() + "'s Photo Gallery",
                Toast.LENGTH_LONG).show();
    }


    public void DeletePhoto(View v){
        if(gridView.getCheckedItemPosition()!=-1){
            AlbumList.get(index).getPhotoList().remove(gridView.getCheckedItemPosition());
            gridAdapter.notifyDataSetChanged();
            store();
        }
        else
            Toast.makeText(ImageGallery.this, "Select Photo for Deletion", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NewApi")
    public void AddPhoto(View view) {

        //Checking For Permission
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            //Intent to start image gallery view for android System
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, ADD_PHOTO_REQUEST_CODE);
        } else {
            Toast.makeText(ImageGallery.this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
            //For Permission to accesss Phone's external Storage
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            }
        }
    }

    //For Permission to Access Image Gallery of the Android Phone
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, ADD_PHOTO_REQUEST_CODE);
            }
            else{
                Toast.makeText(ImageGallery.this, "Permission Denied by User", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PHOTO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                //Here is where We get images URi and place it onto the Gallery

                Uri imageUri = data.getData();
                try {
                    Bitmap newImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    AlbumList.get(index).getPhotoList().add(new Photo(newImage));
                    gridAdapter.notifyDataSetChanged();
                    store();
                    //Toast.makeText(getApplicationContext(), "Add This Photo" + data.getData().toString(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Couldn't get Image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //Saves function
    public void store() {
        try {
            FileOutputStream fos = this.getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(AlbumList);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Loads Albums
    public void load() {
        FileInputStream fis = null;
        try {
            fis = this.getApplicationContext().openFileInput(filename);

            ObjectInputStream is = null;
            try {
                is = new ObjectInputStream(fis);

                try {
                    AlbumList = (ArrayList<Album>) is.readObject();
                    is.close();
                    fis.close();
                } catch (ClassNotFoundException e) {
                    AlbumList = new ArrayList<Album>();
                }
            } catch (IOException e) {
                AlbumList = new ArrayList<Album>();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
