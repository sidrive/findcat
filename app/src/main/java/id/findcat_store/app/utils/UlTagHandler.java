package id.findcat_store.app.utils;

import android.text.Editable;
import android.text.Html.TagHandler;

import org.xml.sax.XMLReader;

/**
 * Developer at Geekgarden.
 * Created by sim-x on 1/22/18.
 * Website geekgarden.id .
 * More info  geekgardendev@gmail.com.
 */

public class UlTagHandler implements TagHandler {
  boolean first= true;
  String parent=null;
  int index=1;
  @Override
  public void handleTag(boolean opening, String tag, Editable output,
      XMLReader xmlReader) {

    if(tag.equals("ul")) parent="ul";
    else if(tag.equals("ol")) parent="ol";
    if(tag.equals("li")){
      if(parent.equals("ul")){
        if(first){
          output.append("•");
          first= false;
        }else{
          first = true;
        }
      }
      else{
        if(first){
          output.append(""+index+". ");
          first= false;
          index++;
        }else{
          first = true;
        }
      }
    }
  }
}
