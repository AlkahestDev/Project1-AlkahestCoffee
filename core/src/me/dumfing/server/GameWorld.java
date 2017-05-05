package me.dumfing.server;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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
            if(worldHitbox.getPixel((int)(player.getX()+player.getvX()),(int)(player.getY()))==1){
                System.out.println("hit");
            }
        }
    }
}
