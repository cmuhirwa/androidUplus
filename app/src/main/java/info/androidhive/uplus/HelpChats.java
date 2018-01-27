package info.androidhive.uplus;

/**
 * Created by user on 15/11/2017.
 */

public class HelpChats {

    private String chatUserName;
    private String chatMsg;
    private String chatName;
    //setting up constructor to get chat details
    public HelpChats() {
    }
    public HelpChats(String chatUserName, String chatMsg, String chatName){

        this.setChatUserName(chatUserName);
        this.setChatMsg(chatMsg);
        this.setChatName(chatName);
    }
    //setting up set and get for all chat details
    public String getChatUserName() {

        return chatUserName;
    }

    public void setChatUserName(String chatUserName) {

        this.chatUserName = chatUserName;
    }

    public String getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(String chatMsg) {
        this.chatMsg = chatMsg;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }



}
