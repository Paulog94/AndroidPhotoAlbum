package group53.android53;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Used to create an Album class
 * Contains a String name and a photo list
 *
 * Created by Paulo and Joshua on 3/29/2016.
 */
public class Album implements Serializable{
    private String name;
    private ArrayList<Photo> photos;

    /**
     * Initializes a new Album with an empty
     * list of photos
     *
     * @param name Album name
     */
    public Album (String name){
        this.name = name;
        photos = new ArrayList<Photo>();
    }

    public String getName(){return name;}
    public void setName(String n){name = n;}

    /**
     * Used to add photos to this Album
     * @param p photo to be added
     */
    public void addPhoto(Photo p){
        if(containsPhoto(p))
            photos.add(p);
    }

    /**
     * Used to delete photos from this Album
     * @param p photo to be delete
     */
    public void deletePhoto(Photo p){
        if(containsPhoto(p))
            photos.remove(p);
    }

    /**
     * Used to see if a photo is in this Album
     * @param p photo to check
     */
    public boolean containsPhoto(Photo p){
        return photos.contains(p);
    }
    public boolean containsPhoto(String p){
        return false;
    }
    
    public ArrayList<Photo> getPhotoList() {return photos;}

    @Override
    public String toString(){return getName()+" : "+photos.size()+" Photos";}
}
