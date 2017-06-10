package me.dumfing.multiplayerTools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;

public class PlayerSoldier {

    private int health, maxHealth, animationID, team, pickedClass,facingDirection; // animationID is an int describing the direction the player is facing and what animation they're doing
    private boolean canJump;

    private MultiplayerTools.ClientControlObject[] keysHeld = new MultiplayerTools.ClientControlObject[10];

    private Rectangle playerArea;

    private float vX, vY, animationTime;

    private String name = "[#FFFFFF]N[#DDDDDD]O[#BBBBBB]_[#999999]N[#777777]A[#555555]M[#333333]E[#111111]!";

    public static final float width = 1;
    public static final float height = 2;
    public static final int KNIGHT = 0;
    public static final int ARCHER = 1;

    public boolean swinging = false;
    public boolean stabbing = false;
    public boolean sheilding = false;
    public int swingDamage = 10;
    public int stabDamage = 5;

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
            return PlayerAnimations.redPlayer;
        }
        else {
            return PlayerAnimations.bluPlayer;
        }

    }

    public boolean isAnimationDone(){
        // Returns whether the current animation is done or not

        return getAnimationSet()[this.getAnimationID()&PlayerAnimations.DIRECTION][0][this.getCurrentClass()].isAnimationFinished(this.animationTime);

    }

    public int getFrame(){
        return getAnimationSet()[this.getAnimationID()&PlayerAnimations.DIRECTION][0][this.getCurrentClass()].getKeyFrameIndex(this.animationTime);
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
        //System.out.println("after moving "+super.getPos());
    }

    /**
     * Draws the PlayerSoldier
     * @param batch
     * @param isPlayer If this PlayerSoldier is the PlayerSoldier the client is viewing
     */
    public void draw(SpriteBatch batch, boolean isPlayer){
        TextureRegion drawFrame;
        Animation[][][] animationSet;
            if (this.getTeam() == 0) { // red
                animationSet = PlayerAnimations.redPlayer;
            } else { // blu
                animationSet = PlayerAnimations.bluPlayer;
            }

        //System.out.println(this.animationTime);
        //System.out.println(this.getAnimationID());
        if((this.getAnimationID()&PlayerAnimations.ISWALKING) == PlayerAnimations.WALK){
            drawFrame = (TextureRegion) animationSet[this.getAnimationID()&PlayerAnimations.DIRECTION][0][this.getCurrentClass()].getKeyFrame(this.animationTime);
        }
        else if((this.getAnimationID()&PlayerAnimations.ISFALLING) == PlayerAnimations.FALL){
            drawFrame = (TextureRegion) animationSet[this.getAnimationID()&PlayerAnimations.DIRECTION][0][this.getCurrentClass()].getKeyFrame(this.animationTime); //TODO: change to 1 when falling sprites are added
        }
        else if((this.getAnimationID()&PlayerAnimations.ISJUMPING) == PlayerAnimations.JUMP){
            drawFrame = (TextureRegion) animationSet[this.getAnimationID()&PlayerAnimations.DIRECTION][0][this.getCurrentClass()].getKeyFrame(this.animationTime); //TODO: change to 2 when jumping sprites are added
        }
        else if((this.getAnimationID()&PlayerAnimations.ISATTACK) == PlayerAnimations.ATTACK){
            drawFrame = (TextureRegion) animationSet[this.getAnimationID()&PlayerAnimations.DIRECTION][3][this.getCurrentClass()].getKeyFrame(this.animationTime); //TODO: change to 3 when attacking sprites are added
        }
        else { // idling
            drawFrame = (TextureRegion) animationSet[this.getAnimationID() & PlayerAnimations.DIRECTION][4][this.getCurrentClass()].getKeyFrame(this.animationTime);
        }
        batch.draw(drawFrame,this.getX(),this.getY(),this.getWidth(),this.getHeight());
    }

    public void setX(float x){
            this.playerArea.setX(x);
    }
    public void setY(float y){
        this.playerArea.setY(y);
    }
    public float getWidth(){
        return this.width;
    }
    public float getHeight(){
        return this.height;
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
        return (this.width/2f)+this.getX();
    }
    public float getCenterY(){
        return (this.height/2f)+this.getY();
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
}
