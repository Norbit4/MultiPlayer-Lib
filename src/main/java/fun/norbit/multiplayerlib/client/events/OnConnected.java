package fun.norbit.multiplayerlib.client.events;

import fun.norbit.multiplayerlib.client.ClientListener;
import fun.norbit.multiplayerlib.client.GameClient;
import fun.norbit.multiplayerlib.objects.Packet;

import java.util.UUID;

public class OnConnected extends ClientListener {

    @Override
    public void onMessageEvent(GameClient gameClient, Packet packet) {
        if(packet.getChannel().equals("CONNECTED_CHANNEL")){

            String clientUUIDString = (String) packet.getObject(String.class);

            UUID clientUUID = UUID.fromString(clientUUIDString);

            gameClient.setClientUUID(clientUUID);
        }
    }
}
