package fun.norbit.multiplayerlib.server;

import fun.norbit.multiplayerlib.objects.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private final GameServer gameServer;
    private final ConnectedClient client;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private final InetAddress address;
    private boolean connected;

    public ClientHandler(ConnectedClient client) {

        this.socket = client.getSocket();
        this.address = socket.getLocalAddress();
        this.gameServer = client.getGameServer();
        this.client = client;
        this.connected = true;

        try {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                Object object = objectInputStream.readObject();
                Packet packet = (Packet) object;

                gameServer.getServerListeners().forEach(serverListener
                        -> serverListener.onMessageEvent(client, packet));
            } catch (IOException | ClassNotFoundException e) {
                end();
                break;
            }
        }
    }

    public void sendObject(Object object){
        try {
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void end(){
        try {
            connected = false;
            gameServer.getServerListeners().forEach(serverListener -> serverListener.onLeaveEvent(client));

            if(gameServer.isEnableJoinLeaveMessages()) {
                System.out.println("[-] " + address);
            }

            objectOutputStream.close();
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}