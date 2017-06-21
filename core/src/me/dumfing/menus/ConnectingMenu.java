package me.dumfing.menus;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;


public class ConnectingMenu extends Menu{

    /**
     * Constructor for the menu
     *
     * @param bmfc    BitMapFontCache for drawing all text in the menu
     * @param manager
     * @param camera
     */
    public ConnectingMenu(Array<BitmapFontCache> bmfc, AssetManager manager, OrthographicCamera camera) {
        super(bmfc, manager, camera);
    }

    @Override
    public void init() {
        for(int i = 1;i<15;i++){
            super.addBackground(MenuTools.mGTR(String.format("loading/tuzkii/tuzkiii%d.png",i),getManager()));//new TextureRegion((Texture)getManager().get(String.format("loading/tuzkii/tuzkiii%d.png",i))));
        }
        super.setFrameRate(30);
    }
}
