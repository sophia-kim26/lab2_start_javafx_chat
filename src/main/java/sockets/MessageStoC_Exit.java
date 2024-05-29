package sockets;

import java.util.ArrayList;

public class MessageStoC_Exit extends Message {
    public String userName;
    ArrayList<String> userList;

    public MessageStoC_Exit(String userName, ArrayList<String> list) {
        this.userName = userName;
        this.userList = list;
    }

    public String toString() {
        return "Exitting: " + userName;
    }

}