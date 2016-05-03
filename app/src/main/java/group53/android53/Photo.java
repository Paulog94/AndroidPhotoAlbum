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

    public Bitmap getImage(){

        return stringToBitmap(image);}

    public boolean hasTag(String s){

        for(tag t: tags){
            if(t.getValue().contains(s)){
                return true;
            }
        }
        return false;
    }
    public boolean hasTag(String type,String s){

        for(tag t: tags){
            if(t.getValue().contains(s)&& t.getType().equals(type)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<tag> getTags(){return tags;}

    private final static String bitmapToString(Bitmap in){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        return Base64.encodeToString(bytes.toByteArray(),Base64.DEFAULT);
    }
    private final static Bitmap stringToBitmap(String in){
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

}
