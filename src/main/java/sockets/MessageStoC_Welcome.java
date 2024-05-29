package sockets;

import java.util.ArrayList;

public class MessageStoC_Welcome extends Message {
    public String userName;
    ArrayList<String> userList;

    public MessageStoC_Welcome(String userName, ArrayList<String> list) {
        this.userName = userName;
        this.userList = list;
    }

    public String toString() {
        return "Welcome: " + userName + userList + "DEBUG";
    }

}