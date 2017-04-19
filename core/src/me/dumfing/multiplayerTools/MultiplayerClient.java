package me.dumfing.multiplayerTools;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;


/**
 * Created by dumpl on 4/19/2017.
 */
public class MultiplayerClient {
    private Client playerClient;
    public MultiplayerClient(){
        playerClient = new Client();
        MultiplayerTools.register(playerClient);
        playerClient.addListener(new Listener.ThreadedListener(new Listener(){
            @Override
            public void connected(Connection connection) {
                System.out.printf("Successfully connected to %s",connection.getID());
                super.connected(connection);
            }
            @Override
            public void disconnected(Connection connection) {
                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object o) {
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
    public void requestWorld(){ // asks server for world info
        playerClient.sendUDP(new MultiplayerTools.RequestWorld());
    }
}
