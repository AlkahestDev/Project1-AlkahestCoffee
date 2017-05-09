package me.dumfing.multiplayerTools;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import me.dumfing.client.maingame.GameState;
import me.dumfing.client.maingame.MainGame;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by dumpl on 4/19/2017.
 */
public class MultiplayerClient {
    private Runnable findServers = new Runnable() {
        @Override
        public void run() {
            findingServers = true;
            serverSummaries = new HashMap<String, MultiplayerTools.ServerSummary>(); // set the client's server list to empty
            for(InetAddress addr : findServers()){ // iterates through the list of servers that the client found running on the designated ports
                if(!addr.getHostAddress().equals("127.0.0.1")) { //Disallow connecting to localhost to prevent duplicate results if you're hosting on the same computer you're playing on
                    pingServer(addr.getHostAddress()); // connect to the server and ask for information about it in return
                    try {
                        Thread.sleep(500); // give the server 500 milliseconds to respond before disconnecting to move on to the next server
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
    private LinkedList<String> messages = new LinkedList<String>();
    private boolean findingServers = false;
    private Client playerClient;
    private HashMap<String, MultiplayerTools.ServerSummary> serverSummaries;
    private int redTeam = 0;
    private int blueTeam = 0;
    private int rLimit = 0;
    private int bLimit = 0;
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
                // if you disconnected while doing some server related activity
                // non server related activity that will trigger the disconnected event
                // includes pinging servers
                System.out.println("disconnected "+MainGame.state);
                if(GameState.ONLINESTATES.contains(MainGame.state)){
                    MainGame.state = GameState.State.SERVERBROWSER; // go back to the server browser
                }
                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object o) {
                System.out.print("Got info! "+connection.getID()+" ");
                if(o instanceof MultiplayerTools.ServerSummary){
                    System.out.println("ServerSummary");
                    MultiplayerTools.ServerSummary temp = (MultiplayerTools.ServerSummary) o;
                    serverSummaries.put(connection.getRemoteAddressUDP().toString(),temp);
                    connection.close();
                }
                else if(o instanceof  MultiplayerTools.ServerResponse){
                    System.out.println("ServerResponse");
                    MultiplayerTools.ServerResponse temp = (MultiplayerTools.ServerResponse) o;
                    switch (temp.response){
                        case CLIENTCONNECTED:
                            //Yay, we now have a spot in the server dedicated to us
                            //wait for detailed server summary
                            MainGame.state = GameState.State.GAMELOBBY;
                            break;
                        case SERVERFULL:
                            playerClient.close();
                            System.out.println("Server was full");
                            break;
                    }
                }
                else if(o instanceof MultiplayerTools.ServerGameCountdown){
                    System.out.println("ServerGameCountdown");
                    MultiplayerTools.ServerGameCountdown temp = (MultiplayerTools.ServerGameCountdown)o;
                    System.out.println(temp.seconds);
                }
                else if(o instanceof MultiplayerTools.ServerDetailedSummary){
                    System.out.println("deets");
                    MultiplayerTools.ServerDetailedSummary temp = (MultiplayerTools.ServerDetailedSummary)o;
                    redTeam = temp.rTeam;
                    blueTeam = temp.bTeam;
                    rLimit = temp.rMax;
                    bLimit = temp.bMax;
                    System.out.println(String.format("R: %d/%d B: %d/%d",temp.rTeam,temp.rMax,temp.bTeam,temp.bMax));
                }
                else if(o instanceof MultiplayerTools.ServerSentChatMessage){
                    if(messages.size()>8){ // keep the linkedlist short
                        messages.removeLast();
                    }
                    messages.offerFirst(((MultiplayerTools.ServerSentChatMessage) o).message);
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
            playerClient.connect(3000,svIP,MultiplayerTools.TCPPORT, MultiplayerTools.UDPPORT); // connect with 3000ms timeout
        } catch (IOException e) {
            System.err.println("Could not connect to server!");
            e.printStackTrace();
            MainGame.state = GameState.State.SERVERBROWSER;
        }
        System.out.println(MainGame.state);
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

    /**
     *
     * @return The ip address of the first server that responds
     */
    public InetAddress findServer(){
        return playerClient.discoverHost(MultiplayerTools.UDPPORT, 1000);
    }
    public void pingServer(String serverIP){
        try {
            playerClient.connect(100,serverIP,MultiplayerTools.TCPPORT, MultiplayerTools.UDPPORT);
            secureSend(new MultiplayerTools.ClientInfoRequest());
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

    /**
     * Use this to connect to the server when you intend on joining the game
     * @param serverIP The server's ip
     */
    public void connectServerPlay(String serverIP){
        connectToServer(serverIP);
        MultiplayerTools.ClientConnectionRequest clientConnectionRequest = new MultiplayerTools.ClientConnectionRequest(MainGame.clientSoldier.getName());
        secureSend(clientConnectionRequest);
    }
    /**
     * Sends the object over UDP, faster but higher chance of lost information
     * @param toSend Object to send over UDP
     */
    public void quickSend(Object toSend){
        playerClient.sendUDP(toSend);
    }

    /**
     * Sends the object over TCP, slower but smaller chance of lost information
     * @param toSend Object to send over TCP
     */
    public void secureSend(Object toSend){
        playerClient.sendTCP(toSend);
    }

    /**
     * Returns how many people are on the red team
     * @return
     */
    public int getRedTeam() {
        return redTeam;
    }

    /**
     * Returns how many people are on the blue team
     * @return
     */
    public int getBlueTeam() {
        return blueTeam;
    }

    /**
     * Gets the max amount of people that can be on the red team
     * @return
     */
    public int getrLimit() {
        return rLimit;
    }

    /**
     * Gets the max amount of people that can be on the blue team
     * @return
     */
    public int getbLimit() {
        return bLimit;
    }

    /**
     * Gets the text messages the client contains
     * @return A LinkedList of messages that have been sent
     */
    public LinkedList<String> getMessages(){
        return this.messages;
    }
}
