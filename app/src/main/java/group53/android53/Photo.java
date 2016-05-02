package group53.android53;

import javafx.scene.image.Image;


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
    private String url;
    private String caption;
    private ArrayList<tag> tags;
    Calendar date;

    /**
     * Adds Photo with Url and caption
     * @param url
     * @param caption
     */
    public Photo(String url, String caption){
        this.url = url;
        this.caption = caption;
        tags = new ArrayList<tag>();
        date = Calendar.getInstance();
        date.add(Calendar.DATE,1);
        date.set(Calendar.MILLISECOND,0);
    }

    /**
     * Adds UrL with just an empty Caption
     *
     * @param url
     */
    public Photo(String url){
        this.url= url;
        caption = "";
        tags = new ArrayList<tag>();
        date = Calendar.getInstance();
        date.add(Calendar.DATE, 1);
        date.set(Calendar.MILLISECOND,0);
    }

    public void addTag(tag t){
        tags.add(t);
        date.add(Calendar.DATE, 1);
        date.set(Calendar.MILLISECOND,0);
    }
    public void deleteTag(tag t){
        tags.remove(t);
        date.add(Calendar.DATE, 1);
        date.set(Calendar.MILLISECOND,0);
    }
    public void editCaption(String caption){
        this.caption = caption;
        date.add(Calendar.DATE, 1);
        date.set(Calendar.MILLISECOND,0);
    }
    public String getCaption(){return caption;}
    public Image getImage(){return new Image(url);}
    public String getURL(){return url;}

    public Calendar getDate(){return date;}

    public boolean hasTag(tag s){

        for(tag t: tags){
            if(t.equals(s)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<tag> getTags(){return tags;}


}
