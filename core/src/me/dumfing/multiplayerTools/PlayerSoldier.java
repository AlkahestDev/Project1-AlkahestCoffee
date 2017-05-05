package me.dumfing.multiplayerTools;


import com.badlogic.gdx.math.Rectangle;

/**
 * Created by dumpl on 4/28/2017.
 */
public class PlayerSoldier extends MultiplayerTools.ClientPlayerInfo {
    // a more detailed version of the players that will be sent at the start but won't be sent around as much later
        private int health, maxHealth;
        private float vX, vY;
        public PlayerSoldier(Rectangle playerRect, int team){
            super(playerRect,team,null);
            this.vX = 0;
            this.vY = 0;
            this.health = 100;
            this.maxHealth = 100;
        }
        public PlayerSoldier(Rectangle player, int team, String name){
            super(player, team, name);
            this.vX = 0;
            this.vY = 0;
            this.health = 100;
            this.maxHealth = 100;
        }
        public void setMaxHealth(int maxHealth){
            this.maxHealth = maxHealth;
        }
        public MultiplayerTools.ClientPlayerInfo getPlayerInfo(){
            return new MultiplayerTools.ClientPlayerInfo(this); // a stripped down version of this for what other people see
        }
        public int getHealth(){
            return super.getHealth();
        }
        public int getMaxHealth(){
            return this.maxHealth;
        }

    public float getvY() {
        return vY;
    }

    public float getvX() {

        return vX;
    }
}
