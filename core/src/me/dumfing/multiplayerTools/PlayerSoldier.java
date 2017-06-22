package me.dumfing.multiplayerTools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import me.dumfing.gdxtools.MathTools;

public class PlayerSoldier {
    //Arrow drawing will work by having a counter that increases as the mouse is down. If drawingBow is true and the mouse is up the arrow will be fired, and the counter will be set to 0
    private int health, maxHealth, animationID, team, pickedClass,facingDirection, bowDrawTime, kills, deaths; // animationID is an int describing the direction the player is facing and what animation they're doing
    private boolean canJump, drawingBow;

    private MultiplayerTools.ClientControlObject[] keysHeld = new MultiplayerTools.ClientControlObject[10];

    private Rectangle playerArea;

    private float vX, vY, animationTime,hitCooldown; // hitCooldown is the amount of time you can be hit again in seconds

    private String name = "[#FF0000]N[#DD1100]O[#BB3300]_[#995500]N[#777700]A[#559900]M[#33BB00]E[#11DD00]!";

    public static final float width = 1;
    public static final float height = 2;
    public static final int KNIGHT = 0;
    public static final int ARCHER = 1;

    boolean swinging = false;
    boolean alive = true;
    private boolean stabbing = false;
    private boolean shielding = false;
    private boolean shieldUp = false;
    private boolean drawingShield = false;

    private int swingDamage = 25;
    private int stabDamage = 15;

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    public boolean [] collisions; // Keeps tracks of all current collision. 1-Top, 2-Down, 3-Left, 4-Right



    public PlayerSoldier(){
        fillKeys();
    }

    public PlayerSoldier(Rectangle playerRect, int team){
        this.playerArea = playerRect;
        this.team = team;
        pickedClass = -1;
        vX = 0;
        vY = 0;
        this.health = 100;
        this.maxHealth = 100;
        fillKeys();
        collisions = new boolean[4];
    }

    public PlayerSoldier(Rectangle player, int team, String name){
        this.playerArea = player;
        this.team = team;
        this.name = name;
        this.vX = 0;
        this.vY = 0;
        this.health = 100;
        this.maxHealth = 100;
        fillKeys();
        collisions = new boolean[4];
    }

    private void fillKeys(){
        for(int i = 0; i<keysHeld.length;i++){
            keysHeld[i] = new MultiplayerTools.ClientControlObject();
        }
    }

    public void update(float deltaTime){
        this.animationTime+=deltaTime;
        if(this.hitCooldown>0) {
            this.hitCooldown = MathTools.towardsZero(this.hitCooldown, deltaTime);
        }
    }

    public void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }

    public void setHealth(int health){
        this.health = health;
    }

    public int getHealth(){
        return this.health;
    }

    public boolean isShielding() {
        return shielding;
    }

    public boolean isShieldUp() {
        return shieldUp;
    }

    public void setShielding(boolean shielding) {
        this.shielding = shielding;
    }

    public void setShieldUp(boolean shieldUp) {
        this.shieldUp = shieldUp;
    }

    public boolean isDrawingShield() {
        return drawingShield;
    }

    public void setDrawingShield(boolean drawingShield) {
        this.drawingShield = drawingShield;
    }

    public String getName(){return this.name;}

    public void setKeysHeld(MultiplayerTools.ClientControlObject[] keysHeld) {
        this.keysHeld = keysHeld;
    }

    public void setAnimationID(int animationID) {
        this.animationID = animationID;
    }

    public int getAnimationID() {
        return animationID|this.facingDirection;
    }

    public Animation [] [] [] getAnimationSet(){

        if (this.getTeam() == 0){
            return AnimationManager.redPlayer;
        }
        else {
            return AnimationManager.bluPlayer;
        }

    }

    public boolean isAnimationDone(){
        // Returns whether the current animation is done or not

        return getAnimation().isAnimationFinished(this.animationTime);

    }

    public int getFrameIndex(){
        return getAnimation().getKeyFrameIndex(this.animationTime);
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public MultiplayerTools.ClientControlObject[] getKeysHeld() {
        return keysHeld;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public boolean canMove(int direction){
        return !collisions[direction - 1];
    }

    public int getFacingDirection() {
        return facingDirection;
    }

    public float getHitCooldown() {
        return hitCooldown;
    }

    public void setFacingDirection(int facingDirection) {
        this.facingDirection = facingDirection;
    }
    public float getMouseAngle(){
        if(keysHeld[MultiplayerTools.Keys.ANGLE]==null){
            return 0;
        }
        return this.keysHeld[MultiplayerTools.Keys.ANGLE].angle;
    }
    public void setMouseAngle(float ang){
            if(keysHeld[MultiplayerTools.Keys.ANGLE]==null){
                this.keysHeld[MultiplayerTools.Keys.ANGLE] = new MultiplayerTools.ClientControlObject(ang);
            }
            else {
                this.keysHeld[MultiplayerTools.Keys.ANGLE].angle = ang;
            }
    }
    public int getMaxHealth(){
        return this.maxHealth;
    }

    public void move(){
        playerArea.x+=this.vX;
        playerArea.y+=this.vY;
    }

    public boolean isSwinging() {
        return swinging;
    }

    public void setSwinging(boolean swinging) {
        this.swinging = swinging;
    }

    /**
     * Draws the PlayerSoldier
     * @param batch
     */
    public void draw(SpriteBatch batch){
        TextureRegion drawFrame = (TextureRegion) getAnimation().getKeyFrame(this.animationTime);
        float trW = drawFrame.getRegionWidth();
        float trH = drawFrame.getRegionHeight();
        float ratio = trW/trH;
        if (this.getFacingDirection() == 0) {
            batch.draw(drawFrame, this.getX() - 0.84f, this.getY(), this.getHeight() * ratio + 0.14f, this.getHeight() + 0.13f);
        }
        else {
            batch.draw(drawFrame, this.getX() - 0.22f, this.getY(), this.getHeight() * ratio + 0.14f, this.getHeight() + 0.13f); // add 0.1 because the attacking sprites are 4 FCKING PIXELS TALLER THAN THE STANDING SPRITES
        }
        if(this.isDrawingBow()){ // bow drawing animation
                    float limAng = getMouseAngle(); // this angle will be clamped based on which direction the player is facing
                       if(this.getFacingDirection()==0){

                           limAng = MathUtils.clamp(limAng,160,200); // negative x part of circle
                       }
                       else{
                           if(limAng > 0 && limAng<90){ // 0 is straight right, meaning the top half of the right is 0-90 and the bottom half is 270-360, separate cases are needed for each
                               limAng = MathUtils.clamp(limAng,0,20); //positive x, positive y part of circle
                           }
                           else if(limAng>=270){
                               limAng = MathUtils.clamp(limAng,340,360); //positive x, negative y part of circle
                           }
                       }
                    if(this.getFacingDirection() == 0) {//draw the frames facing the correct direction based on the direction the player is facing
                        batch.draw((TextureRegion) AnimationManager.archerDrawLeft[this.getTeam()].getKeyFrame((float)bowDrawTime/30f), this.getX() - 0.85f, this.getY()+0.1f, 1.1f, 0.6f, 2, 2, 1, 1, 180+limAng);
                    }
                    else {
                        batch.draw((TextureRegion) AnimationManager.archerDrawRight[this.getTeam()].getKeyFrame((float)bowDrawTime/30f), this.getX() - 0.22f, this.getY()+0.1f, 0.7f, 0.6f, 2, 2, 1, 1, limAng);// add 0.1 because the attacking sprites are 4 FCKING PIXELS TALLER THAN THE STANDING SPRITES
                    }
                }
        }
    public Animation getAnimation(){ // gets the animation that should be drawn for the player in their current state
        Animation[][][] animationSet;
        if (this.getTeam() == 0) { // red
            animationSet = AnimationManager.redPlayer;
        } else { // blu
            animationSet = AnimationManager.bluPlayer;
        }
        if((this.getAnimationID()&AnimationManager.ISWALKING) == AnimationManager.WALK && (this.getAnimationID()&AnimationManager.ISATTACK)==AnimationManager.ATTACK){
            return animationSet[this.getAnimationID()& AnimationManager.DIRECTION][5][this.getCurrentClass()];
        }
        else if((this.getAnimationID()& AnimationManager.SHIELD_WALKING) == AnimationManager.SHIELD_WALKING){
            return animationSet[this.getAnimationID()& AnimationManager.DIRECTION][8][this.getCurrentClass()];
        }
        else if((this.getAnimationID()& AnimationManager.ISWALKING) == AnimationManager.WALK){
            return animationSet[this.getAnimationID()& AnimationManager.DIRECTION][0][this.getCurrentClass()];
        }
        else if((this.getAnimationID()& AnimationManager.ISFALLING) == AnimationManager.FALL){
            return animationSet[this.getAnimationID()& AnimationManager.DIRECTION][0][this.getCurrentClass()]; //TODO: change to 1 when falling sprites are added
        }
        else if((this.getAnimationID()& AnimationManager.ISJUMPING) == AnimationManager.JUMP){
            return animationSet[this.getAnimationID()& AnimationManager.DIRECTION][0][this.getCurrentClass()]; //TODO: change to 2 when jumping sprites are added
        }
        else if((this.getAnimationID()& AnimationManager.ISATTACK) == AnimationManager.ATTACK){
            return animationSet[this.getAnimationID()& AnimationManager.DIRECTION][3][this.getCurrentClass()];
        }
        else if((this.getAnimationID()& AnimationManager.SHIELD_IDLE) == AnimationManager.SHIELD_IDLE){
            return animationSet[this.getAnimationID()& AnimationManager.DIRECTION][6][this.getCurrentClass()];
        }
        else if((this.getAnimationID()& AnimationManager.SHIELD_DRAW_IDLE) == AnimationManager.SHIELD_DRAW_IDLE){
            return animationSet[this.getAnimationID()& AnimationManager.DIRECTION][7][this.getCurrentClass()];
        }
        else { // idling
            return animationSet[this.getAnimationID() & AnimationManager.DIRECTION][4][this.getCurrentClass()];
        }
    }
    public void setX(float x){
            this.playerArea.setX(x);
    }
    public void setY(float y){
        this.playerArea.setY(y);
    }
    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }
    public void setvX(float vX) {
        this.vX = vX;
    }

    public void setvY(float vY) {
        this.vY = vY;
    }

    public void setPos(float x, float y){
        this.playerArea.setPosition(x,y);
    }
    public void setPos(GridPoint2 pos){
        this.playerArea.setX(pos.x);
        this.playerArea.setY(pos.y);
    }

    public Rectangle getRect() {
        return playerArea;
    }

    public int getTeam() {
        return team;
    }

    public void setCurrentClass(int currentClass) {this.pickedClass = currentClass;
    }

    public int getCurrentClass() {
        return this.pickedClass;
    }
    public float getX(){
        return playerArea.getX();
    }
    public float getY(){
        return playerArea.getY();
    }

    public float getvX() {
        return vX;
    }

    public float getvY() {
        return vY;
    }

    public float getAnimationTime() {
        return animationTime;
    }

    public void setAnimationTime(float animationTime) {
        this.animationTime = animationTime;
    }
    public float getCenterX(){
        return (width/2f)+this.getX();
    }
    public float getCenterY(){
        return (height/2f)+this.getY();
    }
    public void attack(PlayerSoldier target){
        int side = (int)((target.getX()-this.getX())/Math.abs(target.getX()-this.getX())); // is positive when the target is on the right side of the player annd is negative when target is on left side of player
        if (target.shielding){
            // Swing Attack
            if (this.swinging){
                target.damage(this.swingDamage/2);
                target.knockBack(0.3f*side);
            }

            // Stab Attack
            if (this.stabbing){
                target.damage(this.stabDamage/2);
                target.knockBack(0.2f*side);
            }
        }
        else {
            // Swing Attack
            if (this.swinging){
                target.damage(this.swingDamage);//setHealth(Math.max(0,target.getHealth() - this.swingDamage));
                target.knockBack(0.5f*side);
            }

            // Stab Attack
            if (this.stabbing){
                target.damage(this.stabDamage);//setHealth(Math.max(0,target.getHealth() - this.stabDamage));
                target.knockBack(0.3f*side);
            }
        }
    }
    void knockBack(float val){

        this.setvX(val);


    }
    void damage(int amount){
        this.health = Math.max(0,this.health-amount);
        this.hitCooldown = 0.4f; // this is in seconds because it's being subtracted by deltatime
    }

    public String toString() {
        return name;
    }

    public int getBowDrawTime() {
        return bowDrawTime;
    }

    public void setBowDrawTime(int bowDrawTime) {
        this.bowDrawTime = bowDrawTime;
    }

    public boolean isDrawingBow() {
        return drawingBow;
    }

    public void setDrawingBow(boolean drawingBow) {
        this.drawingBow = drawingBow;
    }
    public void reset(){
        this.health = maxHealth;
        this.animationTime = 0;
        this.vX = 0;
        this.vY = 0;
        this.drawingBow = false;
        this.bowDrawTime = 0;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * returns the animation in bit form as a string
     * @return
     */
    public String animationString(){
        String sOut = "";
        int animation = this.getAnimationID();
        for(int i = 32; i>-1;i--){
            sOut=(animation%2)+sOut;
            animation/=2;
        }
        return sOut;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }
    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    public void addKill(){
        this.kills++;
    }
    public void addDeath(){
        this.deaths++;
    }
}
