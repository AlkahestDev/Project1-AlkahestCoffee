package me.dumfing.multiplayerTools;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.HashMap;

/**
 * Created by dumpl on 4/18/2017.
 */
public class MultiplayerTools {
    public static final int UDPPORT = 8625;
    public static final int TCPPORT = 8626;
    public static void register(EndPoint endpoint){
        Kryo serializer = endpoint.getKryo();
        serializer.register(PlayerInfo.class);
        serializer.register(ServerSummary.class);
        serializer.register(ServerInfoRequest.class);
    }
    public static class ServerInfoRequest{
        public String svIP;
        public ServerInfoRequest(){

        }
        public ServerInfoRequest(String svIP){
            this.svIP = svIP;
        }
    }
    public static class ServerSummary{
        public int num, max, ping;
        public String serverName, serverIP;
        public ServerSummary(){

        }
        public ServerSummary(int numPlayers, int maxPlayers, int ping, String serverName, String serverIP){
            this.num = numPlayers;
            this.max = maxPlayers;
            this.ping = ping;
            this.serverName = serverName;
            this.serverIP = serverIP;
        }
        public String toString(){
            return String.format("%20s %d/%d %d",serverName.substring(0,Math.min(20,serverName.length())),num,max,ping);
        }
    }
    public static class PlayerInfo{
        private Vector2 pPos;
        private int team, health;
        private String name;
        public PlayerInfo(float x, float y, int team, String name){
            this.pPos = new Vector2(x,y);
            this.team = team;
            this.name = name;
            this.health = health;
        }
        public PlayerInfo(Vector2 pos, int team, String name, int health){ // a simple version of a player that can be sent back and forth
            this.pPos = pos;
            this.team = team;
            this.name = name;
            this.health = health;
        }
        public PlayerInfo(PlayerSoldier sIn){
            this.pPos = sIn.getPos();
            this.team = sIn.getTeam();
            this.name = sIn.getName();
            this.health = sIn.getHealth();
        }
        public Vector2 getPos(){
            return this.pPos;
        }
        public float getX(){
            return this.pPos.x;
        }
        public float getY(){
            return this.pPos.y;
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

    public static class ConnectionRequest{
        public String playerName;
        public ConnectionRequest(String playerName){
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
