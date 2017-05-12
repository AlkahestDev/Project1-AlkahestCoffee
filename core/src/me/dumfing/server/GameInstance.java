package me.dumfing.server;

import com.esotericsoftware.kryonet.Connection;
import me.dumfing.multiplayerTools.MultiplayerTools;
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
        world.update();
        if(frameCount == 5){ // 2 gets an interesting 30hz
            frameCount = 0;
            sv.quickSendAll(new MultiplayerTools.ServerPlayerPositions(world.getSimpleInfo()));
        }
        frameCount++;
    }
}
