package me.dumfing.multiplayerTools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

import java.util.HashMap;
import java.util.LinkedList;

import static me.dumfing.multiplayerTools.MultiplayerTools.GRAVITY;
import static me.dumfing.multiplayerTools.MultiplayerTools.REDTEAM;

/**
 * Created by aaronli on 2017-06-02.
 */
public class Projectile {
    private static final float CHECKRES = 0.001f;
    public static final int MAXLIFETIME = 240;
    private float x,y;
    private float vX = 0;
    private float vY = 0;
    private boolean isHit = false;
    int timeAlive = 0;
    private float angle = 0;
    private int projectileType = 0;
    private int attackerTeam = REDTEAM;
    private int caster = -1; // the player that fired the projectile
    int physicsParent = -1; //upon hitting a player, the arrow will inherit all of their velocities
    boolean particlesStarted = false;
    //0 for arrow
    public Projectile(){}
    public Projectile(float x, float y,float speed, float angle, int type, int attackerTeam, int caster){
        this.x = x;
        this.y = y;
        this.vX = speed*(float) Math.cos(Math.toRadians(angle));
        this.vY = speed*(float) Math.sin(Math.toRadians(angle));
        this.projectileType = type;
        this.attackerTeam = attackerTeam;
        this.caster = caster;
    }
    public void checkCollisions(HashMap<Integer,PlayerSoldier> players, WorldMap world) {
        timeAlive++;
        if (physicsParent == -1) {
            float hyp = (float) Math.hypot(this.vX, this.vY);
            float xAmt = this.vX / hyp;
            float yAmt = this.vY / hyp;
            for (float i = 0; i < hyp; i += CHECKRES) {
                float checkX = xAmt * i;
                float checkY = yAmt * i;
                for (Integer k : players.keySet()) {
                    PlayerSoldier player = players.get(k);
                    if (player.getTeam() != this.attackerTeam && player.getRect().contains(x + checkX, y + checkY)) {
                        this.x += checkX;
                        this.y += checkY;
                        player.damage((int) Math.round(Math.hypot(this.vX,this.vY)*20));
                        this.vX = 0;
                        this.vY = 0;
                        isHit = true;
                        physicsParent = k;
                        return;
                    }
                }
                if (!isHit && ((world.getPosId(Math.round(x + checkX), (int) (y + checkY + 1)) == 0x000001FF)||(world.getPosId(Math.round(x + checkX), (int) (y + checkY + 1)) ==(this.attackerTeam==0?0x00FFFFFF:0xFFFF00FF)))) {
                    //TODO: action when collided
                    this.x += checkX;
                    this.y += checkY;
                    this.vX = 0;
                    this.vY = 0;
                    isHit = true;
                    return;
                }
            }
        }
        else {//TODO: figure out why arrows are being destroyed when they hit players
            if(players.size()-1 <physicsParent||players.get(physicsParent) == null||!players.get(physicsParent).isAlive()){
                this.timeAlive = MAXLIFETIME;
            }
            else {
                this.vX = players.get(physicsParent).getvX();
                this.vY = players.get(physicsParent).getvY();
            }
        }
        this.move(); // if it hit nothing then move it to the intended destination
        if(!isHit) {
            this.vY += GRAVITY;
            angle = (float) Math.toDegrees(Math.atan2(this.vY,this.vX));
        }
    }

    public int getTimeAlive() {
        return timeAlive;
    }

    public void move(){
            this.x+=vX;
            this.y+=vY;
    }
    public void draw(SpriteBatch batch, TextureRegion textureRegion){
        batch.draw(textureRegion,this.x,this.y,0,0,0.6f,0.15f,1f,1f,angle);
    }

    public int getProjectileType() {
        return projectileType;
    }

    public int getAttackerTeam() {
        return attackerTeam;
    }

    public String toString() {
        return "Projectile{" +
                "x=" + x +
                ", y=" + y +
                ", vX=" + vX +
                ", vY=" + vY +
                ", isHit=" + isHit +
                ", projectileType=" + projectileType +
                ", attackerTeam=" + attackerTeam +
                '}';
    }
    public GridPoint2 getAttackPair(){ // returns the caster and physicsparent (player who is hit by arrow)
        return new GridPoint2(caster,physicsParent);
    }

    public boolean isParticlesStarted() {
        return particlesStarted;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setParticlesStarted(boolean particlesStarted) {
        this.particlesStarted = particlesStarted;
    }
}
