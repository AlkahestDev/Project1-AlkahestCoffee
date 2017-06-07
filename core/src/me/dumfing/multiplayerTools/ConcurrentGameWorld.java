package me.dumfing.multiplayerTools;

import com.badlogic.gdx.Gdx;
import me.dumfing.gdxtools.MathTools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import static me.dumfing.multiplayerTools.MultiplayerTools.GRAVITY;
import static me.dumfing.multiplayerTools.MultiplayerTools.JUMPPOWER;
import static me.dumfing.multiplayerTools.MultiplayerTools.WALKSPEED;
import static me.dumfing.multiplayerTools.PlayerSoldier.ARCHER;
import static me.dumfing.multiplayerTools.PlayerSoldier.KNIGHT;

/**
 * A new version of the gameworld that is designed to be used on both the client and serverside for better
 * simulation of how the server's world is working
 */
public class ConcurrentGameWorld {
    private HashMap<Integer, PlayerSoldier> players;
    private WorldMap map;
    private LinkedList<Projectile> projectiles = new LinkedList<Projectile>();
    public ConcurrentGameWorld(HashMap<Integer, PlayerSoldier> initialPlayers){
        this.players = initialPlayers;
    }
    public void setWorld(WorldMap wmIn){
        this.map = wmIn;
    }
    public void update(){
        //System.out.println(projectiles);
        for(PlayerSoldier p : players.values()){
            p.setAnimationID(handleKeyInput(p));
            p.update(Gdx.graphics.getDeltaTime());
            detectCollisions(p);
            handleCollisions(p);
            p.move();
        }
        for(Projectile proj : projectiles){
            proj.checkCollisions(new LinkedList<PlayerSoldier>(players.values()),map);
        }
        for(int i = projectiles.size()-1;i>-1;i--){
            if(projectiles.get(i).getTimeAlive() >= Projectile.MAXLIFETIME){
                projectiles.remove(i);
            }
        }
    }

    public HashMap<Integer, PlayerSoldier> getPlayers() {
        return players;
    }

    public LinkedList<Projectile> getProjectiles() {
        return projectiles;
    }

    public WorldMap getMap() {
        return map;
    }



    private void detectCollisions(PlayerSoldier playerSoldier){

        //  playerSoldier.setvY(playerSoldier.getvY()+GRAVITY);  // Making the player fall down

        // playerSoldier.setY(playerSoldier.getY()+GRAVITY);


        /*
                 0
            -----------
            |         |
          2 |         | 3      <-- COLLISION DIRECTIONS (temporary comment)
            |         |
            -----------
                 1

         */

        Arrays.fill(playerSoldier.collisions,false);

        // Colliding Top [0]
        if ((map.getPosId(Math.round(playerSoldier.getX()), (int)(playerSoldier.getY() + playerSoldier.getHeight())) == 1)){
            playerSoldier.setY(Math.round(playerSoldier.getY()));
            playerSoldier.collisions[0] = true;
        }

        // Colliding Bottom [1]
        if ((map.getPosId(Math.round(playerSoldier.getX()), Math.round(playerSoldier.getY())) == 1)){
            playerSoldier.setY(Math.round(playerSoldier.getY()));
            playerSoldier.collisions[1] = true;
        }
        // Colliding Left [2]
        if ((map.getPosId((int)(playerSoldier.getX()), (int)(playerSoldier.getY() + 1)) == 1)){
            //playerSoldier.setX((int)playerSoldier.getX());
            playerSoldier.setX(Math.round(playerSoldier.getX()));
            playerSoldier.collisions[2] = true;
        }

        // Colliding Right [3]
        if ((map.getPosId((int)(playerSoldier.getX() + 1), (int)(playerSoldier.getY() + 1)) == 1)){
            //playerSoldier.setX((int)playerSoldier.getX());
            playerSoldier.setX(Math.round(playerSoldier.getX()));
            playerSoldier.collisions[3] = true;
        }


    }
    private void handleCollisions(PlayerSoldier playerSoldier) {

        // Vertical collisions:

        // If the player is not on the floor, gravity will act upon it.
        if (!playerSoldier.collisions[1]) {
            playerSoldier.setvY(playerSoldier.getvY() + GRAVITY);
        }

        // If the player collides with the ground or a ceiling, velocity is lost.
        if (playerSoldier.collisions[0]) {
            playerSoldier.setY((playerSoldier.getY() - 1));
            playerSoldier.setvY(0);
        }
        if (playerSoldier.collisions[1]) {
            // playerSoldier.setY(Math.round(playerSoldier.getY()));
            playerSoldier.setvY(0);
        }

    }

    public void updatePlayerKeys(Integer cID, MultiplayerTools.ClientControlObject[] keys){
        players.get(cID).setKeysHeld(keys);
    }
    public void updatePlayers(HashMap<Integer, PlayerSoldier> newInfo){
        this.players = newInfo;
    }

    public void updateProjectiles(LinkedList<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    public int handleKeyInput(PlayerSoldier pIn){

        MultiplayerTools.ClientControlObject[] keys = pIn.getKeysHeld();
        int animation = 0;

        // Jumping
        if(keyDown(keys,MultiplayerTools.Keys.W) || keyDown(keys,MultiplayerTools.Keys.SPACE)){

            // The player is only able to jump when colliding with the ground, and when there's no top collision
            if(pIn.collisions[1] && !pIn.collisions[0]){
                animation+=PlayerAnimations.JUMP;

                pIn.setvY(JUMPPOWER);
                pIn.setY(pIn.getY() + pIn.getvY());  // Boost Up

            }

        }
        // Falling
        if(!pIn.collisions[0] && !pIn.collisions[1] && pIn.getvY() < 0){
            animation+=PlayerAnimations.FALL;
        }

        // Crouching / speed falling
        if(keyDown(keys,MultiplayerTools.Keys.S)){
            // ToDo: Crouching and Falling Animation

            // Speed Fall:
            // If the player is in the air and going up, crouching can make the user immediately fall (Experimental)
            if (pIn.getvY() > 0){
                pIn.setvY(0);
                pIn.setY((pIn.getY() - 1));
            }

        }

        // Going Left
        if(keyDown(keys,MultiplayerTools.Keys.A)){

            pIn.setFacingDirection(0);  // Updating direction

            if(pIn.collisions[1]) {

                animation += PlayerAnimations.WALK;

                // If there is no collision to the left, the player moves left
                if (!pIn.collisions[2] && !pIn.collisions[0]){
                    pIn.setX(pIn.getX() - WALKSPEED);
                }

            }
            else{
                // In the air, the player moves at half the speed
                if (!pIn.collisions[2] && !pIn.collisions[0]){
                    pIn.setX(pIn.getX() - (WALKSPEED));  // /2f
                }

                // broom:small issue with speed boost under tight staircases. Can be ignored.

            }

        }

        // Going Right
        else if(keyDown(keys,MultiplayerTools.Keys.D)){

            //BROOM: Should these ifs be a method?

            pIn.setFacingDirection(1);  // Updating direction

            if(pIn.collisions[1]) {

                animation += PlayerAnimations.WALK;

                // If there is no collision to the right, the player moves right
                if (!pIn.collisions[3] && !pIn.collisions[0]){
                    pIn.setX(pIn.getX() + WALKSPEED);
                }

            }
            else{
                // In the air, the player moves at half the speed
                if (!pIn.collisions[3] && !pIn.collisions[0]){
                    pIn.setX(pIn.getX() + (WALKSPEED));
                }

            }
        }

        // Attacking / Interacting
        else if(keyDown(keys,MultiplayerTools.Keys.LMB)){
            switch (pIn.getCurrentClass()){
                case KNIGHT:
                    // ToDo:Attacking
                    break;
                case ARCHER:
                    //System.out.println("add projectile");
                    if(projectiles.size()<20) {
                        projectiles.add(new Projectile(pIn.getX(), pIn.getY(), 2f, pIn.getMouseAngle(), 0, pIn.getTeam()));
                    }
                    break;
            }
        }
        if(animation+pIn.getFacingDirection()!=pIn.getAnimationID()){
            pIn.setAnimationTime(0);
        }
        return animation;
    }
    public void setPlayerPos(int playerID, float posX, float posY){
        players.get(playerID).setPos(posX,posY);
    }
    private boolean keyDown(MultiplayerTools.ClientControlObject[] keys, int key){
        return keys[key]!=null && keys[key].type == 1 && keys[key].isDown;
    }
}
