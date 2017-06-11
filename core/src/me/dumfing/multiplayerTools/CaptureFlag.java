package me.dumfing.multiplayerTools;
//FILENAME
//Aaron Li  6/11/2017
//EXPLAIN

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;

import java.util.HashMap;

public class CaptureFlag {
    private float xPos, yPos;
    private float animationTime = 0;
    private int physicsParent = -1;
    private int teamID = -1; //whichever team the flag is on
    private int facingDirection = 0;
    public CaptureFlag(){}
    public CaptureFlag(float x, float y, int teamID){
        this.xPos = x;
        this.yPos = y;
        this.teamID = teamID;
    }
    public CaptureFlag(GridPoint2 pos, int teamID){
        this.xPos = pos.x;
        this.yPos = pos.y;
        this.teamID = teamID;
    }
    public void update(float deltaTime, HashMap<Integer, PlayerSoldier> players){
        if(players.containsKey(this.physicsParent)){
            PlayerSoldier parent = players.get(this.physicsParent);
            this.xPos = parent.getX();
            this.yPos = parent.getY();
            this.facingDirection = parent.getFacingDirection();
        }
        this.animationTime+=deltaTime;

    }
    public void draw(SpriteBatch batch, HashMap<Integer, PlayerSoldier> players){
        if(teamID!=-1){
            if(players.containsKey(this.physicsParent)){
                PlayerSoldier parent = players.get(this.physicsParent);
                batch.draw((TextureRegion) (this.teamID==0?AnimationManager.redFlag:AnimationManager.bluFlag)[parent.getFacingDirection()].getKeyFrame(this.animationTime),this.xPos,this.yPos,1,2);
            }
            else{
                batch.draw((TextureRegion) (this.teamID==0?AnimationManager.redFlag:AnimationManager.bluFlag)[this.facingDirection].getKeyFrame(this.animationTime),this.xPos,this.yPos,1,2);
            }
        }
    }
    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public int getPhysicsParent() {
        return physicsParent;
    }

    public int getTeamID() {
        return teamID;
    }
}
