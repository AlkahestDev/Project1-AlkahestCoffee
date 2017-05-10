package me.dumfing.server;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.esotericsoftware.kryonet.Connection;
import me.dumfing.multiplayerTools.MultiplayerTools;
import me.dumfing.multiplayerTools.PlayerSoldier;

import java.util.HashMap;

/**
 * Created by dumpl on 5/4/2017.
 */
public class GameWorld {
    Pixmap worldHitbox;
    HashMap<Connection, PlayerSoldier> players;
    public GameWorld(HashMap<Connection, PlayerSoldier> players){
        this.players = players;
        for(PlayerSoldier playerSoldier : players.values()){
            playerSoldier.setPos( 2, 5);
        }
    }
    public void update(){
        for(Connection connection: players.keySet()){
            PlayerSoldier p = players.get(connection);
            if(CoffeeServer.redTeamMembers.contains(connection) || CoffeeServer.bluTeamMembers.contains(connection)) {
                System.out.println("Checkkkk");
                checkCollisions(p);
            }
        }
    }
    public void setCollisionBoxes(FileHandle pixMapFile){
        this.worldHitbox = new Pixmap(pixMapFile);
    }
    public void checkCollisions(PlayerSoldier player){
            System.out.println(player.getPos());
            if(worldHitbox.getPixel((int)player.getX(),worldHitbox.getHeight()-(int)(player.getY()+player.getvY()))>>8 == 1){
                System.out.println("hitY");
                player.setY((int)player.getY()+1);
            }
            else{
                player.setVy(player.getvY()-0.981f);
            }
            if(worldHitbox.getPixel((int)(player.getX()+player.getvX()),worldHitbox.getHeight()-(int)(player.getY()))>>8==1){
                System.out.println("hitX");
                player.setX((int)player.getX());
            }
    }
    public void moveAll(){
        for(PlayerSoldier playerSoldier : players.values()){
                playerSoldier.move();
        }
    }
    public HashMap<Integer,MultiplayerTools.ServerPlayerInfo> getSimpleInfo(){
        HashMap<Integer,MultiplayerTools.ServerPlayerInfo> out = new HashMap<Integer, MultiplayerTools.ServerPlayerInfo>();
        for(Connection c : players.keySet()){
            out.put(c.getID(),players.get(c).getPlayerInfo());
        }
        return out;
    }
}
