package me.dumfing.server;

import me.dumfing.multiplayerTools.MultiplayerTools;
import me.dumfing.multiplayerTools.PlayerSoldier;

import java.util.HashMap;

/**
 * A class that manages the entire gameplay portion of the server
 */
public class ServerGameInstance {
    int frameCount = 0;
    GameWorld world;
    public ServerGameInstance(HashMap<Integer, PlayerSoldier> players){
        world = new GameWorld(players);
    }
    public void update(MainServer sv){
        world.serverUpdate();
        if(frameCount == 5){ // 2 gets an interesting 30hz
            frameCount = 0;
            sv.quickSendAll(new MultiplayerTools.ServerPlayerPositions(world.players));
        }
        frameCount++;
    }
}
