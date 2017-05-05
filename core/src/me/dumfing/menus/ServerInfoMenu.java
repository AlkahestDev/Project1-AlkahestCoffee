package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.multiplayerTools.MultiplayerTools;
import me.dumfing.server.CoffeeServer;
import me.dumfing.server.MainServer;

/**
 * Created by dumpl on 5/3/2017.
 */
public class ServerInfoMenu extends Menu{
    MenuTools.QueueText peopleConnected;
    boolean timerStarted = false;
    int numFrames = 0;
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
        peopleConnected = new MenuTools.QueueText(5, Gdx.graphics.getHeight()-35,0,0);
        peopleConnected.setFont(0);
        peopleConnected.setText("",super.getFonts());
        super.setBackground(new TextureRegion((Texture)getManager().get("tuzki.png")));
        super.addQueueText(peopleConnected);
        super.init();
    }

    public void update(MainServer svIn) {
        timerStarted = (float)svIn.getPlayers().size()/(float)svIn.getMaxPlayers()>0.6f && !timerStarted;
        if(timerStarted){
            numFrames++;
            if(numFrames % 60 == 0){
                System.out.println(numFrames);
                System.out.println("Sent "+(6-(numFrames/60)));
                svIn.secureSendAll(new MultiplayerTools.ServerGameCountdown(6-(numFrames/60)));
            }
            if(numFrames/60 == 6){
                CoffeeServer.svState = CoffeeServer.ServerState.RUNNINGGAME;
            }
        }
    }
    public void updateMenuInfo(MainServer svIn){
        peopleConnected.setText(String.format("%s%d|%d",svIn.getPlayers().size()>=svIn.getMaxPlayers()?"[RED]":"",svIn.getPlayers().size(),svIn.getMaxPlayers()),getFonts());
    }
}
