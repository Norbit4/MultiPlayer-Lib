package fun.norbit.multiplayerlib.server.cmd;

import fun.norbit.multiplayerlib.server.GameServer;

import java.util.List;

public interface CommandExecutor {

    void onCommand(String command, List<String> args, GameServer gameServer);
}
