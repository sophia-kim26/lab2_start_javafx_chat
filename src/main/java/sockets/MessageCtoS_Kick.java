package sockets;

public class MessageCtoS_Kick extends Message{
    // public String kicker;
    public String kickee;

    public MessageCtoS_Kick(String kickee) {
        // this.kicker = kicker;
        this.kickee = kickee;
    }
}
