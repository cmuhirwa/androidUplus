package info.androidhive.uplus;

/**
 * Created by user on 05/11/2017.
 */

public class ChatData {
    private String text;
    private String name;
    private String photoUrl;

    public ChatData(){

    }

    public ChatData(String text, String name, String photoUrl){
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;

    }

    public String getText(){ return text;}
    public void setText(String text){ this.text = text; }
    public String getName(){ return name;}
    public void setName(String name){ this.name = name; }
    public String getPhotoUrl(){ return photoUrl;}
    public void setPhotoUrl(String photoUrl){ this.photoUrl = photoUrl; }
}
