package fun.norbit.multiplayerlib.server;

import fun.norbit.multiplayerlib.objects.Packet;

import java.util.List;

public class ServerListener {

    public void onMessageEvent(ConnectedClient client, Packet packet){
    }

    public void onJoinEvent(ServerClient client){
    }

    public void onLeaveEvent(ServerClient client){
    }

    public void onCmdEvent(String command, List<String> args){
    }
}
