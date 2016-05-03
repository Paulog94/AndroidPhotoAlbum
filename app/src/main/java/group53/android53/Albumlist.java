package group53.android53;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created By Paulo Garcia and Joshua Cross
 *
 * Albumlist page activity:
 * implements a list of albums
 */
public class Albumlist extends AppCompatActivity {

    private static final int  ADD_REQUEST_CODE = 1;
    private static final int  EDIT_REQUEST_CODE = 2;

    private static ArrayList<Album> AlbumList = new ArrayList<Album>();
    private static String filename = "AlbumList.bin";

    private ListView listView;
    ArrayAdapter<Album> adapter;

    /**
     * Displays saved Albums on creation
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albumlist);

        listView = (ListView) findViewById(R.id.listView);
        load();

        adapter = new ArrayAdapter<Album>(this,R.layout.support_simple_spinner_dropdown_item,AlbumList);
        listView.setAdapter(adapter);
    }

    /**
     * Launches Edit activity when the Edit button is clicked
     *
     * Goes to EditAlbum.java
     *
     * @param view
     */
    public void Edit(View view){

        //Used to determine if an item is clicked or not
        //Goes to EditAlbum.java
        if(listView.getCheckedItemPosition() >= 0){

            Intent intent = new Intent(getApplicationContext(), EditAlbum.class);
            String ogName = AlbumList.get(listView.getCheckedItemPosition()).getName();
            intent.putExtra("AlbumName", ogName);
            int i = listView.getCheckedItemPosition();
            intent.putExtra("index",i);
            startActivityForResult(intent, EDIT_REQUEST_CODE);
        }
        else
            Toast.makeText(getApplicationContext(),"No Album Selected",Toast.LENGTH_LONG).show();


    }

    /**
     * Adds a new Album to the app
     *
     * Goes to AddAlbum.java
     *
     * @param v
     */
    public void Add(View v){
        Intent intent = new Intent(getApplicationContext(),AddAlbum.class);
        startActivityForResult(intent,ADD_REQUEST_CODE);
    }

    /**
     * Deletes Selected ALbum
     *
     * @param view
     */
    public void Delete(View view){

        //Clears selection after deletion
        if(!AlbumList.isEmpty() && listView.getCheckedItemPosition()!=-1) {
            AlbumList.remove(listView.getCheckedItemPosition());
            adapter.notifyDataSetChanged();
            listView.clearChoices();
            store();

        }
        else{
            Toast.makeText(getApplicationContext(),"No Album Selected",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Opens Album and displays its images
     * Goes to ImageGallery.java
     *
     * @param view
     */
    public void Open(View view){
        //Checks if there is a selected item
        if(listView.getCheckedItemPosition() >= 0){

            Intent intent = new Intent(getApplicationContext(), ImageGallery.class);
            int i = listView.getCheckedItemPosition();
            intent.putExtra("index",i);
            startActivity(intent);
        }
        else
            Toast.makeText(getApplicationContext(),"No Album Selected",Toast.LENGTH_LONG).show();
    }

    /**
     * Launches Search Activity to search for tags
     *
     * Goes to Search.java
     *
     * @param view
     */
    public void Search(View view){
        Intent intent = new Intent(getApplicationContext(),Search.class);
        startActivity(intent);
    }

    /**
     * Returns Data from activity launches
     * Identifies activity from request codes
     *
     * Used for AddAlbum.java and EditAlbum.java
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                Album a = (Album) data.getSerializableExtra("New Album");
                AlbumList.add(a);
                adapter.notifyDataSetChanged();
                store();
                Log.d("Add Result","Ok");
            }
        }
        else if(requestCode == EDIT_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                String n = data.getStringExtra("NewName");
                int i = data.getIntExtra("index",0);
                AlbumList.get(i).setName(n);
                adapter.notifyDataSetChanged();
                store();
            }
        }
    }

    /**
     * Enables Persistance once back button is clicked
     * Albumlist will be automatically updated on back
     *
     */
    @Override
    public void onRestart(){
        super.onRestart();
        listView = (ListView) findViewById(R.id.listView);
        load();
        adapter = new ArrayAdapter<Album>(this,R.layout.support_simple_spinner_dropdown_item,AlbumList);
        listView.setAdapter(adapter);
    }

    /**
     * Allows app to save data
     */
    public void store() {
        try {
            FileOutputStream fos = this.getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(AlbumList);
            oos.close();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(Albumlist.this, "Could not Save", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Allows app to load data
     */
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
            Toast.makeText(Albumlist.this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

}
