# MultiPlayer-Lib
🎮 Library for easier creation of multiplayer games

**Server**
- [Start](https://github.com/Norbit4/MultiPlayer-Lib#server "Click")
- [Listeners](https://github.com/Norbit4/MultiPlayer-Lib#listeners "Click")
- [Commands](https://github.com/Norbit4/MultiPlayer-Lib#commands "Click")

**Client**
- [Start](https://github.com/Norbit4/MultiPlayer-Lib#client "Click")
- [Listeners](https://github.com/Norbit4/MultiPlayer-Lib#listeners-1 "Click")


#
<h3>Server</h3>

- Creating the server

```java
int port = 5478;

//create server object
GameServer gameServer = new GameServer(port);

//start sever
gameServer.start();

//close server
gameServer.close();
```

- Sending message to client/clients

```java

//create packet object
String message = "hi!";
String channel = "MESSAGE_CHANNEL";

Packet packet = new Packet(channel, message);

ConnectedClient connectedClient = gameServer.getClientsList().get(0);

//send message to client
gameServer.sendObject(packet, connectedClient);

//send message to all clients
gameServer.sendObject(packet);    
```

<h4>Listeners</h4>

*Creating listeners*

- Message listener

```java
public class GameServerListener extends ServerListener {

  @Override
  public void onMessageEvent(ConnectedClient client, Packet packet) {

      if(packet.getChannel().equals("MESSAGE_CHANNEL")){

          String receivedMessage = packet.getObject(String.class);

          System.out.println(receivedMessage);
      }
   }
}  
```

- Join/Leave listener

```java
public class ServerListener extends ServerListener {

  @Override
  public void onJoinEvent(ServerClient client) {
      System.out.println("[+] " + client.getClientUUID());
  }

  @Override
  public void onLeaveEvent(ServerClient client) {
      System.out.println("[-] " + client.getClientUUID());
  }
}  
```

- Cmd listener

```java
public class CmdListener extends ServerListener {

  @Override
  public void onCmdEvent(String command, List<String> args) {
      if(command.equals("stop")) {
          //log
      }
  }
}  
```

*Registering listeners*

```java
gameServer.registerListener(new MessageListener());
gameServer.registerListener(new CmdListener());
gameServer.registerListener(new ClientListener());
```

<h4>Commands</h4>

*Creating commands*

```java
public class AlertCommmand implements CommandExecutor {

    @Override
    public void onCommand(String command, List<String> args, GameServer gameServer) {
        if(!args.isEmpty()) {
            String message = args.get(0);

            gameServer.sendObject(new Packet("ALERT_CHANNEL", message));
        }
    }
}
```

*Registering commands*

```java
gameServer.addCommand("alert", new AlertCommmand());
```

#
<h3>Client</h3>

- Creating the client

```java
GameClient gameClient = new GameClient();

String serverIp = "localhost";
int port = 5478;

gameClient.connect(serverIp , port);
```

- Sending message to server

```java
String message = "Hi " + gameClient.getClientUUID();
String channel = "MESSAGE_CHANNEL";

Packet packet = new Packet(channel, message);  
```

<h4>Listeners</h4>

*Creating listeners*

- Message listener

```java
public class MessageListener extends ClientListener {

    @Override
    public void onMessageEvent(GameClient gameClient, Packet packet) {

        if(packet.getChannel().equals("ALERT_CHANNEL")) {
            String message = packet.getObject(String.class);

            System.out.println(message);
        }
    }
}
```

- Join/Leave listener

```java
  public class ExampleListener extends ClientListener {

      @Override
      public void onConnect(GameClient client) {
          System.out.println("Connected");
      }

      @Override
      public void onDisconnect(GameClient client) {
          System.out.println("Disconnected");
      }
  }
```

*Registering listeners*

```java
gameClient.registerListener(new ExampleListener());
gameClient.registerListener(new MessageListener());
```



