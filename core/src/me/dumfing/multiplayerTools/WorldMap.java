package me.dumfing.multiplayerTools;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An object that links the visual componenets and functional components of a map together
 */
public class WorldMap {
    Pixmap collisionMap;
    TextureRegion visualComponent;
    public WorldMap(TextureRegion colMap,TextureRegion visComp){
        if(!colMap.getTexture().getTextureData().isPrepared()){
            colMap.getTexture().getTextureData().prepare();
        }
        collisionMap = colMap.getTexture().getTextureData().consumePixmap();
        visualComponent = visComp;
    }
    public int getPosId(int x, int y){
        return collisionMap.getPixel(x,collisionMap.getHeight()-y)>>8;
    }

    public TextureRegion getVisualComponent() {
        return visualComponent;
    }

    public Pixmap getCollisionMap() {
        return collisionMap;
    }
}
