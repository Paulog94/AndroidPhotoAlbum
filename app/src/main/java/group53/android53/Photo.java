package group53.android53;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Used For Storing Photo urls
 * Caption
 * and Tags
 * Created by Paulo1 on 3/29/2016.
 */
public class Photo implements Serializable {
    private String caption;
    private ArrayList<tag> tags;
    private Bitmap image;


    /**
     * Adds Photo with Bitmap and caption
     * @param caption
     */
    public Photo(Bitmap b, String caption){
        this.image = b;
        this.caption = caption;
        tags = new ArrayList<tag>();
    }

    /**
     *Adds Bitmap to photo
     */
    public Photo(Bitmap b){
        image = b;
        caption = "";
        tags = new ArrayList<tag>();
    }

    public void addTag(tag t){
        tags.add(t);
    }
    public void deleteTag(tag t){
        tags.remove(t);
    }
    public void editCaption(String caption){
        this.caption = caption;
    }
    public String getCaption(){return caption;}

    public Bitmap getImage(){return image;}

    public boolean hasTag(String s){

        for(tag t: tags){
            if(t.getValue().equals(s)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<tag> getTags(){return tags;}


}
