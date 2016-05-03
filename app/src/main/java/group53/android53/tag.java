package group53.android53;

import java.io.Serializable;

/**
 * Tag Object
 * Created by Paulo on 3/29/2016.
 */
public class tag implements Serializable {
    //Tag type
    private String type;
    //Tag value
    private String value;

    /**
     * Constructs a tag object
     *
     * @param type tag type (ex: Location)
     * @param value tag value (ex: New York)
     */
    public tag(String type, String value){
            this.type = type.toLowerCase();
            this.value = value;
    }

    /**
     * Gets tag type
     * @return
     */
    public String getType(){return type;}

    /**
     * Gets tag name
     * @return
     */
    public String getValue(){return value;}

    /**
     * Displays tag type and tag name
     *
     * @return
     */
    public String toString(){
        return getType()+": "+getValue();
    }
}
