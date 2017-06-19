package me.dumfing.multiplayerTools;
//FILENAME
//Aaron Li  6/11/2017
//EXPLAIN

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;

public class CaptureFlag {
    private Rectangle hitBox;
    private float animationTime = 0;
    private int physicsParent = -1;
    private int teamID = -1; //whichever team the flag is on
    private int facingDirection = 0;
    private boolean scored = false;
    public CaptureFlag(){}
    public CaptureFlag(float x, float y, int teamID){
        this.hitBox = new Rectangle(x,y,1,2);
        this.teamID = teamID;
    }
    public CaptureFlag(GridPoint2 pos, int teamID){
        this.hitBox = new Rectangle(pos.x,pos.y,1,2);
        this.teamID = teamID;
    }

    public void setPhysicsParent(int physicsParent) {
        this.physicsParent = physicsParent;
    }

    public void update(float deltaTime, HashMap<Integer, PlayerSoldier> players, WorldMap world, int[] score){
        if(players.containsKey(this.physicsParent)){
            PlayerSoldier parent = players.get(this.physicsParent);
            hitBox.setPosition(parent.getX(),parent.getY());
            this.facingDirection = parent.getFacingDirection();
        }
        else{
            this.physicsParent = -1;
            resetPos(world);
        }
        for(Integer playerKey : players.keySet()){
            PlayerSoldier player = players.get(playerKey);
            if(player.getTeam() != this.teamID) {
                if (player.getRect().overlaps(this.hitBox)) {
                    this.physicsParent = playerKey;
                    break;
                }
            }
        }
        this.animationTime+=deltaTime;


    }
    public void draw(SpriteBatch batch, HashMap<Integer, PlayerSoldier> players){
        if(teamID!=-1){
            if(players.containsKey(this.physicsParent)){
                PlayerSoldier parent = players.get(this.physicsParent);
                batch.draw((TextureRegion) (this.teamID==0?AnimationManager.redFlag:AnimationManager.bluFlag)[1-parent.getFacingDirection()].getKeyFrame(this.animationTime),this.getxPos()+(parent.getFacingDirection()==0?0.75f:-0.8f),this.getyPos()+1f,1,2);
            }
            else{
                batch.draw((TextureRegion) (this.teamID==0?AnimationManager.redFlag:AnimationManager.bluFlag)[this.facingDirection].getKeyFrame(this.animationTime),this.getxPos(),this.getyPos(),1,2);
            }
        }
    }
    public float getxPos() {
        return this.hitBox.getX();
    }

    public float getyPos() {
        return this.hitBox.getY();
    }

    public int getPhysicsParent() {
        return physicsParent;
    }

    public int getTeamID() {
        return teamID;
    }
    public void resetPos(WorldMap world){
        GridPoint2 flagPos = this.teamID==0?world.getRedFlag():world.getBluFlag();
        this.physicsParent = -1;
        this.hitBox.setPosition(flagPos.x,flagPos.y);
    }

    public boolean isScored() {
        return scored;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }
}
