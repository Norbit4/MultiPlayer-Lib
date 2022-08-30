# MultiPlayer-Lib
Library for creating multiplayer games

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
    String channel = "message";

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

          if(packet.getChannel().equals("c1")){

              String receivedMessage = packet.getObject(String.class);

              System.out.println(receivedMessage);
          }
      }
  }  
```

- Join/Leave listener

```java

  public class ClientListener extends ServerListener {

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



