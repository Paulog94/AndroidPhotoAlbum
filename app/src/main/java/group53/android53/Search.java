package group53.android53;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Paulo Garcia and Joshua Cross
 *
 * Allows user to Search entire photo album
 * for photos based on tag type, or just the
 * tag value alone
 */
public class Search extends AppCompatActivity {
    private static ArrayList<Album> AlbumList = new ArrayList<Album>();
    private static String filename = "AlbumList.bin";
    private static String tmpfilename = "SearchPhotos.bin";
    private static ArrayList<Photo> tmpPhotos;
    private GridView gridView;
    private static GridViewAdapter gridAdapter;
    private CheckBox cbLocation;
    private CheckBox cbPerson;
    private EditText txtsearch;

    /**
     * sets variables
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        load();
        cbLocation = (CheckBox)findViewById(R.id.checkBoxLocation);
        cbPerson = (CheckBox)findViewById(R.id.checkBoxPerson);
        gridView = (GridView)findViewById(R.id.S_Photo_grid);
        txtsearch = (EditText)findViewById(R.id.txtSearch);
        tmpPhotos = new ArrayList<>();
    }

    /**
     * Sets the Gallery
     */
    public void setGallery(){
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, tmpPhotos);
        gridView.setAdapter(gridAdapter);
    }


    /**
     * Searches photo on click
     * takes the search query from user and goes through every photo tag list,
     * if the query is found and the tag type is matched, or not required,
     * photo is added to the temp photo list
     * Gallery is set if photos are found
     *
     * @param v
     */
    public void SearchPhotos(View v){
        String query = txtsearch.getText().toString();
        tmpPhotos.clear();
        if(!(cbLocation.isChecked()||cbPerson.isChecked()) ||
                (cbLocation.isChecked()&&cbPerson.isChecked())){
            //Checks for tag Value for both Location and Height
            for(Album a: AlbumList){
                for(Photo p: a.getPhotoList()){

                    if(p.hasTag(query)){
                        tmpPhotos.add(p);
                    }
                }
            }
        }
        else if(cbLocation.isChecked()){
            for(Album a: AlbumList){
                //Checks For Location Tag
                for(Photo p: a.getPhotoList()){

                    if(p.hasTag("location",query)){
                        tmpPhotos.add(p);
                    }
                }
            }
        }
        else if (cbPerson.isChecked()){
            //Checks For Person Tag
            for(Album a: AlbumList){
                for(Photo p: a.getPhotoList()){

                    if(p.hasTag("person",query)){
                        tmpPhotos.add(p);
                    }
                }
            }
        }
        //Displays no photos found if theres an empty list
        if(tmpPhotos.isEmpty()){
            Toast.makeText(Search.this, "No photos found", Toast.LENGTH_SHORT).show();
        }

        setGallery();
    }

    /**
     * Allows User to view the selected searched Photo on button click
     *
     * Goes to View_Searched_Photos.java
     *
     * @param v
     */
    public void openPhoto(View v){
        int index = 0;
        if(!tmpPhotos.isEmpty()) {
            if(gridView.getCheckedItemPosition()!=-1){
                index = gridView.getCheckedItemPosition();
            }
            Intent intent = new Intent(this.getApplicationContext(),View_Searched_Photos.class);
            intent.putExtra("index",index);
            storetmp();
            startActivity(intent);
        }
    }


    /**
     * Loads Photo Albums
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
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Stores temp Photos so
     * View Searched Photos can access them
     */
    public void storetmp() {
        try {
            FileOutputStream fos = this.getApplicationContext().openFileOutput(tmpfilename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tmpPhotos);
            oos.close();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(this, "Could not Save", Toast.LENGTH_SHORT).show();
        }
    }
}
