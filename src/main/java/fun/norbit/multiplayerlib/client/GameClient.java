package fun.norbit.multiplayerlib.client;

import fun.norbit.multiplayerlib.client.events.OnConnected;
import fun.norbit.multiplayerlib.objects.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameClient {

    private String closeServerMessage;
    private Socket socket;
    private boolean connected;
    private UUID clientUUID;
    private final List<ClientListener> clientListeners;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public GameClient() {
        this.closeServerMessage = "Server closed";
        this.clientListeners = new ArrayList<>();
        this.registerListener(new OnConnected());
    }

    public void connect(String IP, int PORT){
        try {
            this.socket = new Socket(IP, PORT);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());

            listenMessage();
            connected = true;
            clientListeners.forEach(clientListener -> clientListener.onConnect(this));
        } catch (IOException e) {
            System.out.println(closeServerMessage);

        }
    }

    public void disconnect(){
        try {
            socket.close();
            connected = false;
            clientListeners.forEach(clientListener -> clientListener.onDisconnect(this));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T extends Packet> void sendObject(T object){
        if(socket != null) {
            if (!socket.isClosed()) {
                try {
                    objectOutputStream.writeObject(object);
                    objectOutputStream.flush();

                } catch (IOException e) {
                    disconnect();
                }
            }
        }
    }

    private void listenMessage(){
        new Thread(() -> {
            while (socket.isConnected()){
                try {
                    Object object = objectInputStream.readObject();
                    Packet packet = (Packet) object;

                    clientListeners.forEach(clientListener -> clientListener.onMessageEvent(this, packet));

                } catch (IOException | ClassNotFoundException e) {
                    disconnect();
                    break;
                }
            }
        }).start();
    }

    public void registerListener(ClientListener clientListener){
        clientListeners.add(clientListener);
    }

    public boolean isConnected() {
        return connected;
    }

    public UUID getClientUUID() {
        return clientUUID;
    }

    public void setClientUUID(UUID clientUUID) {
        this.clientUUID = clientUUID;
    }

    public void setCloseServerMessage(String closeServerMessage) {
        this.closeServerMessage = closeServerMessage;
    }
}


