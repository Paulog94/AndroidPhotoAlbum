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

    /**
     * gets Album name
     * @return Album name
     */
    public String getName(){return name;}

    /**
     * sets Album name
     * @param n new name
     */
    public void setName(String n){name = n;}

    /**
     * Returns Photo ArrayList
     *
     * @return ArrayList of Photos
     */
    public ArrayList<Photo> getPhotoList() {return photos;}

    /**
     * Override's toString Method
     * @return
     */
    @Override
    public String toString(){return getName()+" : "+photos.size()+" Photos";}
}
