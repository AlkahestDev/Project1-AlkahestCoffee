package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.server.MainServer;

/**
 * Created by dumpl on 5/5/2017.
 */
public class ServerRunningGameMenu extends Menu{
    MenuTools.QueueText numConnected;
    /**
     * Constructor for the menu
     *
     * @param bmfc    BitMapFontCache for drawing all text in the menu
     * @param manager
     * @param camera
     */
    public ServerRunningGameMenu(Array<BitmapFontCache> bmfc, AssetManager manager, OrthographicCamera camera) {
        super(bmfc, manager, camera);
    }

    @Override
    public void init() {
        numConnected = new MenuTools.QueueText(5, Gdx.graphics.getHeight()-35,0,0);
        numConnected.setFont(0);
        numConnected.setText("",getFonts());
        super.addQueueText(numConnected);
        super.setBackground(new TextureRegion((Texture)getManager().get("4k-image-santiago.jpg")));
    }

    @Override
    public void update() {
    }
    public void updateMenuInfo(MainServer svIn){
        /*svIn.getPlayers().size();
        svIn.getMaxPlayers();
        getFonts().get(0);*/
        numConnected.setText(String.format("%s%d|%d",svIn.getPlayers().size()>=svIn.getMaxPlayers()?"[RED]":"",svIn.getPlayers().size(),svIn.getMaxPlayers()),getFonts());
    }
}
