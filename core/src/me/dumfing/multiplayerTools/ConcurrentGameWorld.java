package me.dumfing.multiplayerTools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import me.dumfing.gdxtools.MathTools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import static me.dumfing.multiplayerTools.MultiplayerTools.*;
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
    private CaptureFlag[] flags = new CaptureFlag[2];
    private int[] score = new int[2];
    public ConcurrentGameWorld(HashMap<Integer, PlayerSoldier> initialPlayers){
        this.players = initialPlayers;
    }
    public void setWorld(WorldMap wmIn){
        this.map = wmIn;
        flags[0] = new CaptureFlag(this.map.getRedFlag(),0);
        flags[1] = new CaptureFlag(this.map.getBluFlag(),1);
    }

    public CaptureFlag[] getFlags() {
        return flags;
    }

    public void update(){
        float deltaTime = Gdx.graphics.getDeltaTime();
        for(PlayerSoldier p : players.values()){
            p.setAnimationID(handleKeyInput(p));
            p.update(deltaTime);
            detectCollisions(p);
            handleCollisions(p);
            handleAttacks(p);
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
        for(CaptureFlag flag : flags){
            flag.update(deltaTime,players,map,score);
            if(map.getPosId(Math.round(flag.getxPos()), Math.round(flag.getyPos()+1))==(flag.getTeamID()==0?0x0003FFFF:0xFF0300FF)){
                flag.setPhysicsParent(-1);
                score[flag.getTeamID()]+=1;
                flag.resetPos(map);
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
        Arrays.fill(playerSoldier.collisions,false);
        playerSoldier.setvY(playerSoldier.getvY()+GRAVITY);  // Making the player fall down
        // Colliding Top [0]
        if ((map.getPosId(Math.round(playerSoldier.getX()), (int)(playerSoldier.getY() + 2))>>8 == 1)||(map.getPosId(Math.round(playerSoldier.getX()), (int)(playerSoldier.getY() + 2)) == (playerSoldier.getTeam()==1?0xFFFF00FF:0x00FFFFFF))){
            playerSoldier.setY(Math.round(playerSoldier.getY()));
            playerSoldier.collisions[0] = true;
        }

        // Colliding Bottom [1]
        if ((map.getPosId(Math.round(playerSoldier.getX()), Math.round(playerSoldier.getY()))>>8 == 1)||(map.getPosId(Math.round(playerSoldier.getX()), Math.round(playerSoldier.getY()))==(playerSoldier.getTeam()==1?0xFFFF00FF:0x00FFFFFF))){
            playerSoldier.setY(Math.round(playerSoldier.getY()));
            playerSoldier.collisions[1] = true;
        }
        // Colliding Left [3]
        if ((map.getPosId((int)(playerSoldier.getX()), (int)(playerSoldier.getY() + 1))>>8 == 1)||(map.getPosId((int)(playerSoldier.getX()), (int)(playerSoldier.getY() + 1))==(playerSoldier.getTeam()==1?0xFFFF00FF:0x00FFFFFF))){
            playerSoldier.setX((int)playerSoldier.getX()+1);
            playerSoldier.collisions[3] = true;
        }

        // Colliding Right [2]
        if ((map.getPosId((int)(playerSoldier.getX()+1), (int)(playerSoldier.getY() + 1))>>8 == 1)||(map.getPosId((int)(playerSoldier.getX()+1), (int)(playerSoldier.getY() + 1))==(playerSoldier.getTeam()==1?0xFFFF00FF:0x00FFFFFF))){
            playerSoldier.setX((int)playerSoldier.getX());
            playerSoldier.collisions[2] = true;
        }


    }
    private void handleCollisions(PlayerSoldier playerSoldier){

        if(playerSoldier.collisions[0]){ //top
            playerSoldier.setvY(Math.min(0,playerSoldier.getvY()));
        }

        if(playerSoldier.collisions[1]){ //bottom
            playerSoldier.setvY(Math.max(0,playerSoldier.getvY()));
            playerSoldier.setvX(MathTools.towardsZero(playerSoldier.getvX(), 0.1f));
        }

        if(playerSoldier.collisions[2]){ //right
            playerSoldier.setvX(Math.min(0,playerSoldier.getvX()));
        }

        if(playerSoldier.collisions[3]){ //left
            playerSoldier.setvX(Math.min(0,playerSoldier.getvX()));
        }

    }
    private void handleAttacks(PlayerSoldier attacker){

        // Checking if player collides with any other player
        Rectangle attackRect = new Rectangle(attacker.getX()+(attacker.getFacingDirection()==0?-0.7f:1),attacker.getY()+0.5f,0.7f,0.5f); // TODO: tweak this to line up with the animations better
        for (PlayerSoldier target : players.values()){
            if (target != attacker){
                if (attacker.getFrameIndex() == AnimationManager.ATTACKFRAME && attackRect.overlaps(target.getRect())){  // Maybe its  2 frame where the damage may be done?
                    // Attacking Left
                    /*if (target.getX() < attacker.getX() && attacker.getFacingDirection() == 0){
                        //doDamage(playerSoldier, p);
                        attacker.attack(target);
                    }

                    // Attacking Right
                    if (target.getX() > attacker.getX() && attacker.getFacingDirection() == 1) {
                        attacker.attack(target);
                    }*/
                    attacker.attack(target); // always will attack because the attack rectangle is left or right of the player already
                }
            }
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


        // Keeping the attack animation running until it's finished
        if (pIn.swinging){

            animation += AnimationManager.ATTACK;

            // if (pIn.getAnimationSet()[pIn.getAnimationID() & AnimationManager.DIRECTION][0][pIn.getCurrentClass()].isAnimationFinished(pIn.getAnimationTime())){
            //     pIn.swinging = false;
            // }

            if (pIn.isAnimationDone()){
                pIn.swinging = false;
            }


        }

        if(keyDown(keys,MultiplayerTools.Keys.W) || keyDown(keys,MultiplayerTools.Keys.SPACE)){
            if(pIn.collisions[1]){  // canJump can be replaced by pIn.collisions[1]
                pIn.setvY(MultiplayerTools.JUMPPOWER);
                pIn.setCanJump(false);
            }
        }
        if(!pIn.isCanJump() && pIn.getvX()<0){ // if player is not on ground and player is descending
            animation+= AnimationManager.FALL;
        }
        else if(!pIn.isCanJump() && pIn.getvX()>0){
            animation+= AnimationManager.JUMP;
        }
        if(keyDown(keys,MultiplayerTools.Keys.S)){
            // ToDo: Crouching Animation
        }
        if(keyDown(keys,MultiplayerTools.Keys.A)){
            if(pIn.collisions[1]) {
                pIn.setvX(-WALKSPEED);
                animation += AnimationManager.WALK;
            }
            else{
                pIn.setvX(-WALKSPEED/2f);
            }
            pIn.setFacingDirection(0);
        }
        else if(keyDown(keys,MultiplayerTools.Keys.D)){
            if(pIn.collisions[1]) {
                pIn.setvX(WALKSPEED);
                animation += AnimationManager.WALK;
            }
            else{
                pIn.setvX(WALKSPEED/2f);
            }
            pIn.setFacingDirection(1);
        }

        else if(keyDown(keys,MultiplayerTools.Keys.LMB)){

            switch (pIn.getCurrentClass()){
                case KNIGHT:

                    // On the ground, not moving
                    if (pIn.collisions[1] && !keyDown(keys,MultiplayerTools.Keys.D) && !keyDown(keys,MultiplayerTools.Keys.A)){
                        pIn.swinging = true;
                        handleAttacks(pIn);

                    }

                    break;

                case ARCHER:
                    if(projectiles.size()<2) {
                        projectiles.add(new Projectile(pIn.getX(), pIn.getY(), 2f, pIn.getMouseAngle(), 0, pIn.getTeam()));
                    }
                    break;
            }
        }

        else if(keyDown(keys,MultiplayerTools.Keys.RMB)){
            // SHITTY TEST FOR KNOCKBACK

            pIn.knockBack(0.3f);

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
    public void updateFlags(CaptureFlag[] flags){
        this.flags = flags;
    }
    public int getRedScore(){
        return score[REDTEAM];
    }
    public int getBluScore(){
        return score[BLUTEAM];
    }
    private void checkDeaths(){// checks if any player should be killed and does something about it
        for(PlayerSoldier player : players.values()){
            if(player.getY()<-10){
                //kill them
            }
        }
    }
}
