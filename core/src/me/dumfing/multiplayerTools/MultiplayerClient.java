package me.dumfing.multiplayerTools;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.dumfing.maingame.MainGame;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;


/**
 * Created by dumpl on 4/19/2017.
 */
public class MultiplayerClient {
    private Runnable findServers = new Runnable() {
        @Override
        public void run() {
            findingServers = true;
            serverSummaries = new HashMap<String, MultiplayerTools.ServerSummary>();
            for(InetAddress addr : findServers()){
                if(!addr.getHostAddress().equals("127.0.0.1")) { //Disallow connecting to localhost to prevent duplicate results if you're hosting on the same computer you're playing on
                    pingServer(addr.getHostAddress());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    disconnect();
                }
            }
            findingServers = false;
            // since I have no easy of checking whether or not this is done checking for new servers,
            // I'm forced to make serverBrowser static and populate it's list from in here.
            MainGame.serverBrowser.populateServerList(serverSummaries);
        }
    };

    private boolean findingServers = false;
    private Client playerClient;
    private HashMap<String, MultiplayerTools.ServerSummary> serverSummaries;
    public MultiplayerClient(){
        playerClient = new Client();
        serverSummaries = new HashMap<String, MultiplayerTools.ServerSummary>();
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
                System.out.println("Got info! "+connection.getID());
                if(o instanceof MultiplayerTools.ServerSummary){
                    MultiplayerTools.ServerSummary temp = (MultiplayerTools.ServerSummary) o;
                    serverSummaries.put(connection.getRemoteAddressUDP().toString(),temp);
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
            playerClient.connect(3000,svIP,MultiplayerTools.TCPPORT, MultiplayerTools.UDPPORT);
        } catch (IOException e) {
            System.err.println("Could not connect to server!");
            e.printStackTrace();
        }
    }
    public void connectToServer(InetAddress svIP){
        connectToServer(svIP.getHostAddress());
    }

    /**
     * Finds the servers broadcasting on the ports designated for the game<br>
     * Should be run in a background thread to prevent delays
     * @return A List of addresses with all found servers
     */
    private List<InetAddress> findServers(){
        return playerClient.discoverHosts(MultiplayerTools.UDPPORT,1000);
    }

    /**
     * Will repopulate the client's Server summaries list<br>
     * Pings all servers the client can find, populating the serverSummaries array
     * with their ip and ServerSummary
     */
    public void pingServers(){
        new Thread(findServers).start();
    }
    public InetAddress findServer(){
        return playerClient.discoverHost(MultiplayerTools.UDPPORT, 1000);
    }
    public void pingServer(String serverIP){
        try {
            playerClient.connect(100,serverIP,MultiplayerTools.TCPPORT, MultiplayerTools.UDPPORT);
            playerClient.sendTCP(new MultiplayerTools.ServerInfoRequest());
        } catch (IOException e) {
            System.out.println("Could not connect to "+serverIP);
        }
    }
    public void disconnect(){
        playerClient.close();
    }

    public boolean isFindingServers() {
        return findingServers;
    }

    public HashMap<String, MultiplayerTools.ServerSummary> getServers() {
        return serverSummaries;
    }

    public void requestWorld(){ // asks server for world info
        playerClient.sendUDP(new MultiplayerTools.RequestWorld());
    }
}
