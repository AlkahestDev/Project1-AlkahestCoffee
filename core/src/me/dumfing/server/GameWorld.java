package me.dumfing.server;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.esotericsoftware.kryonet.Connection;
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
    }
    public void setCollisionBoxes(FileHandle pixMapFile){
        this.worldHitbox = new Pixmap(pixMapFile);
    }
    public void checkCollisions(){
        for(PlayerSoldier player : players.values()){
            if(worldHitbox.getPixel((int)(player.getX()+player.getvX()),worldHitbox.getHeight()-(int)(player.getY()))==1){
                System.out.println("hitX");
                player.setX((int)player.getX());
            }
            if(worldHitbox.getPixel((int)player.getY(),worldHitbox.getHeight()-(    int)(player.getY()+player.getvY())) == 1){
                System.out.println("hitY");
                player.setY((int)player.getY());
            }
        }
    }
    public void moveAll(){
        for(PlayerSoldier playerSoldier : players.values()){
            playerSoldier.move();
        }
    }
}
