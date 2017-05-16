package me.dumfing.multiplayerTools;


import com.badlogic.gdx.math.Rectangle;

import java.util.Arrays;

/**
 * Created by dumpl on 4/28/2017.
 */
public class PlayerSoldier {
// a more detailed version of the players that will be sent at the start but won't be sent around as much later
    private int health, maxHealth;
    private boolean canJump;
    private boolean[] keysHeld = new boolean[8];
    private Rectangle playerArea;
    private float vX, vY;
    private int team, pickedClass;
    private String name;
    public PlayerSoldier(){}
    public PlayerSoldier(Rectangle playerRect, int team){
        this.playerArea = playerRect;
        this.team = team;
        pickedClass = -1;
        vX = 0;
        vY = 0;
        this.health = 100;
        this.maxHealth = 100;
    }
    public PlayerSoldier(Rectangle player, int team, String name){
        this.playerArea = player;
        this.team = team;
        this.name = name;
        this.vX = 0;
        this.vY = 0;
        this.health = 100;
        this.maxHealth = 100;
    }
    public void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }
    public String getName(){return this.name;}
    public void setKeysHeld(boolean[] keysHeld) {
        this.keysHeld = keysHeld;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public boolean[] getKeysHeld() {
        return keysHeld;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public int getHealth(){
        return this.health;
    }
    public int getMaxHealth(){
        return this.maxHealth;
    }
    public void move(){
        playerArea.x+=this.vX;
        playerArea.y+=this.vY;
        //System.out.println("after moving "+super.getPos());
    }
    public void setX(float x){
            this.playerArea.setX(x);
    }
    public void setY(float y){
        this.playerArea.setY(y);
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
