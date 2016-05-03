package group53.android53;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class PhotoEditor extends AppCompatActivity {
    private static final int PHOTO_EDIT_RESULT_CODE = 7;
    private static ArrayList<Album> AlbumList = new ArrayList<Album>();
    private static String filename = "AlbumList.bin";
    private int Aindex, Pindex;
    private CheckBox cbLocation;
    private CheckBox cbPerson;
    private ImageView img;
    private ListView TagList;
    private EditText caption;
    private  EditText tagV;
    ArrayAdapter<tag> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_editor);
        load();
        Bundle b = getIntent().getExtras();

        //Loads information Needed
        Aindex = b.getInt("album index");
        Pindex = b.getInt("photo index");
        img = (ImageView)findViewById(R.id.img);
        TagList = (ListView)findViewById(R.id.tags);
        caption = (EditText) findViewById(R.id.txtEditCaption);
        tagV = (EditText)findViewById(R.id.txtTagValue);
        cbLocation =(CheckBox)findViewById(R.id.cbLocation);
        cbPerson = (CheckBox)findViewById(R.id.cbPerson);
        load();
        setContent();

    }

    //Sets content
    public void setContent(){
        img.setImageBitmap(AlbumList.get(Aindex).getPhotoList().get(Pindex).getImage());
        caption.setText(AlbumList.get(Aindex).getPhotoList().get(Pindex).getCaption());

        //Sets Tag List
        adapter = new ArrayAdapter<tag>(this,R.layout.support_simple_spinner_dropdown_item,
                AlbumList.get(Aindex).getPhotoList().get(Pindex).getTags());
        TagList.setAdapter(adapter);
    }

    //Saves Changes
    public void SaveChanges(View v){
        AlbumList.get(Aindex).getPhotoList().get(Pindex).editCaption(caption.getText().toString());
        store();
        setResult(Activity.RESULT_OK);
        finish();
    }

    //sets other checkbox to not selected
    public void checkLocation(View v){
        if(cbPerson.isChecked()){
            cbPerson.setChecked(false);
        }
    }
    //Sets other checkbox to not selected
    public void checkPerson(View v){
        if(cbLocation.isChecked()){
            cbLocation.setChecked(false);
        }
    }

    //Adds Tag Button Action
    public void AddTag(View v) {
        if (!(cbLocation.isChecked() || cbPerson.isChecked())) {
            Toast.makeText(PhotoEditor.this, "Select a Tag type to add a tag", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cbLocation.isChecked() &&
                !tagV.getText().toString().equals("") &&
                !RepeatTag("location",tagV.getText().toString()))
        {
            AlbumList.get(Aindex).getPhotoList().get(Pindex).getTags().
                    add(new tag("Location", tagV.getText().toString()));
            adapter.notifyDataSetChanged();
            store();
            return;
        }
        else if (cbPerson.isChecked() &&
                !tagV.getText().toString().equals("") &&
                !RepeatTag("person",tagV.getText().toString()))
        {
            AlbumList.get(Aindex).getPhotoList().get(Pindex).getTags().
                    add(new tag("Person", tagV.getText().toString()));
            adapter.notifyDataSetChanged();
            store();
            return;
        }
        else{
            Toast.makeText(PhotoEditor.this, "Cannot have an empty or repeated tag", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean RepeatTag(String type,String a){
        for(tag t: AlbumList.get(Aindex).getPhotoList().get(Pindex).getTags()){
            if(t.getValue().equals(a) && t.getType().equals(type)){
                return true;
            }
        }
        return false;
    }

    //Saves Albums
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
