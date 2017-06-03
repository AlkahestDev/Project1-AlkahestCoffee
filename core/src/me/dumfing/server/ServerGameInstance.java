package me.dumfing.server;

import me.dumfing.multiplayerTools.ConcurrentGameWorld;
import me.dumfing.multiplayerTools.MultiplayerTools;
import me.dumfing.multiplayerTools.PlayerSoldier;
import me.dumfing.multiplayerTools.WorldMap;

import java.util.HashMap;

/**
 * A class that manages the entire gameplay portion of the server
 */
public class ServerGameInstance {
    int frameCount = 0;
    ConcurrentGameWorld world;
    public ServerGameInstance(HashMap<Integer, PlayerSoldier> players){
        world = new ConcurrentGameWorld(players);
    }
    public void update(MainServer sv){
        world.update();
        if(frameCount == 3){ // 2 gets an interesting 30hz
            frameCount = 0;
            System.out.println(world.getProjectiles().size());
            sv.quickSendAll(new MultiplayerTools.ServerPlayerPositions(world.getPlayers()));
            sv.quickSendAll(new MultiplayerTools.ServerProjectilePositions(world.getProjectiles()));
        }

        frameCount++;
    }
    public void setWorldMap(WorldMap map){
        world.setWorld(map);
    }
}
