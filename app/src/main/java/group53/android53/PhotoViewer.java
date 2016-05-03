package group53.android53;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class PhotoViewer extends AppCompatActivity {
    private static final int PHOTO_EDIT_RESULT_CODE = 7;
    private static ArrayList<Album> AlbumList = new ArrayList<Album>();
    private static String filename = "AlbumList.bin";
    private int Aindex, Pindex;

    private ImageView img;
    private ListView TagList;
    private TextView caption;
    ArrayAdapter<tag> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        Bundle b = getIntent().getExtras();

        //Loads information Needed
        Aindex = b.getInt("album index");
        Pindex = b.getInt("photo index");

        //If no Photo Was selected to Open
        if(Pindex == -1){
            Pindex = 0;
        }
        img = (ImageView)findViewById(R.id.imageView);
        TagList = (ListView)findViewById(R.id.TagList);
        caption = (TextView)findViewById(R.id.TVcaption);
        load();
        setContent();
    }

    //Goes to next Photo
    public void Prev(View v){
        if(Pindex == 0){
            Pindex = AlbumList.get(Aindex).getPhotoList().size() - 1;
            Toast.makeText(PhotoViewer.this, "No more previous photos", Toast.LENGTH_SHORT).show();
        }else{
            --Pindex;
        }
        setContent();
    }

    //Goes to prev Photo
    public void Next(View v){
        if(Pindex == AlbumList.get(Aindex).getPhotoList().size() - 1){
            Pindex = 0;
            Toast.makeText(PhotoViewer.this, "No more next photo, back at start", Toast.LENGTH_SHORT).show();
        }else{
            ++Pindex;
        }
        setContent();
    }

    //Edits Photo
    public void EditPhoto(View v){
        Intent intent = new Intent(getApplicationContext(),PhotoEditor.class);
        intent.putExtra("album index",Aindex);
        intent.putExtra("photo index",Pindex);
        startActivityForResult(intent,PHOTO_EDIT_RESULT_CODE);
    }

    //Deletes Tag
    public void deleteTag(View v){
        if(TagList.getCheckedItemPosition()!=-1){
            AlbumList.get(Aindex).getPhotoList().get(Pindex).getTags().
                    remove(TagList.getCheckedItemPosition());
            adapter.notifyDataSetChanged();
            TagList.clearChoices();
            store();
        }
        else
            Toast.makeText(this, "Select Tag for Deletion", Toast.LENGTH_SHORT).show();
    }

    public void setContent(){
        //There is a converter inside Photo to convert String Bitmap into real bitmap
        //Sets image View and Caption
        img.setImageBitmap(AlbumList.get(Aindex).getPhotoList().get(Pindex).getImage());
        caption.setText(AlbumList.get(Aindex).getPhotoList().get(Pindex).getCaption());

        //Sets Tag List
        adapter = new ArrayAdapter<tag>(this,R.layout.support_simple_spinner_dropdown_item,
                AlbumList.get(Aindex).getPhotoList().get(Pindex).getTags());
        TagList.setAdapter(adapter);

    }

    //Catches Result onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_EDIT_RESULT_CODE) {
            if (resultCode == RESULT_OK){
                load();
                setContent();
            }
        }
    }

    @Override
    public void onRestart(){
        super.onRestart();
        load();
        setContent();
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
