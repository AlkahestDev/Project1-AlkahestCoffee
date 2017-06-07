package me.dumfing.menus;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;

@Deprecated
public class SettingsMenu extends Menu{
    /**
     * Constructor for the menu
     *
     * @param bmfc    BitMapFontCache for drawing all text in the menu
     * @param manager
     * @param camera
     */
    public SettingsMenu(Array<BitmapFontCache> bmfc, AssetManager manager, OrthographicCamera camera) {
        super(bmfc, manager, camera);
    }

    @Override
    public void init() {
        super.init();
        super.setBackground(MenuTools.mGTR("simpleBG.png",getManager()));//new TextureRegion((Texture)getManager().get("4k-image-santiago.jpg")));
    }
}
