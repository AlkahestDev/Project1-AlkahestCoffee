package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.server.MainServer;

/**
 * Created by dumpl on 5/3/2017.
 */
public class ServerInfoMenu extends Menu{
    int numConnected = 0;
    MenuTools.QueueText peopleConnected;
    /**
     * Constructor for the menu
     *
     * @param bmfc    BitMapFontCache for drawing all text in the menu
     * @param manager
     * @param camera
     */
    public ServerInfoMenu(Array<BitmapFontCache> bmfc, AssetManager manager, OrthographicCamera camera) {
        super(bmfc, manager, camera);
    }

    @Override
    public void init() {
        peopleConnected = new MenuTools.QueueText(5, Gdx.graphics.getHeight()-30,0,0);
        peopleConnected.setFont(0);
        peopleConnected.setText("",super.getFonts());
        super.setBackground(new TextureRegion((Texture)getManager().get("tuzki.png")));
        super.addQueueText(peopleConnected);
        super.init();
    }

    @Override
    public void update() {
        super.update();
    }
    public void updateMenuInfo(MainServer svIn){
        peopleConnected.setText(String.format("%s%d|%d",svIn.getPlayers().size()>=svIn.getMaxPlayers()?"[RED]":"",svIn.getPlayers().size(),svIn.getMaxPlayers()),getFonts());
    }
}
