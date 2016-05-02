package group53.android53;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class AddAlbum extends AppCompatActivity {

    private static ArrayList<Album> AlbumList = new ArrayList<Album>();
    private static String filename = "AlbumList.bin";
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);
        edit = (EditText)findViewById(R.id.AddAlbumText);
        load();
    }


    public void SaveAlbum(View v){
        Log.d("Save Album","At the Start");
        if(!edit.getText().toString().equals("")&&!RepeatAlbum(edit.getText().toString())){

            Album a = new Album(edit.getText().toString());
            Intent returnIntent = new Intent();
            returnIntent.putExtra("New Album",a);
            setResult(Activity.RESULT_OK,returnIntent);
            //Log.d("Save Album","Inside True");
            finish();

        }
        else{
            Log.d("Save Album","Didnt Work");
            Toast.makeText(getApplicationContext(),"Cannot add duplicate or empty album",Toast.LENGTH_LONG).show();
            //setResult(Activity.RESULT_CANCELED);
            //finish();
        }
    }


    public boolean RepeatAlbum(String a){
        for(Album al: AlbumList){
            if(al.getName().equals(a)){
                return true;
            }
        }
        return false;
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
                    Toast.makeText(this, "Cannot Create Albums", Toast.LENGTH_SHORT).show();
                    AlbumList = new ArrayList<Album>();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Cannot Read InputStream", Toast.LENGTH_SHORT).show();
                AlbumList = new ArrayList<Album>();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "No FIle Loaded", Toast.LENGTH_SHORT).show();
        }
    }

}
