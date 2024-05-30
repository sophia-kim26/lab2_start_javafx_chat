package sockets;

public class MessageStoC_Kick extends Message{
    public String kicker;
    public String kickee;

    public MessageStoC_Kick(String kicker, String kickee) {
        this.kicker = kicker;
        this.kickee = kickee;
    }

    public String toString() {
        return "Kick action from " + kicker + " to " + kickee;
    }
}
