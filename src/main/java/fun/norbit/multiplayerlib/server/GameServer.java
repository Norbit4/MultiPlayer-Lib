package fun.norbit.multiplayerlib.server;

import fun.norbit.multiplayerlib.objects.Packet;
import fun.norbit.multiplayerlib.server.cmd.CommandExecutor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    private final static List<GameServer> servers = new ArrayList<>();
    private final List<ServerListener> serverListeners;
    private final  List<ConnectedClient> clientsList;
    private final ExecutorService service;
    private ServerSocket serverSocket;
    private final int port;
    private boolean running;
    private final HashMap<String, CommandExecutor> commandExecutorHashMap;
    private boolean enableJoinLeaveMessages;

    public GameServer(int port){
        this.clientsList = new ArrayList<>();
        this.serverListeners = new ArrayList<>();
        this.service = Executors.newCachedThreadPool();
        this.port = port;
        this.commandExecutorHashMap = new HashMap<>();
        servers.add(this);
    }

    public void start(){
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.running = true;

        System.out.println("Server started!");
        openServer();
        cmdTask();
    }

    private void openServer(){
        service.submit(() -> {
            while (!serverSocket.isClosed()){
                try {
                    Socket socket = serverSocket.accept();
                    ConnectedClient client = new ConnectedClient(socket, this, service);

                    serverListeners.forEach(serverListener -> serverListener.onJoinEvent(client));
                    clientsList.add(client);

                    if(isEnableJoinLeaveMessages()) {
                        System.out.println("[+] " + socket.getLocalAddress());
                    }
                    //uuid to client
                    sendObject(new Packet("CONNECTED_CHANNEL",client.getClientUUID().toString()), client);

                } catch (IOException e) {
                    System.out.println("Server stoped!");
                }
            }
        });
    }

    private void cmdTask(){
        service.submit(() -> {
            Scanner scanner = new Scanner(System.in);
            while (!serverSocket.isClosed()){
                String cmd = scanner.nextLine();

                String[] cmdArgs = cmd.split(" ");

                if(commandExecutorHashMap.containsKey(cmdArgs[0])) {
                    List<String> argsList = new LinkedList<>(Arrays.asList(cmdArgs));
                    argsList.remove(0);

                    serverListeners.forEach(serverListener -> serverListener.onCmdEvent(cmdArgs[0], argsList));
                    commandExecutorHashMap.get(cmdArgs[0]).onCommand(cmdArgs[0], argsList, this);
                }else if(cmdArgs[0].equals("end")){

                    commandExecutorHashMap.get(cmdArgs[0]).onCommand("end", null, this);
                    this.close();
                }else{
                    System.out.println("Invalid CMD");
                }
            }
        });
    }

    public void close(String closeMessage){
        try {
            System.out.println(closeMessage);
            running = false;
            clientsList.forEach(ConnectedClient::kick);
            serverSocket.close();
            service.shutdown();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void close(){
        try {
            running = false;
            clientsList.forEach(ConnectedClient::kick);
            serverSocket.close();
            service.shutdown();
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public <T extends Packet> void sendObject(T object){

        clientsList.forEach(client -> client.sendObject(object));
    }

    public void sendObject(Object object, ConnectedClient connectedClient){

        connectedClient.sendObject(object);
    }

    public void addCommand(String cmdName, CommandExecutor commandExecutor){
        commandExecutorHashMap.put(cmdName, commandExecutor);
    }

    public void setEnableJoinAndLeaveMessages(boolean enableJoinLeaveMessages) {
        this.enableJoinLeaveMessages = enableJoinLeaveMessages;
    }

    public boolean isEnableJoinLeaveMessages() {
        return enableJoinLeaveMessages;
    }

    public void registerListener(ServerListener serverListener){
        serverListeners.add(serverListener);
    }

    public static List<GameServer> getServers(){
        return servers;
    }

    public List<ConnectedClient> getClientsList() {
        return clientsList;
    }

    public boolean isRunning() {
        return running;
    }

    public List<ServerListener> getServerListeners() {
        return serverListeners;
    }
}
