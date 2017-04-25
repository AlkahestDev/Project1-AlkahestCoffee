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
    }
    public static class PlayerSoldier extends PlayerInfo{ // a more detailed version of the players that will be sent at the start but won't be sent around as much later
        private int health, maxHealth;
        private float vX, vY;
        public PlayerSoldier(float x, float y, int team){
            super(x,y,team,null);
            this.vX = 0;
            this.vY = 0;
            this.health = 100;
            this.maxHealth = 100;
        }
        public PlayerSoldier(float x, float y, int team, String name){
            super(x, y, team, name);
            this.vX = 0;
            this.vY = 0;
            this.health = 100;
            this.maxHealth = 100;
        }
        public void setMaxHealth(int maxHealth){
            this.maxHealth = maxHealth;
        }
        public PlayerInfo getPlayerInfo(){
            return new PlayerInfo(this); // a stripped down version of this for what other people see
        }
        public int getHealth(){
            return this.health;
        }
        public int getMaxHealth(){
            return this.maxHealth;
        }
        public void setName(String name){
            super.name = name;
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
