package me.dumfing.multiplayerTools;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import org.lwjgl.util.Point;

/**
 * An object that links the visual componenets and functional components of a map together
 */

public class WorldMap {
    Pixmap collisionMap;
    Array<TextureRegion> visualComponent = new Array<TextureRegion>();
    TextureRegion foreground, background;
    GridPoint2 redSpawn, bluSpawn;
    int currentFrame = 0;
    int frameCount = 0;
    int frameTime;

    public WorldMap(TextureRegion colMap,TextureRegion visComp){
        if(!colMap.getTexture().getTextureData().isPrepared()){
            colMap.getTexture().getTextureData().prepare();
        }
        collisionMap = colMap.getTexture().getTextureData().consumePixmap();
        visualComponent = new Array<TextureRegion>();
        visualComponent.add(visComp);
        redSpawn = findColour(-65536);
        bluSpawn = findColour(255);
    }
    public void draw(SpriteBatch batch){
        batch.draw(visualComponent.get(currentFrame),0,0,collisionMap.getWidth(),collisionMap.getHeight());
    }
    public void drawBG(SpriteBatch batch, float xPersp, float yPersp){
        if(background!=null) {
            batch.draw(background,0-( xPersp / 10f),0- (yPersp / 10f), 128, 32);
        }
    }
    public void drawFG(SpriteBatch batch,float xPersp,float yPersp){
        if(foreground!=null) {
            batch.draw(foreground, xPersp*1.1f,yPersp*1.1f);
        }
    }
    public void setFrameRate(int fps){
        this.frameTime = 60/fps;
    }
    public void update(){
        if(frameCount == frameTime){
            frameCount = 0;
            currentFrame=(currentFrame+1)%visualComponent.size;
        }
        frameCount++;
    }

    public int getPosId(int x, int y){
        return collisionMap.getPixel(x,collisionMap.getHeight()-y)>>8;
    }

    public TextureRegion getVisualComponent() {
        return visualComponent.get(currentFrame);
    }

    public GridPoint2 getRedSpawn() {
        return redSpawn;
    }

    public GridPoint2 getBluSpawn() {
        return bluSpawn;
    }

    public Pixmap getCollisionMap() {
        return collisionMap;
    }
    public void addFrame(TextureRegion frame){
        this.visualComponent.add(frame);
    }
    public void addForeground(TextureRegion fg){
        this.foreground = fg;
    }
    public void addBackground(TextureRegion frame){
        this.background = frame;
    }
    /**
     * returns the GridPoint2 where the first occurence of the colour with the given ID si found
     * @param id
     */
    public GridPoint2 findColour(int id){
        for(int x = 0; x<collisionMap.getWidth();x++){
            for(int y = 0; y<collisionMap.getWidth();y++){
                if(getPosId(x,y)==id) {
                    System.out.println("spawn: "+x+" "+y+" "+id);
                    return new GridPoint2(x, y);
                }
            }
        }
        return new GridPoint2(0,0);
    }
}
