package me.dumfing.multiplayerTools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by aaronli on 2017-06-02.
 */
public class Projectile {
    private static final float CHECKRES = 0.1f;
    private float x,y;
    private float vX = 0;
    private float vY = 0;
    private boolean isHit = false;
    int projectileType = 0;
    //0 for arrow
    public Projectile(float x, float y,float speed, float angle, int type){
        this.x = x;
        this.y = y;
        this.vX = speed*(float) Math.cos(Math.toRadians(angle));
        this.vY = speed*(float) Math.sin(Math.toRadians(angle));
        this.projectileType = type;
    }
    public void checkCollisions(PlayerSoldier[] players, WorldMap world){
        float hyp = (float)Math.hypot(this.vX,this.vY);
        float xAmt = this.vX/hyp;
        float yAmt = this.vY/hyp;
        for(float i = 0; i<hyp;i+=CHECKRES){
            float checkX = xAmt*i;
            float checkY = yAmt*i;
            for(PlayerSoldier player : players){
                if(player.getRect().contains(checkX,checkY)){
                    this.x = checkX;
                    this.y = checkY;
                    isHit = true;
                    break;
                }
            }
            if(!isHit && world.getPosId((int)checkX,(int)checkY) == 1){
                //TODO: action when collided
                this.x = checkX;
                this.y = checkY;
                isHit = true;
                break;
            }
        }
        this.move(); // if it hit nothing then move it to the intended destination
    }
    public void move(){
        if(!isHit){
            this.x+=vX;
            this.y+=vY;
        }
    }
    public void draw(SpriteBatch batch, TextureRegion textureRegion){
        float angle = (float) Math.toDegrees(Math.atan2(this.vY,this.vX));
        batch.draw(textureRegion,this.x,this.y,0.25f,0.1f,0.5f,0.2f,0.5f,0.2f,angle);
    }
}
