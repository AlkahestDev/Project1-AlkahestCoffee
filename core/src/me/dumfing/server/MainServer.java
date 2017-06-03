package me.dumfing.server;

import com.badlogic.gdx.math.Rectangle;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import me.dumfing.multiplayerTools.MultiplayerTools;
import me.dumfing.multiplayerTools.PlayerSoldier;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by dumpl on 4/18/2017.
 */
public class MainServer {
    private Server server;
    private boolean isRunning;
    private int maxPlayers;
    private int numPlayers = 0;
    private String svName;
    HashMap<Integer, PlayerSoldier> players;
    LinkedList<Connection> validConnections = new LinkedList<Connection>();
    LinkedList<ServerEvent> events = new LinkedList<ServerEvent>();
    public MainServer(final String svName, final int maxPlayers){
        this.maxPlayers = maxPlayers;
        this.svName = svName;
        this.players = new HashMap<Integer, PlayerSoldier>();
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
                if(validConnections.contains(connection)){
                    validConnections.remove(connection);
                    players.remove(connection.getID());
                    secureSendAll(new MultiplayerTools.ServerDetailedSummary(CoffeeServer.redTeamMembers.size(),CoffeeServer.bluTeamMembers.size(),players));
                }
                if(CoffeeServer.redTeamMembers.contains(connection.getID())){
                    CoffeeServer.redTeamMembers.remove(connection.getID());
                }
                else if(CoffeeServer.bluTeamMembers.contains(connection.getID())){
                    CoffeeServer.bluTeamMembers.remove(connection.getID());
                }

                super.disconnected(connection);
            }

            @Override
            public void received(Connection connection, Object o) {
                //We will be using a request and response system rather than periodically broadcasting to all clients as there's no easy way to have the server periodically update the clients
                System.out.println("Received "+o.getClass().getSimpleName());
                if(o instanceof MultiplayerTools.ClientInfoRequest){
                    System.out.println("received serverInfoRequest");
                    connection.sendTCP(new MultiplayerTools.ServerSummary(players.size(),maxPlayers,connection.getReturnTripTime(),svName.substring(0,Math.min(16,svName.length()))));
                    //connection.sendTCP(new MultiplayerTools.ServerSummary(numPlayers,maxPlayers,connection.getReturnTripTime(),svName.substring(0,Math.min(16,svName.length()))));
                }
                else if(o instanceof  MultiplayerTools.ClientConnectionRequest){
                    System.out.println("received ConnectionRequest");
                    MultiplayerTools.ClientConnectionRequest temp = (MultiplayerTools.ClientConnectionRequest) o;
                    MultiplayerTools.ServerResponse response;
                    if(numPlayers>=maxPlayers){
                        //Too many people
                        response = new MultiplayerTools.ServerResponse(MultiplayerTools.ServerResponse.ResponseCode.SERVERFULL);
                    }
                    else if(false){
                        //just in case anything needs to be added
                    }
                    else{
                        //Successful Connection
                        validConnections.add(connection);
                        players.put(connection.getID(),new PlayerSoldier(new Rectangle(2,5,1,2),0,temp.playerName));
                        events.add(new ServerEvent(ServerEvent.EventType.PLAYERCONNECTED,connection.getID()));
                        response = new MultiplayerTools.ServerResponse(MultiplayerTools.ServerResponse.ResponseCode.CLIENTCONNECTED);
                        quickSendAll(new MultiplayerTools.ServerDetailedSummary(CoffeeServer.redTeamMembers.size(),CoffeeServer.bluTeamMembers.size(),players));

                    }
                    connection.sendTCP(response);
                    if(CoffeeServer.svState == CoffeeServer.ServerState.RUNNINGGAME){
                        connection.sendTCP(new MultiplayerTools.ServerGameStarted());
                    }
                    System.out.println(temp.playerName);
                }
                else if(o instanceof MultiplayerTools.ClientPickedTeam){
                    System.out.println("Received ClientPickedTeam");
                    MultiplayerTools.ClientPickedTeam temp = (MultiplayerTools.ClientPickedTeam)o;
                    //(temp.getPicked()==0?CoffeeServer.redTeamMembers:CoffeeServer.bluTeamMembers).add(connection); // add the client to their selected team
                    players.get(connection.getID()).setTeam(temp.getPicked());
                    events.add(new ServerEvent(ServerEvent.EventType.PLAYERPICKEDTEAM,connection.getID()));
                    secureSendAll(new MultiplayerTools.ServerDetailedSummary(CoffeeServer.redTeamMembers.size(),CoffeeServer.bluTeamMembers.size(),players));
                }
                else if(o instanceof MultiplayerTools.ClientPickedLoadout){
                    MultiplayerTools.ClientPickedLoadout temp = (MultiplayerTools.ClientPickedLoadout) o;
                    players.get(connection.getID()).setCurrentClass(temp.getLoadout());
                    players.get(connection.getID()).setPos(2,5);
                    events.add(new ServerEvent(ServerEvent.EventType.PLAYERPICKEDCLASS,connection.getID()));
                    (players.get(connection.getID()).getTeam()==0?CoffeeServer.redTeamMembers:CoffeeServer.bluTeamMembers).add(connection.getID()); // the client needs to be in a team to start being simulated
                    connection.sendTCP(new MultiplayerTools.ServerNotifyGame(0)); // tell the client which world is being used and what their ID is
                }
                else if(o instanceof  MultiplayerTools.ClientSentChatMessage){
                    quickSendAll(new MultiplayerTools.ServerSentChatMessage(o,connection,players));
                }
                else if(o instanceof MultiplayerTools.ClientKeysUpdate){
                    players.get(connection.getID()).setKeysHeld(((MultiplayerTools.ClientKeysUpdate) o).getKeys());
                    System.out.println(players.get(connection.getID()).getMouseAngle());
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
    public LinkedList<ServerEvent> getEvents(){
        LinkedList<ServerEvent> temp = new LinkedList<ServerEvent>(events);
        events.clear();
        return temp;
    }
    public boolean running(){
        return this.isRunning;
    }
    public HashMap<Integer, PlayerSoldier> getPlayers(){
        return this.players;
    }
    public int getMaxPlayers(){
        return this.maxPlayers;
    }
    public void secureSendAll(Object o){
        //TODO reverse list of players every time to average out delay from sending object to each client
        //players.keySet is all players that are actually playing the game
        for(Connection c : validConnections){
            c.sendTCP(o);
        }
    }
    public void quickSendAll(Object o){
        for(Connection c : validConnections){
            c.sendUDP(o);
        }
    }
}
