package me.dumfing.multiplayerTools;

import com.badlogic.gdx.Gdx;
import me.dumfing.gdxtools.MathTools;

import java.util.HashMap;

import static me.dumfing.multiplayerTools.MultiplayerTools.WALKSPEED;

/**
 * A new version of the gameworld that is designed to be used on both the client and serverside for better
 * simulation of how the server's world is working
 */
public class ConcurrentGameWorld {
    HashMap<Integer, PlayerSoldier> players;
    WorldMap map;
    public ConcurrentGameWorld(HashMap<Integer, PlayerSoldier> initialPlayers){
        this.players = initialPlayers;
    }
    public void setWorld(WorldMap wmIn){
        this.map = wmIn;
    }
    public void update(){
        for(PlayerSoldier p : players.values()){
            p.update(Gdx.graphics.getDeltaTime());
            //System.out.print(p.getvY()+" ");
            p.setAnimationID(handleKeyInput(p));
            //System.out.print(p.getvY()+" ");
            handleCollisions(p);
            p.move();
            //System.out.println(p.getvY());
            //System.out.println(p);
        }
    }

    public HashMap<Integer, PlayerSoldier> getPlayers() {
        return players;
    }
    public WorldMap getMap() {
        return map;
    }
    public void handleCollisions(PlayerSoldier playerSoldier){
        if(playerSoldier.isCanJump()) {
            playerSoldier.setvX(MathTools.towardsZero(playerSoldier.getvX(), 0.1f));
        }
        else { // air friction
            playerSoldier.setvX(MathTools.towardsZero(playerSoldier.getvX(), 0.001f));
        }
        //System.out.println(map.getPosId((int)(playerSoldier.getX()),(int)(playerSoldier.getY()+playerSoldier.getvY())));
        if(map.getPosId((int)(playerSoldier.getX()), Math.round(playerSoldier.getY()+playerSoldier.getvY()))==1){
            //System.out.printf("%d %d %f\n",(int)(playerSoldier.getX()),(int)(playerSoldier.getY()+playerSoldier.getvY()), playerSoldier.getvY());
            playerSoldier.setvY(0);
            playerSoldier.setY((int)playerSoldier.getY()+0.001f);
            playerSoldier.setCanJump(true);
        }
        else{
            playerSoldier.setvY(playerSoldier.getvY()+MultiplayerTools.GRAVITY);
        }
        //TODO: vertical Collisions
       if((map.getPosId((int)(playerSoldier.getX()+1),(int)(playerSoldier.getY()+1))==1)){ //right side
            System.out.println("hitX");
            playerSoldier.setX(Math.round(playerSoldier.getX()));
            playerSoldier.setvX(Math.min(playerSoldier.getvX(),0));
        }
        if((map.getPosId((int)(playerSoldier.getX()),(int)(playerSoldier.getY()+1))==1)){ // left side
            System.out.println("hitXL");
            playerSoldier.setX(Math.round(playerSoldier.getX())-1);
            playerSoldier.setvX(Math.max(playerSoldier.getvX(),0));
        }
    }
    public void updatePlayerKeys(Integer cID, boolean[] keys){
        players.get(cID).setKeysHeld(keys);
    }
    public void updatePlayers(HashMap<Integer, PlayerSoldier> newInfo){
        this.players = newInfo;
    }
    public int handleKeyInput(PlayerSoldier pIn){
        boolean[] keys = pIn.getKeysHeld();
        int animation = 0;
        if(keys[MultiplayerTools.Keys.W]){
            if(pIn.isCanJump()) {
                pIn.setvY(MultiplayerTools.JUMPPOWER);
                pIn.setCanJump(false);
            }
        }
        if(!pIn.isCanJump() && pIn.getvX()<0){ // if player is not on ground and player is descending
            animation+=PlayerAnimations.FALL;
        }
        else if(!pIn.isCanJump() && pIn.getvX()>0){
            animation+=PlayerAnimations.JUMP;
        }
        if(keys[MultiplayerTools.Keys.S]){

        }
        if(keys[MultiplayerTools.Keys.A]){
            if(pIn.isCanJump()) {
                pIn.setvX(-WALKSPEED);
                animation += PlayerAnimations.WALK;
            }
            else{
                pIn.setvX(-WALKSPEED/2f);
            }
            pIn.setFacingDirection(0);
        }
        else if(keys[MultiplayerTools.Keys.D]){
            if(pIn.isCanJump()) {
                pIn.setvX(WALKSPEED);
                animation += PlayerAnimations.WALK;
            }
            else{
                pIn.setvX(WALKSPEED/2f);
            }
            pIn.setFacingDirection(1);
        }
        if(animation+pIn.getFacingDirection()!=pIn.getAnimationID()){
            pIn.setAnimationTime(0);
        }
        return animation;
    }
}
