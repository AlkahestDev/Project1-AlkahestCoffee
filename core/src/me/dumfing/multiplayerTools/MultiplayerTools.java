package me.dumfing.multiplayerTools;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import java.util.HashMap;
/**
 * Objects that will be sent between the client and server as well as useful variables like ports<br>
 * All classes that the server will send will be prefixed with <b>Server</b> and all classes that the client will send will be prefixed with <b>Client</b>
 */
public class MultiplayerTools {
    public static final int UDPPORT = 19815;
    public static final int TCPPORT = 19816;
    public static void register(EndPoint endpoint){
        Kryo serializer = endpoint.getKryo();
        serializer.register(ClientPlayerInfo.class);
        serializer.register(ClientInfoRequest.class);
        serializer.register(ClientConnectionRequest.class);
        serializer.register(ServerSummary.class);
        serializer.register(ServerResponse.class);
        serializer.register(ServerResponse.ResponseCode.class);
        serializer.register(ServerDetailedSummary.class);
    }

    /**
     * TODO: determine what info should be sent in the detailed server summary
     */
    public static class ServerDetailedSummary {

    }
    /**
     * The response from the server for if the client has connected as a player or if they aren't allowed on
     */
    public static class ServerResponse{
        public enum ResponseCode{
            CLIENTCONNECTED,
            SERVERFULL
        }
        ResponseCode response;
        public ServerResponse(){}
        public ServerResponse(ResponseCode response){
            this.response = response;
        }
    }
    /**
     * Sent by the client to tell the server they want basic info about the server
     */
    public static class ClientInfoRequest {
        public ClientInfoRequest(){

        }
    }

    /**
     * Contains the ping, amount of people on, max people on, and server's name
     */
    public static class ServerSummary{
        public int num, max, ping;
        public String serverName;
        public ServerSummary(){

        }
        public ServerSummary(int numPlayers, int maxPlayers, int ping, String serverName){
            this.num = numPlayers;
            this.max = maxPlayers;
            this.ping = ping;
            this.serverName = serverName;
        }
        public String toString(){
            return String.format("%20s %d/%d %d",serverName.substring(0,Math.min(20,serverName.length())),num,max,ping);
        }
    }

    public static class ClientPlayerInfo {
        private Rectangle playerArea;
        private int team, health;
        private String name;
        public ClientPlayerInfo(Rectangle area, int team, String name){
            this.playerArea = area;
            this.team = team;
            this.name = name;
            this.health = 100;
        }
        public ClientPlayerInfo(Rectangle area, int team, String name, int health){ // a simple version of a player that can be sent back and forth
            this.playerArea = area;
            this.team = team;
            this.name = name;
            this.health = health;
        }
        public ClientPlayerInfo(PlayerSoldier sIn){
            this.playerArea = sIn.getRect();
            this.team = sIn.getTeam();
            this.name = sIn.getName();
            this.health = sIn.getHealth();
        }
        public Vector2 getPos(){
            return this.playerArea.getPosition(new Vector2());
        }
        public float getX(){
            return this.playerArea.x;
        }
        public float getY(){
            return this.playerArea.y;
        }
        public Rectangle getRect(){
            return this.playerArea;
        }
        public int getTeam(){
            return this.team;
        }
        public String getName(){
            return this.name;
        }
        public void setName(String name){
            this.name = name;
        }
        public void setTeam(int team) {
            this.team = team;
        }

        public int getHealth() {
            return health;
        }
    }
    /**
     * Sent by the client when they wish to connect to the server to play
     */
    public static class ClientConnectionRequest {
        public String playerName;
        public ClientConnectionRequest(){
        }
        public ClientConnectionRequest(String playerName){
            this.playerName = playerName;
        }
    }
    public static class RequestWorld{
        public RequestWorld(){
        }
    }
    public static class WorldInfo{
        HashMap<Connection, PlayerSoldier> world;
        public WorldInfo(HashMap<Connection, PlayerSoldier> world){
            this.world = world;
        }
    }
}
