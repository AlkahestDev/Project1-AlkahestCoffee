package me.dumfing.multiplayerTools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MathTools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import static me.dumfing.multiplayerTools.MultiplayerTools.GRAVITY;
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
    private Array<CaptureFlag> flags = new Array<CaptureFlag>();
    public ConcurrentGameWorld(HashMap<Integer, PlayerSoldier> initialPlayers){
        this.players = initialPlayers;
    }
    public void setWorld(WorldMap wmIn){
        this.map = wmIn;
        flags.add(new CaptureFlag(this.map.getRedFlag(),0));
        flags.add(new CaptureFlag(this.map.getBluFlag(),1));
    }

    public Array<CaptureFlag> getFlags() {
        return flags;
    }

    public void update(){
        //System.out.println(projectiles);
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
            flag.update(deltaTime,players);

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
        //System.out.println(map.getPosId(Math.round(playerSoldier.getX()), (int)(playerSoldier.getY() + 2)));
        if ((map.getPosId(Math.round(playerSoldier.getX()), (int)(playerSoldier.getY() + 2))>>8 == 1)){
            playerSoldier.setY(Math.round(playerSoldier.getY()));
            playerSoldier.collisions[0] = true;
        }

        // Colliding Bottom [1]
        if ((map.getPosId(Math.round(playerSoldier.getX()), Math.round(playerSoldier.getY()))>>8 == 1)){
            playerSoldier.setY(Math.round(playerSoldier.getY()));
            playerSoldier.collisions[1] = true;
        }
        // Colliding Left [3]
        if ((map.getPosId((int)(playerSoldier.getX()), (int)(playerSoldier.getY() + 1))>>8 == 1)){
            playerSoldier.setX((int)playerSoldier.getX()+1);
            playerSoldier.collisions[3] = true;
        }

        // Colliding Right [2]
        if ((map.getPosId((int)(playerSoldier.getX()+1), (int)(playerSoldier.getY() + 1))>>8 == 1)){
            playerSoldier.setX((int)playerSoldier.getX());
            playerSoldier.collisions[2] = true;
        }


    }
    private void handleCollisions(PlayerSoldier playerSoldier){

        if(playerSoldier.collisions[0]){ //top
            playerSoldier.setvY(Math.min(0,playerSoldier.getvY()));
        }

        if(playerSoldier.collisions[1]){ //bottom
            //System.out.println("hit bottom");
            playerSoldier.setvY(Math.max(0,playerSoldier.getvY()));
            playerSoldier.setvX(MathTools.towardsZero(playerSoldier.getvX(), 0.1f));
        }

        if(playerSoldier.collisions[2]){ //right
            playerSoldier.setvX(Math.min(0,playerSoldier.getvX()));
        }

        if(playerSoldier.collisions[3]){ //left
            playerSoldier.setvX(Math.max(0,playerSoldier.getvX()));
        }

    }

    public void handleAttacks(PlayerSoldier playerSoldier){

        // Checking if player collides with any other player
        for (PlayerSoldier p : players.values()){
            if (p != playerSoldier){
                if (playerSoldier.getFrameIndex() == AnimationManager.ATTACKFRAME && playerSoldier.getRect().overlaps(p.getRect())){  // Maybe its  2 frame where the damage may be done?

                    // Attacking Left
                    if (p.getX() < playerSoldier.getX() && playerSoldier.getFacingDirection() == 0){
                        //doDamage(playerSoldier, p);
                        playerSoldier.attack(p);
                    }

                    // Attacking Right
                    if (p.getX() > playerSoldier.getX() && playerSoldier.getFacingDirection() == 1) {
                        playerSoldier.attack(p);
                    }
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
                    //System.out.println("add projectile");
                    if(projectiles.size()<20) {
                        projectiles.add(new Projectile(pIn.getX(), pIn.getY(), 2f, pIn.getMouseAngle(), 0, pIn.getTeam()));
                    }
                    break;
            }
        }

        else if(keyDown(keys,MultiplayerTools.Keys.RMB)){
            // SHITTY TEST FOR KNOCKBACK

            pIn.knockBack();

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
