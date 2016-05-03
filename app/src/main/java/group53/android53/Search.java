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

    //Sets the Gallery
    public void setGallery(){
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, tmpPhotos);
        gridView.setAdapter(gridAdapter);
    }


    //Searches For Photos
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
        if(tmpPhotos.isEmpty()){
            Toast.makeText(Search.this, "No photos found", Toast.LENGTH_SHORT).show();
        }

        setGallery();
    }

    //Opens Searched Photo
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

    //Saves
    public void store() {
        try {
            FileOutputStream fos = this.getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(AlbumList);
            oos.close();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(this, "Could not Save", Toast.LENGTH_SHORT).show();
        }
    }

    //Loads
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

    //Stores Temp Photos
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
