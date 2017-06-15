package me.dumfing.multiplayerTools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;

public class PlayerSoldier {
    //Arrow drawing will work by having a counter that increases as the mouse is down. If drawingBow is true and the mouse is up the arrow will be fired, and the counter will be set to 0
    private int health, maxHealth, animationID, team, pickedClass,facingDirection,drawTime; // animationID is an int describing the direction the player is facing and what animation they're doing
    private boolean canJump, drawingBow;

    private MultiplayerTools.ClientControlObject[] keysHeld = new MultiplayerTools.ClientControlObject[10];

    private Rectangle playerArea;

    private float vX, vY, animationTime;

    private String name = "[#FF0000]N[#DD1100]O[#BB3300]_[#995500]N[#777700]A[#559900]M[#33BB00]E[#11DD00]!";

    public static final float width = 1;
    public static final float height = 2;
    public static final int KNIGHT = 0;
    public static final int ARCHER = 1;

    boolean swinging = false;
    private boolean stabbing = false;
    private boolean shielding = false;
    private int swingDamage = 10;
    private int stabDamage = 5;

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

    public String getName(){return this.name;}

    public void setKeysHeld(MultiplayerTools.ClientControlObject[] keysHeld) {
        this.keysHeld = keysHeld;
    }

    public void setAnimationID(int animationID) {
        //this.animationTime = 0;
        this.animationID = animationID;
    }

    public int getAnimationID() {
        return animationID+this.facingDirection;
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

    public void setFacingDirection(int facingDirection) {
        this.facingDirection = facingDirection;
    }
    public float getMouseAngle(){
        if(keysHeld[MultiplayerTools.Keys.ANGLE]==null){
            return 0;
        }
        return this.keysHeld[MultiplayerTools.Keys.ANGLE].angle;
    }

    public int getMaxHealth(){
        return this.maxHealth;
    }

    public void move(){
        playerArea.x+=this.vX;
        playerArea.y+=this.vY;
    }

    /**
     * Draws the PlayerSoldier
     * @param batch
     * @param isPlayer If this PlayerSoldier is the PlayerSoldier the client is viewing
     */
    public void draw(SpriteBatch batch, boolean isPlayer){
        TextureRegion drawFrame = (TextureRegion) getAnimation().getKeyFrame(this.animationTime);
        float trW = drawFrame.getRegionWidth();
        float trH = drawFrame.getRegionHeight();
        float ratio = trW/trH;
        //if((this.getAnimationID()&AnimationManager.ISATTACK) == AnimationManager.ATTACK){
        switch(this.getCurrentClass()) {
            case KNIGHT:
             if (this.getFacingDirection() == 0) {
                 batch.draw(drawFrame, this.getX() - 0.84f, this.getY(), this.getHeight() * ratio + 0.14f, this.getHeight() + 0.13f);
             } else {
                 batch.draw(drawFrame, this.getX() - 0.22f, this.getY(), this.getHeight() * ratio + 0.14f, this.getHeight() + 0.13f); // add 0.1 because the attacking sprites are 4 FCKING PIXELS TALLER THAN THE STANDING SPRITES
               }
            break;
            case ARCHER:
                if (this.getFacingDirection() == 0) {
                    batch.draw(drawFrame, this.getX() -0.28f, this.getY(), this.getHeight() * ratio + 0.14f, this.getHeight() + 0.13f);
                } else {
                    batch.draw(drawFrame, this.getX() - 0.22f, this.getY(), this.getHeight() * ratio + 0.14f, this.getHeight() + 0.13f); // add 0.1 because the attacking sprites are 4 FCKING PIXELS TALLER THAN THE STANDING SPRITES
                }
            break;
        }
        //}
        //else {
        //    batch.draw(drawFrame, this.getX(), this.getY(), this.getHeight()*ratio, this.getHeight());
        //}
    }
    public Animation getAnimation(){
        Animation[][][] animationSet;
        if (this.getTeam() == 0) { // red
            animationSet = AnimationManager.redPlayer;
        } else { // blu
            animationSet = AnimationManager.bluPlayer;
        }
        if((this.getAnimationID()& AnimationManager.ISWALKING) == AnimationManager.WALK){
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
                target.setHealth(Math.max(0,target.getHealth() - this.swingDamage / 2));
                target.knockBack(0.3f*side);
            }

            // Stab Attack
            if (this.stabbing){
                target.setHealth(Math.max(0,target.getHealth() - this.stabDamage / 2));
                target.knockBack(0.2f*side);
            }
        }
        else {
            // Swing Attack
            if (this.swinging){
                target.setHealth(Math.max(0,target.getHealth() - this.swingDamage));
                target.knockBack(0.5f*side);
            }

            // Stab Attack
            if (this.stabbing){
                target.setHealth(Math.max(0,target.getHealth() - this.stabDamage));
                target.knockBack(0.3f*side);
            }
        }
    }
    void knockBack(float val){

        this.setvX(val);


    }
    void damage(int amount){
        System.out.println(amount);
        this.health = Math.max(0,this.health-amount);
    }
    @Override
    public String toString() {
        return "PlayerSoldier{" +
                "health=" + health +
                ", maxHealth=" + maxHealth +
                ", canJump=" + canJump +
                ", keysHeld=" + Arrays.toString(keysHeld) +
                ", playerArea=" + playerArea +
                ", vX=" + vX +
                ", vY=" + vY +
                ", team=" + team +
                ", pickedClass=" + pickedClass +
                ", name='" + name + '\'' +
                '}';
    }

    public int getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(int drawTime) {
        this.drawTime = drawTime;
    }

    public boolean isDrawingBow() {
        return drawingBow;
    }

    public void setDrawingBow(boolean drawingBow) {
        this.drawingBow = drawingBow;
    }
}
