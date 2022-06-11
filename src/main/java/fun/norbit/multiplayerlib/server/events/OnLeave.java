package fun.norbit.multiplayerlib.server.events;


import fun.norbit.multiplayerlib.server.ConnectedClient;
import fun.norbit.multiplayerlib.server.ServerClient;
import fun.norbit.multiplayerlib.server.ServerListener;

public class OnLeave extends ServerListener {

    @Override
    public void onLeaveEvent(ServerClient client) {

        client.getGameServer().getClientsList().remove((ConnectedClient) client);
    }
}
