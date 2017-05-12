package me.dumfing.multiplayerTools;


import com.badlogic.gdx.math.Rectangle;

/**
 * Created by dumpl on 4/28/2017.
 */
public class PlayerSoldier extends MultiplayerTools.ServerPlayerInfo {
// a more detailed version of the players that will be sent at the start but won't be sent around as much later
    private int health, maxHealth;
    private boolean canJump;
    private boolean[] keysHeld = new boolean[8];
    public PlayerSoldier(Rectangle playerRect, int team){
        super(playerRect,team,null,0);
        super.setvX(0);
        super.setvY(0);
        this.health = 100;
        this.maxHealth = 100;
    }
    public PlayerSoldier(Rectangle player, int team, String name){
        super(player, team, name,0);
        super.setvX(0);
        super.setvY(0);
        this.health = 100;
        this.maxHealth = 100;
    }
    public void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }
    public MultiplayerTools.ServerPlayerInfo getPlayerInfo(){
        //TODO: make a constructor that includes the vx and vy
        MultiplayerTools.ServerPlayerInfo temp = new MultiplayerTools.ServerPlayerInfo(this); // a stripped down version of this for what other people see
        temp.setvX(this.getvX());
        temp.setvY(this.getvY());
        return temp;
    }

    public void setKeysHeld(boolean[] keysHeld) {
        this.keysHeld = keysHeld;
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
        return super.getHealth();
    }
    public int getMaxHealth(){
        return this.maxHealth;
    }
    public void move(){
        super.getRect().x+=super.getvX();
        super.getRect().y+=super.getvY();
        System.out.println("after moving "+super.getPos());
    }
    public void setX(float x){
            this.getRect().setX(x);
    }
    public void setY(float y){
        this.getRect().setY(y);
    }
    public void setPos(float x, float y){
        this.getRect().setPosition(x,y);
    }


    public void setCurrentClass(int currentClass) {
        super.setPickedClass(currentClass);
    }

    public int getCurrentClass() {
        return super.getPickedClass();
    }
}
