package group53.android53;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class View_Searched_Photos extends AppCompatActivity {

    private static String filename = "SearchPhotos.bin";
    private static ArrayList<Photo> tmpPhotos;
    private static int index;
    ImageView img;
    TextView caption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__searched__photos);
        Bundle b = getIntent().getExtras();
        index = b.getInt("index");
        load();
        img = (ImageView) findViewById(R.id.sImg);
        caption = (TextView) findViewById(R.id.captionTV);
        setContent();
    }

    //Sets textView and ImageView
    public void setContent() {
        //There is a converter inside Photo to convert String Bitmap into real bitmap
        //Sets image View and Caption
        String tags = " ";
        img.setImageBitmap(tmpPhotos.get(index).getImage());
        for(tag t : tmpPhotos.get(index).getTags())
            tags+="#"+t.getValue()+" ";
        caption.setText(tmpPhotos.get(index).getCaption()+"\n"+tags);
    }

    //Goes to next Photo
    public void prev(View v){
        if(index == 0){
            index = tmpPhotos.size() - 1;
            Toast.makeText(this, "No more previous photos", Toast.LENGTH_SHORT).show();
        }else{
            --index;
        }
        setContent();
    }

    //Goes to next Photo
    public void next(View v){
        if(index == tmpPhotos.size() - 1){
            index = 0;
            Toast.makeText(this, "No more photos, back at start", Toast.LENGTH_SHORT).show();
        }else{
            ++index;
        }
        setContent();
    }

    //Loads Temp images
    public void load(){
        FileInputStream fis = null;
        try {
            fis = this.getApplicationContext().openFileInput(filename);

            ObjectInputStream is = null;
            try {
                is = new ObjectInputStream(fis);

                try {
                    tmpPhotos = (ArrayList<Photo>) is.readObject();
                    is.close();
                    fis.close();
                } catch (ClassNotFoundException e) {
                    tmpPhotos = new ArrayList<Photo>();
                }
            } catch (IOException e) {
                tmpPhotos = new ArrayList<Photo>();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }
}
