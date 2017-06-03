package me.dumfing.multiplayerTools;

import com.badlogic.gdx.Gdx;
import me.dumfing.gdxtools.MathTools;

import java.util.HashMap;
import java.util.LinkedList;

import static me.dumfing.multiplayerTools.MultiplayerTools.WALKSPEED;
import static me.dumfing.multiplayerTools.PlayerSoldier.ARCHER;
import static me.dumfing.multiplayerTools.PlayerSoldier.KNIGHT;

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
        //System.out.println(projectiles);
        for(PlayerSoldier p : players.values()){
            p.update(Gdx.graphics.getDeltaTime());
            p.setAnimationID(handleKeyInput(p));
            handleCollisions(p);
            p.move();
        }
        for(Projectile proj : projectiles){
            proj.checkCollisions(new LinkedList<PlayerSoldier>(players.values()),map);
        }
        for(int i = projectiles.size()-1;i>-1;i--){
            if(projectiles.get(i).getTimeAlive() >= 240){
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

    public void updateProjectiles(LinkedList<Projectile> projectiles) {
        this.projectiles = projectiles;
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
        else if(keys[MultiplayerTools.Keys.LMB] !=null && keys[MultiplayerTools.Keys.LMB].type==1 && keys[MultiplayerTools.Keys.LMB].isDown){
            switch (pIn.getCurrentClass()){
                case KNIGHT:

                    break;
                case ARCHER:
                    //System.out.println("add projectile");
                    if(projectiles.size()<20) {
                        projectiles.add(new Projectile(pIn.getX(), pIn.getY(), 1.5f, pIn.getMouseAngle(), 0, pIn.getTeam()));
                    }
                    break;
            }
        }
        if(animation+pIn.getFacingDirection()!=pIn.getAnimationID()){
            pIn.setAnimationTime(0);
        }
        return animation;
    }
}
