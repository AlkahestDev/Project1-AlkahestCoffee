package me.dumfing.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import me.dumfing.multiplayerTools.MultiplayerTools;
import me.dumfing.multiplayerTools.PlayerSoldier;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dumpl on 4/18/2017.
 */
public class MainServer {
    private Server server;
    private boolean isRunning;
    private int maxPlayers;
    private int numPlayers = 0;
    private String svName;
    HashMap<Connection, PlayerSoldier> players;
    public MainServer(final String svName, final int maxPlayers){
        this.maxPlayers = maxPlayers;
        this.svName = svName;
        this.players = new HashMap<Connection, PlayerSoldier>();
        this.server = new Server();
        MultiplayerTools.register(this.server);
        try {
            this.server.bind(MultiplayerTools.TCPPORT,MultiplayerTools.UDPPORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.server.addListener(new Listener.ThreadedListener(new Listener(){
            @Override
            public void connected(Connection connection) {
                System.out.printf("Client Connected! %d\n",connection.getID());
                connection.updateReturnTripTime();
                super.connected(connection);

            }

            @Override
            public void disconnected(Connection connection) {
                System.out.printf("Client Disconnected %d\n",connection.getID());
                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object o) {
                //We will be using a request and response system rather than periodically broadcasting to all clients as there's no easy way to have the server periodically update the clients
                System.out.println("Received Object");
                if(o instanceof MultiplayerTools.ClientInfoRequest){
                    System.out.println("received serverInfoRequest");
                    connection.sendTCP(new MultiplayerTools.ServerSummary(0,maxPlayers,connection.getReturnTripTime(),svName.substring(0,Math.min(16,svName.length()))));
                }
                else if(o instanceof  MultiplayerTools.ClientConnectionRequest){
                    System.out.println("received ConnectionRequest");
                    MultiplayerTools.ClientConnectionRequest temp = (MultiplayerTools.ClientConnectionRequest) o;
                    MultiplayerTools.ServerResponse response;
                    if(numPlayers>=maxPlayers){
                        response = new MultiplayerTools.ServerResponse(MultiplayerTools.ServerResponse.ResponseCode.SERVERFULL);
                    }
                    else if(false){

                    }
                    else{
                        players.put(connection,new PlayerSoldier(0,0,0,temp.playerName));
                        response = new MultiplayerTools.ServerResponse(MultiplayerTools.ServerResponse.ResponseCode.CLIENTCONNECTED);
                    }
                    connection.sendTCP(response);
                    System.out.println(temp.playerName);
                }
                super.received(connection, o);
            }

            @Override
            public void idle(Connection connection) {
                super.idle(connection);
            }
        }));
    }
    public void start(){
        this.server.start();
        this.isRunning = true;
    }
    public void stop(){
        this.server.stop();
        this.isRunning = false;
    }
    public boolean running(){
        return this.isRunning;
    }
    public HashMap<Connection, PlayerSoldier> getPlayers(){
        return this.players;
    }
    public int getMaxPlayers(){
        return this.maxPlayers;
    }
}
