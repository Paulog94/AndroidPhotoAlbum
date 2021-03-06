package group53.android53;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by Paulo Garcia and Joshua Cross
 *
 * Adds a new Album to the app
 */
public class AddAlbum extends AppCompatActivity {

    private static ArrayList<Album> AlbumList = new ArrayList<Album>();
    private static String filename = "AlbumList.bin";
    private EditText edit;

    /**
     * Sets an Empty edit text
     * so the user can type a new
     * album name
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);
        edit = (EditText)findViewById(R.id.AddAlbumText);
        load();
    }

    /**
     * Button to start Save action and go
     * back to main Albumlist page
     *
     * @param v
     */
    public void SaveAlbum(View v){

        if(!edit.getText().toString().equals("")&&!RepeatAlbum(edit.getText().toString())){

            Album a = new Album(edit.getText().toString());
            Intent returnIntent = new Intent();
            returnIntent.putExtra("New Album",a);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();

        }
        else{
            Toast.makeText(getApplicationContext(),"Cannot add duplicate or empty album",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Used to check repeat Album names
     *
     * @param a
     * @return
     */
    public boolean RepeatAlbum(String a){
        for(Album al: AlbumList){
            if(al.getName().equals(a)){
                return true;
            }
        }
        return false;
    }

    /**
     * Loads Album list to compare
     */
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
