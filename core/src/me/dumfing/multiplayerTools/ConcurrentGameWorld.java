package me.dumfing.multiplayerTools;

import java.util.HashMap;

/**
 * A new version of the gameworld that is designed to be used on both the client and serverside for better
 * simulation of how the server's world is working
 */
public class ConcurrentGameWorld {
    HashMap<Integer, PlayerSoldier> players;
    public ConcurrentGameWorld(){

    }
}
