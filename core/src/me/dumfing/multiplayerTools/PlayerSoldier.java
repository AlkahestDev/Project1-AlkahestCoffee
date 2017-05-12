package me.dumfing.multiplayerTools;


import com.badlogic.gdx.math.Rectangle;

/**
 * Created by dumpl on 4/28/2017.
 */
public class PlayerSoldier extends MultiplayerTools.ServerPlayerInfo {
// a more detailed version of the players that will be sent at the start but won't be sent around as much later
    private int health, maxHealth;
    private float vX, vY;
    public PlayerSoldier(Rectangle playerRect, int team){
        super(playerRect,team,null,0);
        this.vX = 0;
        this.vY = 0;
        this.health = 100;
        this.maxHealth = 100;
    }
    public PlayerSoldier(Rectangle player, int team, String name){
        super(player, team, name,0);
        this.vX = 0;
        this.vY = 0;
        this.health = 100;
        this.maxHealth = 100;
    }
    public void setMaxHealth(int maxHealth){
        this.maxHealth = maxHealth;
    }
    public MultiplayerTools.ServerPlayerInfo getPlayerInfo(){
        return new MultiplayerTools.ServerPlayerInfo(this); // a stripped down version of this for what other people see
    }
    public int getHealth(){
        return super.getHealth();
    }
    public int getMaxHealth(){
        return this.maxHealth;
    }
    public void move(){
        System.out.println("before moving "+super.getPos()+" "+this.vY);
        super.getRect().x+=this.getvX();
        super.getRect().y+=this.getvY();
        System.out.println("after moving "+super.getPos());
    }
    public float getvY() {
        return vY;
    }

    public float getvX() {

        return vX;
    }

    public void setVx(float vX) {
        this.vX = vX;
    }

    public void setVy(float vY) {
        this.vY = vY;
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
