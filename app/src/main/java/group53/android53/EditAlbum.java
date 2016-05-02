package group53.android53;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class EditAlbum extends AppCompatActivity {

    private static ArrayList<Album> AlbumList = new ArrayList<Album>();
    private static String filename = "AlbumList.bin";
    private EditText edit;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);
        edit = (EditText)findViewById(R.id.editAlbumtxt);
        load();
        Bundle b = getIntent().getExtras();
        String ogName = b.getString("AlbumName");
        Log.v("Edit","original Name: "+ogName);
        edit.setText(ogName);
        index = b.getInt("index", 0);
        Log.v("Edit","original Index: "+index);
    }

    public void editName(View v){
        //Toast.makeText(getApplicationContext(),"Cannot edit duplicate or empty album name",Toast.LENGTH_LONG).show();

        if(!edit.getText().toString().equals("") && !RepeatAlbum(edit.getText().toString())){

            String newName = edit.getText().toString();
            Intent data = new Intent();
            data.putExtra("NewName",newName);
            data.putExtra("index",index);
            setResult(Activity.RESULT_OK,data);
            finish();
        }
        else
            Toast.makeText(getApplicationContext(),"Cannot edit duplicate or empty album name",Toast.LENGTH_LONG).show();

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
