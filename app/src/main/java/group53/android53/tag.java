package group53.android53;

import java.io.Serializable;

/**
 * Tag Object
 * Created by Paulo1 on 3/29/2016.
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

    public String getType(){return type;}
    public String getValue(){return value;}

    public String toString(){
        return getType()+": "+getValue();
    }

    public boolean equals(tag t){

        if(t.getType().equals(type) && t.getValue().equals(value)){
            return true;
        }
        return false;
    }
}
