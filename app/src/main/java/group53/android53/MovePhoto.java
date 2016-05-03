package group53.android53;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
 * Created by Paulo Garcia and Joshua Cross
 *
 * Allows User to move selected Photo
 * to a new Album
 */
public class MovePhoto extends AppCompatActivity {

    private static ArrayList<Album> AlbumList = new ArrayList<Album>();
    private static String filename = "AlbumList.bin";
    private ListView listView;
    ArrayAdapter<Album> adapter;
    private int oAlbum, Pindex,nAlbum;

    /**
     * Sets List View of Albums for photo to be moved to
     * Obtains old Album index, and photo index
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_photo);
        Bundle b = getIntent().getExtras();

        //Loads information Needed
        oAlbum = b.getInt("album index");
        Pindex = b.getInt("photo index");
        load();

        listView = (ListView) findViewById(R.id.AList);

        adapter = new ArrayAdapter<Album>(this,R.layout.support_simple_spinner_dropdown_item,AlbumList);
        listView.setAdapter(adapter);
    }

    /**
     * Moves Photo on button click
     * Array list selected album adds the
     * photo, then the old album removes the photo
     * Saves the result
     * App goes back to image gallery
     * @param v
     */
    public void Move(View v){
        if(listView.getCheckedItemPosition()>-1){
            nAlbum = listView.getCheckedItemPosition();

            //Moves photo to new Album
            AlbumList.get(nAlbum).getPhotoList().
                    add(AlbumList.get(oAlbum).getPhotoList().get(Pindex));

            //Removes Photo from old Album
            AlbumList.get(oAlbum).getPhotoList().remove(Pindex);
            store();
            //Exit Activity
            setResult(RESULT_OK);
            finish();
        }
        else
            Toast.makeText(MovePhoto.this, "Select an Album to move the Photo", Toast.LENGTH_SHORT).show();
    }

    /**
     * Save Albums
     */
    public void store() {
        try {
            FileOutputStream fos = this.getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(AlbumList);
            oos.close();
            fos.close();
        } catch (IOException e) {
            Toast.makeText(this, "Save Failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Loads Albums for the List View
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
