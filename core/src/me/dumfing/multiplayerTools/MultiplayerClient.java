package me.dumfing.multiplayerTools;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;


/**
 * Created by dumpl on 4/19/2017.
 */
public class MultiplayerClient {
    private Client playerClient;
    private HashMap<Connection, MultiplayerTools.ServerSummary> serverSummaries;
    public MultiplayerClient(){
        playerClient = new Client();
        serverSummaries = new HashMap<Connection, MultiplayerTools.ServerSummary>();
        MultiplayerTools.register(playerClient);
        playerClient.addListener(new Listener.ThreadedListener(new Listener(){
            @Override
            public void connected(Connection connection) {
                System.out.printf("Successfully connected to %s\n",connection.getID());
                super.connected(connection);
            }
            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object o) {
                System.out.println("Got info!");
                if(o instanceof MultiplayerTools.ServerSummary){
                    System.out.println("Info");
                    MultiplayerTools.ServerSummary temp = (MultiplayerTools.ServerSummary) o;
                    serverSummaries.put(connection,temp);
                    connection.close();
                }
                super.received(connection, o);
            }

            @Override
            public void idle(Connection connection) {
                super.idle(connection);
            }
        }));
    }
    public void startClient(){
        playerClient.start();
    }
    public void stopClient(){
        playerClient.stop();
    }
    public void connectToServer(String svIP){
        try {
            playerClient.connect(5000,svIP,MultiplayerTools.TCPPORT, MultiplayerTools.UDPPORT);
        } catch (IOException e) {
            System.err.println("Could not connect to server!");
            e.printStackTrace();
        }
    }
    public void connectToServer(InetAddress svIP){
        connectToServer(svIP.getHostAddress());
    }
    public List<InetAddress> findServers(){
        return playerClient.discoverHosts(MultiplayerTools.UDPPORT,1000);
    }
    public InetAddress findServer(){
        return playerClient.discoverHost(MultiplayerTools.UDPPORT, 1000);
    }
    public void pingServer(String serverIP){
        try {
            playerClient.connect(50,serverIP,MultiplayerTools.TCPPORT, MultiplayerTools.UDPPORT);
        } catch (IOException e) {
            System.out.println("Could not connect to "+serverIP);
        }
    }

    public HashMap<Connection, MultiplayerTools.ServerSummary> getServers() {
        return serverSummaries;
    }

    public void requestWorld(){ // asks server for world info
        playerClient.sendUDP(new MultiplayerTools.RequestWorld());
    }
}
