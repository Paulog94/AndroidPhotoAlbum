package group53.android53;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ImageGallery extends AppCompatActivity {

    public static final int ADD_PHOTO_REQUEST_CODE = 4;

    private static ArrayList<Album> AlbumList = new ArrayList<Album>();
    private static String filename = "AlbumList.bin";
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        Bundle b = getIntent().getExtras();
        AlbumList = (ArrayList<Album>) b.getSerializable("AlbumList");
        index = b.getInt("index");

        Toast.makeText(getApplicationContext(),
                "Opens "+AlbumList.get(index).getName()+"'s Photo Gallery",
                Toast.LENGTH_LONG).show();

    }

    public void AddPhoto(View view){

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ADD_PHOTO_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_PHOTO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

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
    public void load(){
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
