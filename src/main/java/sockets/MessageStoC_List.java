package sockets;

import java.util.ArrayList;

public class MessageStoC_List extends Message {
    ArrayList<String> userList;

    public MessageStoC_List(ArrayList<String> list) {
        this.userList = list;
    }

    public String toString() {
        return String.join(", ", userList);
    }
}
