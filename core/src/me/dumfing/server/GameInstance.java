package me.dumfing.server;

import com.esotericsoftware.kryonet.Connection;
import me.dumfing.multiplayerTools.PlayerSoldier;

import java.util.HashMap;

/**
 * Created by dumpl on 5/4/2017.
 */
public class GameInstance {
    int frameCount = 0;
    GameWorld world;
    public GameInstance(HashMap<Connection,PlayerSoldier> players){
        world = new GameWorld(players);
    }
    public void update(MainServer sv){
        world.checkCollisions();
        world.moveAll();
        if(frameCount == 3){
            frameCount = 0;
            sv.quickSendAll(world.getSimpleInfo());
        }
        frameCount++;
    }
}
