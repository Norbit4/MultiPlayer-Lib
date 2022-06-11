package fun.norbit.multiplayerlib.server;

import java.util.UUID;

public abstract class ServerClient {

    public abstract UUID getClientUUID();

    public abstract GameServer getGameServer();
}
