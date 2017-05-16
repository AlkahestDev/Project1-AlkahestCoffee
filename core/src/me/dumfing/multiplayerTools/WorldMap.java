package me.dumfing.multiplayerTools;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * An object that links the visual componenets and functional components of a map together
 */
public class WorldMap {
    Pixmap collisionMap;
    Array<TextureRegion> visualComponent = new Array<TextureRegion>();
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
    }
    public void draw(SpriteBatch batch){
        batch.begin();
        batch.draw(visualComponent.get(currentFrame),0,0,collisionMap.getWidth(),collisionMap.getHeight());
        batch.end();
    }
    public void setFrameRate(int fps){
        this.frameTime = 60/fps;
    }
    public void update(){
        if(frameCount == frameTime){
            frameCount = 0;
            currentFrame++;
        }
        frameCount++;
    }
    public int getPosId(int x, int y){
        return collisionMap.getPixel(x,collisionMap.getHeight()-y)>>8;
    }

    public TextureRegion getVisualComponent() {
        return visualComponent.get(currentFrame);
    }

    public Pixmap getCollisionMap() {
        return collisionMap;
    }
}
