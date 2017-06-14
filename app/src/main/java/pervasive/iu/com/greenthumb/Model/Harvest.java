package pervasive.iu.com.greenthumb.Model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by madrinathapa on 4/22/17.
 */

public class Harvest {
    private String Caption;
    private ArrayList<String> Tags;
    private HashMap<String, String> Colors;
    private ArrayList<String> Categories;

    public ArrayList<String> getTags() {
        return Tags;
    }

    public void setTags(ArrayList<String> tags) {
        Tags = tags;
    }

   public HashMap<String, String> getColors() {
        return Colors;
    }

    public void setColors(HashMap<String, String> colors) {
       Colors = colors;
    }

    public ArrayList<String> getCategories() {
       return Categories;
   }

   public void setCategories(ArrayList<String> categories) {
       Categories = categories;
   }

    public void setCaption(String caption){
        this.Caption = caption;
    }

    public String getCaption(){
        return Caption;
    }
public Harvest(){

}
    public Harvest(String caption, ArrayList<String> tags, HashMap<String, String> colors,
                   ArrayList<String> categories){
        this.Caption = caption;
        this.Tags = tags;
        this.Colors = colors;
        this.Categories = categories;
    }
}
