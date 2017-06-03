package me.dumfing.multiplayerTools;

import com.badlogic.gdx.Gdx;
import me.dumfing.gdxtools.MathTools;

import java.util.HashMap;
import java.util.LinkedList;

import static me.dumfing.multiplayerTools.MultiplayerTools.WALKSPEED;

/**
 * A new version of the gameworld that is designed to be used on both the client and serverside for better
 * simulation of how the server's world is working
 */
public class ConcurrentGameWorld {
    HashMap<Integer, PlayerSoldier> players;
    WorldMap map;
    LinkedList<Projectile> projectiles = new LinkedList<Projectile>();
    public ConcurrentGameWorld(HashMap<Integer, PlayerSoldier> initialPlayers){
        this.players = initialPlayers;
    }
    public void setWorld(WorldMap wmIn){
        this.map = wmIn;
    }
    public void update(){
        for(PlayerSoldier p : players.values()){
            p.update(Gdx.graphics.getDeltaTime());
            p.setAnimationID(handleKeyInput(p));
            handleCollisions(p);
            p.move();
        }
    }

    public HashMap<Integer, PlayerSoldier> getPlayers() {
        return players;
    }
    public WorldMap getMap() {
        return map;
    }

    public void handleCollisions(PlayerSoldier playerSoldier){

    }

    // private void handleCollisions(PlayerSoldier playerSoldier){
    //     if(playerSoldier.isCanJump()) {
    //         playerSoldier.setvX(MathTools.towardsZero(playerSoldier.getvX(), 0.1f));
    //     }
    //     else { // air friction
    //         playerSoldier.setvX(MathTools.towardsZero(playerSoldier.getvX(), 0.001f));
    //     }
    //     //System.out.println(map.getPosId((int)(playerSoldier.getX()),(int)(playerSoldier.getY()+playerSoldier.getvY())));
    //     if(map.getPosId((int)(playerSoldier.getX()), Math.round(playerSoldier.getY()+playerSoldier.getvY()))==1){
    //         //System.out.printf("%d %d %f\n",(int)(playerSoldier.getX()),(int)(playerSoldier.getY()+playerSoldier.getvY()), playerSoldier.getvY());
    //         playerSoldier.setvY(0);
    //         playerSoldier.setY((int)playerSoldier.getY()+0.001f);
    //         playerSoldier.setCanJump(true);
    //     }
    //     else{
    //         playerSoldier.setvY(playerSoldier.getvY()+MultiplayerTools.GRAVITY);
    //     }
    //     //TODO: vertical Collisions
    //    if((map.getPosId((int)(playerSoldier.getX()+1),(int)(playerSoldier.getY()+1))==1)){ //right side
    //         System.out.println("hitX");
    //         playerSoldier.setX(Math.round(playerSoldier.getX()));
    //         playerSoldier.setvX(Math.min(playerSoldier.getvX(),0));
    //     }
    //     if((map.getPosId((int)(playerSoldier.getX()),(int)(playerSoldier.getY()+1))==1)){ // left side
    //         System.out.println("hitXL");
    //         playerSoldier.setX(Math.round(playerSoldier.getX())-1);
    //         playerSoldier.setvX(Math.max(playerSoldier.getvX(),0));
    //     }
    // }
    public void updatePlayerKeys(Integer cID, MultiplayerTools.ClientControlObject[] keys){
        players.get(cID).setKeysHeld(keys);
    }
    public void updatePlayers(HashMap<Integer, PlayerSoldier> newInfo){
        this.players = newInfo;
    }



    public int handleKeyInput(PlayerSoldier pIn){
        MultiplayerTools.ClientControlObject[] keys = pIn.getKeysHeld();
        int animation = 0;
        if(keys[MultiplayerTools.Keys.W] != null && keys[MultiplayerTools.Keys.W].type==1 && keys[MultiplayerTools.Keys.W].isDown){
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
        if(keys[MultiplayerTools.Keys.S] !=null && keys[MultiplayerTools.Keys.S].type==1 && keys[MultiplayerTools.Keys.S].isDown){

        }
        if(keys[MultiplayerTools.Keys.A] !=null && keys[MultiplayerTools.Keys.A].type==1 && keys[MultiplayerTools.Keys.A].isDown){
            if(pIn.isCanJump()) {
                pIn.setvX(-WALKSPEED);
                animation += PlayerAnimations.WALK;
            }
            else{
                pIn.setvX(-WALKSPEED/2f);
            }
            pIn.setFacingDirection(0);
        }
        else if(keys[MultiplayerTools.Keys.D]!=null && keys[MultiplayerTools.Keys.D].type==1 && keys[MultiplayerTools.Keys.D].isDown){
            if(pIn.isCanJump()) {
                pIn.setvX(WALKSPEED);
                animation += PlayerAnimations.WALK;
            }
            else{
                pIn.setvX(WALKSPEED/2f);
            }
            pIn.setFacingDirection(1);
        }
        else if(keys[MultiplayerTools.Keys.LMB] !=null && keys[MultiplayerTools.Keys.LMB].type==0 && keys[MultiplayerTools.Keys.LMB].isDown){

        }
        if(animation+pIn.getFacingDirection()!=pIn.getAnimationID()){
            pIn.setAnimationTime(0);
        }
        return animation;
    }
}
