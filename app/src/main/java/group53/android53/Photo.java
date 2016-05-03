package group53.android53;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;


import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * Used For Storing Photo urls
 * Caption
 * and Tags
 * Created by Paulo1 on 3/29/2016.
 */
public class Photo implements Serializable {
    private String caption;
    private ArrayList<tag> tags;
    private String image;


    /**
     * Adds Photo with Bitmap and caption
     * stores bitmap as a string
     *
     * @param caption
     */
    public Photo(Bitmap b, String caption){
        image = bitmapToString(b);
        this.caption = caption;
        tags = new ArrayList<tag>();
    }

    /**
     *Adds Bitmap to photo
     */
    public Photo(Bitmap b){
        image = bitmapToString(b);
        caption = "";
        tags = new ArrayList<tag>();
    }

    /**
     * Edits Photo Caption
     *
     * @param caption
     */
    public void editCaption(String caption){
        this.caption = caption;
    }

    /**
     * Returns the photo's caption
     *
     * @return
     */
    public String getCaption(){return caption;}

    /**
     * Converts stored bitmap string to an actual bitmap
     *
     * @return
     */
    public Bitmap getImage(){

        return stringToBitmap(image);}

    /**
     * Checks if String Tag value is
     * in its list of tags
     *
     * @param s tag value
     * @return
     */
    public boolean hasTag(String s){

        for(tag t: tags){
            if(t.getValue().contains(s)){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if tag type and value is
     * contained in list of tags
     *
     * @param type Tag type
     * @param s Tag name
     * @return
     */
    public boolean hasTag(String type,String s){

        for(tag t: tags){
            if(t.getValue().contains(s)&& t.getType().equals(type)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns Array list of tags
     * @return
     */
    public ArrayList<tag> getTags(){return tags;}

    /**
     * Function to Convert Bitmap into String to become Serializable
     *
     * @param in Bitmap
     * @return
     */
    private final static String bitmapToString(Bitmap in){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        return Base64.encodeToString(bytes.toByteArray(),Base64.DEFAULT);
    }

    /**
     * Converts String into an actual bitmap again
     *
     * @param in Bitmap String
     * @return
     */
    private final static Bitmap stringToBitmap(String in){
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
