package fun.norbit.multiplayerlib.server;

import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class ConnectedClient extends ServerClient{

    private final ClientHandler clientHandler;
    private final UUID clientUUID;
    private final Socket socket;
    private final GameServer gameServer;

    public ConnectedClient(Socket socket, GameServer gameServer, ExecutorService executorService) {

        this.gameServer = gameServer;
        this.socket = socket;
        this.clientHandler = new ClientHandler(this);
        this.clientUUID = UUID.randomUUID();

        executorService.submit(clientHandler);
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public UUID getClientUUID() {
        return clientUUID;
    }

    public void sendObject(Object object){
        clientHandler.sendObject(object);
    }

    public Socket getSocket() {
        return socket;
    }

    public GameServer getGameServer() {
        return gameServer;
    }

    public void kick(){
        this.clientHandler.close();
    }
}
