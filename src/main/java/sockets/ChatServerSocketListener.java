package sockets;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.RadioButton;

public class ChatServerSocketListener  implements Runnable {
    private ClientConnectionData client;
    private List<ClientConnectionData> clientList;

    public ChatServerSocketListener(ClientConnectionData client, List<ClientConnectionData> clientList) {
        this.client = client;
        this.clientList = clientList;
    }

    private void processChatMessage(MessageCtoS_Chat m) {
        System.out.println("Chat received from " + client.getUserName() + " - broadcasting");
        broadcast(new MessageStoC_Chat(client.getUserName(), m.msg), null);
    }

    private void processKickMessage(MessageCtoS_Kick m) {
        // System.out.println("broadcasting: Kick action from " + client.getUserName() + " to " + m.kickee);
        broadcast(new MessageStoC_Kick(client.getUserName(), m.kickee), null);
        
        ClientConnectionData kickee = null;
        for (ClientConnectionData c : clientList) {
            if (c.getUserName().equals(m.kickee)) {
                kickee = c;
                break;
            }
        }
        try {
            kickee.getOut().writeObject(new MessageStoC_Kick(client.getUserName(), m.kickee));
            kickee.getSocket().close();
            clientList.remove(kickee);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Broadcasts a message to all clients connected to the server.
     */
    public void broadcast(Message m, ClientConnectionData skipClient) {
        try {
            System.out.println("broadcasting: " + m);
            for (ClientConnectionData c : clientList){
                // if c equals skipClient, then c.
                // or if c hasn't set a userName yet (still joining the server)
                if ((c != skipClient) && (c.getUserName()!= null)){
                    c.getOut().writeObject(m);
                }
            }
        } catch (Exception ex) {
            System.out.println("broadcast caught exception: " + ex);
            ex.printStackTrace();
        }        
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = client.getInput();

            MessageCtoS_Join joinMessage = (MessageCtoS_Join)in.readObject();
            client.setUserName(joinMessage.userName);

            ArrayList<String> userList = new ArrayList<>();
            for (ClientConnectionData c : clientList) {
                userList.add(c.getUserName());
            }

            // Broadcast the welcome back to the client that joined. 
            // Their UI can decide what to do with the welcome message.
            broadcast(new MessageStoC_Welcome(joinMessage.userName, userList), null);
            
            while (true) {
                Message msg = (Message) in.readObject();
                if (msg instanceof MessageCtoS_Quit) {
                    break;
                }
                else if (msg instanceof MessageCtoS_Chat) {
                    processChatMessage((MessageCtoS_Chat) msg);
                }
                else if (msg instanceof MessageCtoS_Kick) {
                    processKickMessage((MessageCtoS_Kick) msg);
                }
                else {
                    System.out.println("Unhandled message type: " + msg.getClass());
                }
            }
        } catch (Exception ex) {
            if (ex instanceof SocketException) {
                System.out.println("Caught socket ex for " + 
                    client.getName());
            } else {
                System.out.println(ex);
                ex.printStackTrace();
            }
        } finally {
            //Remove client from clientList
            clientList.remove(client); 

            // Notify everyone that the user left.
            ArrayList<String> userList = new ArrayList<>();
            for (ClientConnectionData c : clientList) {
                userList.add(c.getUserName());
            }

            broadcast(new MessageStoC_Exit(client.getUserName(), userList), client);

            try {
                client.getSocket().close();
            } catch (IOException ex) {}
        }
    }
        
}
