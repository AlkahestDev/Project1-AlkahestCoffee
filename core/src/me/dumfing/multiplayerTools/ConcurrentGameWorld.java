package me.dumfing.multiplayerTools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
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
    private static final int SWORD = 0;
    private static final int BOW = 1;
    private HashMap<Integer, PlayerSoldier> players;
    private WorldMap worldMap;
    private LinkedList<Projectile> projectiles = new LinkedList<Projectile>();
    private CaptureFlag[] flags = new CaptureFlag[2];
    private int[] score = new int[2];
    private LinkedList<GridPoint2> respawnTimers = new LinkedList<GridPoint2>(); // a linkedlist of GridPoint2's with the x's as id's and y's as time. 0 means they should respawn
    private LinkedList<GridPoint2> hits= new LinkedList<GridPoint2>();  // LinkedList of GridPoint2's with the x as the attacker's id and the y as the defender's
    private Array<Vector3> particleList = new Array<Vector3>(); // places where particles should be played, and what colour
    private LinkedList<GridPoint3> killLog = new LinkedList<GridPoint3>();
    /**
     * Gets the respawn timeers from the world
     * @return A LinkedList of Vector2 respawn timers
     */
    public LinkedList<GridPoint2> getRespawnTimers() {
        return respawnTimers;
    }

    /**
     * Used to update the respawn timers from the MultiplayerClient
     * @param respawnTimers The most up to date LinkedList of respawn timers from the MultiplayerClient
     */
    public void setRespawnTimers(LinkedList<GridPoint2> respawnTimers) {
        this.respawnTimers = respawnTimers;
    }

    public ConcurrentGameWorld(HashMap<Integer, PlayerSoldier> initialPlayers){
        this.players = initialPlayers;
    }

    /**
     * Sets which worldmap is to be used by the Concurrent GameWorld
     * @param wmIn The WorldMap object to use
     */
    public void setWorld(WorldMap wmIn){
        this.worldMap = wmIn;
        flags[0] = new CaptureFlag(this.worldMap.getRedFlag(),0);
        flags[1] = new CaptureFlag(this.worldMap.getBluFlag(),1);
    }

    /**
     * Gets the flags from the MultiplayerClient
     * @return
     */
    public CaptureFlag[] getFlags() {
        return flags;
    }

    /**
     * Updates everything in the ConcurrentGameWorld, players, projectiles, flags, respawn timers.
     */
    public void update(){
        float deltaTime = Gdx.graphics.getDeltaTime();
        checkDeaths(); // check to see if anyone should be killed so you don't have to deal with dead people
        for(PlayerSoldier p : getLivingPlayers().values()){ // iterate through all the players in the list of living players
            p.setAnimationID(handleKeyInput(p)); // figure out their animation from the keys they are pressing, this also sets their velocities
            p.update(deltaTime); // update the player for any time based actions they'll need to do
            detectCollisions(p); // see if the player is hitting anywhere in the world
            handleCollisions(p); // stop the player from moving if they're hitting anything in the world
            p.move(); // move the player by their vX and vY
            if(p.getMouseAngle()> 90 && p.getMouseAngle()<270){ // if the mouse relative to the player is on the left side relative to the player
                p.setFacingDirection(0); // make the player face left
            }
            else { // if the mouse is on the right side
                p.setFacingDirection(1); // make player face right
            }
        }
        for(Projectile proj : projectiles){ // iterate through the list of projectiles
            proj.checkCollisions(getLivingPlayers(), worldMap); // check if the projectile is colliding with anything
            if(proj.killedPlayer){
                GridPoint2 killPair = proj.getAttackPair();
                logKill(killPair.x,killPair.y,BOW);
                System.out.println(players.get(killPair.x).getName()+" killed "+players.get(killPair.y).getName()+" with an arrow");
            }
        }
        for(int i = projectiles.size()-1;i>-1;i--){ // loop in reverse to prepare to remove projectiles from the list
            if(projectiles.get(i).getTimeAlive() >= Projectile.MAXLIFETIME){ // if the projectile has existed for longer than the designated max lifetime
                projectiles.remove(i); // remove the projectile from the array if it's lasted too long
            }
        }
        for(CaptureFlag flag : flags){ // iterate through all the flags
            flag.update(deltaTime,getLivingPlayers(), worldMap,score); // update the flag, checking if it's supposed to move with a player or hit the ground
            if(worldMap.getPosId(Math.round(flag.getxPos()), Math.round(flag.getyPos()+1))==(flag.getTeamID()==0?0x0003FFFF:0xFF0300FF)){ // if the flag is on the red team, this checks that it's in the blue area and vice versa
                flag.setPhysicsParent(-1); // reset physics parent to nothing
                score[1-flag.getTeamID()]+=1; // increase team that is opposite of flag's score
                flag.setScored(true);
                particleList.add(new Vector3(flag.getxPos(),flag.getyPos(),1-flag.getTeamID()));
                flag.resetPos(worldMap);
            }
        }
        for(GridPoint2 timePair : respawnTimers){
            if(timePair.y<=0){ // if the respawn time is over
                if(players.get(timePair.x)!=null) {
                    players.get(timePair.x).setAlive(true); // set the respective player alive
                }
                respawnTimers.remove(timePair); // remove the time pair
            }
            else{
                timePair.add(0,-1); // subtract 1/60th of a second from the time
            }
        }
    }

    public HashMap<Integer, PlayerSoldier> getPlayers() {
        return players;
    }

    public LinkedList<Projectile> getProjectiles() {
        return projectiles;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    private void detectCollisions(PlayerSoldier playerSoldier){
        Arrays.fill(playerSoldier.collisions,false);
        playerSoldier.setvY(playerSoldier.getvY()+GRAVITY);  // Making the player fall down
        // Colliding Top [0]
        if ((worldMap.getPosId(Math.round(playerSoldier.getX()), (int)(playerSoldier.getY() + 2))>>8 == 1)||(worldMap.getPosId(Math.round(playerSoldier.getX()), (int)(playerSoldier.getY() + 2)) == (playerSoldier.getTeam()==1?0xFFFF00FF:0x00FFFFFF))){
            playerSoldier.setY(Math.round(playerSoldier.getY()));
            playerSoldier.collisions[0] = true;
        }

        // Colliding Bottom [1]
        if ((worldMap.getPosId(Math.round(playerSoldier.getX()), Math.round(playerSoldier.getY()))>>8 == 1)||(worldMap.getPosId(Math.round(playerSoldier.getX()), Math.round(playerSoldier.getY()))==(playerSoldier.getTeam()==1?0xFFFF00FF:0x00FFFFFF))){
            playerSoldier.setY(Math.round(playerSoldier.getY()));
            playerSoldier.collisions[1] = true;
        }
        // Colliding Left [3]
        if ((worldMap.getPosId((int)(playerSoldier.getX()), (int)(playerSoldier.getY() + 1))>>8 == 1)||(worldMap.getPosId((int)(playerSoldier.getX()), (int)(playerSoldier.getY() + 1))==(playerSoldier.getTeam()==1?0xFFFF00FF:0x00FFFFFF))){
            playerSoldier.setX((int)playerSoldier.getX()+1);
            playerSoldier.collisions[3] = true;
        }

        // Colliding Right [2]
        if ((worldMap.getPosId((int)(playerSoldier.getX()+1), (int)(playerSoldier.getY() + 1))>>8 == 1)||(worldMap.getPosId((int)(playerSoldier.getX()+1), (int)(playerSoldier.getY() + 1))==(playerSoldier.getTeam()==1?0xFFFF00FF:0x00FFFFFF))){
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
        Rectangle attackRect = new Rectangle(attacker.getX()+(attacker.getFacingDirection()==0?-0.8f:1),attacker.getY(),0.9f,2f); // TODO: tweak this to line up with the animations better
        for (Integer k: players.keySet()){
            PlayerSoldier target = players.get(k);
            if (target != attacker){
                if (attacker.getFrameIndex() == AnimationManager.ATTACKFRAME && attackRect.overlaps(target.getRect()) && target.getHitCooldown()==0){  // Maybe its  2 frame where the damage may be done?
                    System.out.println(attacker.getName()+" attacked "+target.getName());
                    int attackerID = 0;
                    int victimID = k;
                    attacker.attack(target); // always will attack because the attack rectangle is left or right of the player already
                    for(Integer v : players.keySet()){
                        if(players.get(v) == attacker){
                            hits.add(new GridPoint2(v,k));
                            attackerID = v;
                        }
                    }
                    if(target.getHealth() <= 0){
                        logKill(attackerID,victimID,SWORD);
                        System.out.println(attacker.getName()+" killed "+target.getName()+" with a sword");
                    }
                    return;
                }
            }
        }

    }

    public void updatePlayerKeys(Integer cID, MultiplayerTools.ClientControlObject[] keys){
        players.get(cID).setKeysHeld(keys);
    }
    public void updateRespawnTimes(LinkedList<GridPoint2> times){
        this.respawnTimers = times;
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
            handleAttacks(pIn);
            // if (pIn.getAnimationSet()[pIn.getAnimationID() & AnimationManager.DIRECTION][0][pIn.getCurrentClass()].isAnimationFinished(pIn.getAnimationTime())){
            //     pIn.swinging = false;
            // }

            if (pIn.isAnimationDone()){
                pIn.swinging = false;
            }


        }

        if (pIn.isDrawingShield()){

            // Walking
            if (keyDown(keys, Keys.A) || keyDown(keys, Keys.S)){
                animation += AnimationManager.SHIELD_DRAW_WALKING;
            }
            // Idle
            else {
                animation += AnimationManager.SHIELD_DRAW_IDLE;
            }

            if (pIn.isAnimationDone()){
                pIn.setDrawingShield(false);
                pIn.setShieldUp(true);
                
            }

        }


        if(keyDown(keys,MultiplayerTools.Keys.W) || keyDown(keys,MultiplayerTools.Keys.SPACE)){
            if(pIn.collisions[1]){  // canJump can be replaced by pIn.collisions[1]
                pIn.setvY(MultiplayerTools.JUMPPOWER);
                pIn.setCanJump(false);
            }
            pIn.setSwinging(false);
        }
        else if(!pIn.isCanJump() && pIn.getvX()>0){
            animation+= AnimationManager.JUMP;
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
            pIn.setMouseAngle(MathUtils.clamp(pIn.getMouseAngle(),91,269));

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
            if(pIn.getMouseAngle()>0 && pIn.getMouseAngle()<90) {
                pIn.setMouseAngle(MathUtils.clamp(pIn.getMouseAngle(),0,90));
            }
            else {
                pIn.setMouseAngle(MathUtils.clamp(pIn.getMouseAngle(),270,360));
            }
        }

        // Shielding
        if (keyDown(keys, MultiplayerTools.Keys.CONTROL)){
            // Walking
            if (keyDown(keys, MultiplayerTools.Keys.A) || keyDown(keys, MultiplayerTools.Keys.D)){
                if(pIn.getCurrentClass()==KNIGHT) {
                    // Shield is already drawn
                    if (pIn.isShieldUp()) {
                        animation += AnimationManager.SHIELD_WALKING;
                    }
                    // Drawing Up shield
                    else {
                        animation += AnimationManager.SHIELD_DRAW_WALKING;
                    }
                    pIn.setDrawingShield(true);
                }


            }
            // Idle
            else {
                if(pIn.getCurrentClass()==KNIGHT) {
                    // Shield is already drawn
                    if (pIn.isShieldUp()) {
                        animation += AnimationManager.SHIELD_IDLE;
                    }
                    // Drawing Up shield
                    else if ((animation & 256) != 256) {
                        animation += AnimationManager.SHIELD_DRAW_IDLE;
                    }
                    pIn.setDrawingShield(true);
                }

            }

        }
        else{
            pIn.setShieldUp(false);
            pIn.setDrawingShield(false);
        }

        if(keyDown(keys,MultiplayerTools.Keys.LMB)){

            switch (pIn.getCurrentClass()){
                case KNIGHT:

                    // On the ground, not moving
                    if (pIn.collisions[1] && !keyDown(keys,MultiplayerTools.Keys.D) && !keyDown(keys,MultiplayerTools.Keys.A)){
                        pIn.swinging = true;
                        //animation+=AnimationManager.ATTACK;
                        //handleAttacks(pIn);

                    }
                    else if(pIn.collisions[1] &&(keyDown(keys,MultiplayerTools.Keys.D) || keyDown(keys,MultiplayerTools.Keys.A))){
                        pIn.swinging = true;
                        animation+=AnimationManager.ATTACK;
                        //handleAttacks(pIn);
                    }

                    break;

                case ARCHER:
                    if(projectiles.size()<20) {
                        pIn.setBowDrawTime(pIn.getBowDrawTime()+1);
                        pIn.setDrawingBow(true);
                        animation+=AnimationManager.ATTACK;
                    }
                    break;
            }
        }
        if(!keyDown(keys, Keys.LMB)) {
            if (pIn.isDrawingBow() && pIn.getBowDrawTime() > 20) {
                for(Integer v : players.keySet()){
                    if(players.get(v) == pIn){
                        projectiles.add(new Projectile(pIn.getX() + pIn.getWidth() / 2f, pIn.getY() + pIn.getHeight() / 2f, Math.min(2, (float) pIn.getBowDrawTime() / 45f), pIn.getMouseAngle(), 0, pIn.getTeam(),v));
                    }
                }
            }
            pIn.setDrawingBow(false);
            pIn.setBowDrawTime(0);
        }
        else if(keyDown(keys,MultiplayerTools.Keys.RMB)){
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
        for(Integer k : players.keySet()){
            if(players.get(k).getY()<-30){
                //kill them
                killPlayer(k);
            }
            else if(players.get(k).getHealth() <=0){
                killPlayer(k);
            }
        }
    }
    private HashMap<Integer,PlayerSoldier> getLivingPlayers(){
        HashMap<Integer,PlayerSoldier> playersOut= new HashMap<Integer, PlayerSoldier>();
        for(Integer k : players.keySet()){
            if(players.get(k).isAlive()){
                playersOut.put(k,players.get(k));
            }
        }
        return playersOut;
    }
    private void killPlayer(Integer k){
        GridPoint2 spawnPos = players.get(k).getTeam() == 0? worldMap.getRedSpawn(): worldMap.getBluSpawn();
        players.get(k).setPos(spawnPos);
        players.get(k).setAlive(false);
        players.get(k).reset();
        respawnTimers.add(new GridPoint2(k,180));
    }

    public LinkedList<GridPoint2> getHits() {
        return hits;
    }
    public void clearHits(){
        hits.clear();
    }
    public Array<Vector3> getParticles(){
        Array<Vector3> aOut = new Array<Vector3>(this.particleList);
        this.particleList.clear();
        return aOut;
    }

    /**
     * Adds a kill to the kill log
     * @param killer the connection id of the person who killed
     * @param victim the connection id of the person who was killed
     * @param weapon the weapon that was used
     */
    public void logKill(int killer, int victim, int weapon){
        players.get(killer).addKill();
        players.get(victim).addDeath();
        killLog.add(new GridPoint3(killer,victim,weapon));
        if(killLog.size()>10){
            killLog.remove();
        }
    }

    /**
     * gets the kill log
     * @return
     */
    public LinkedList<GridPoint3> getKillLog() {
        return killLog;
    }
}
