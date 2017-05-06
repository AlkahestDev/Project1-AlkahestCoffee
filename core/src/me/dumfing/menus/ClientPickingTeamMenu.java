package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.multiplayerTools.MultiplayerClient;
import me.dumfing.multiplayerTools.MultiplayerTools;

/**
 * Created by dumpl on 5/5/2017.
 */
public class ClientPickingTeamMenu extends Menu{

    /**
     * Constructor for the menu
     *
     * @param bmfc    BitMapFontCache for drawing all text in the menu
     * @param manager
     * @param camera
     */
    public ClientPickingTeamMenu(Array<BitmapFontCache> bmfc, AssetManager manager, OrthographicCamera camera) {
        super(bmfc, manager, camera);
    }

    public void init(final MultiplayerClient cl) {
        final MenuTools.Button tempRedTeam  =new MenuTools.Button(5,5, Gdx.graphics.getWidth()/2-10,Gdx.graphics.getHeight()-10);
        final MenuTools.Button tempBluTeam = new MenuTools.Button(Gdx.graphics.getWidth()/2+5,5,Gdx.graphics.getWidth()/2-10,Gdx.graphics.getHeight()-10);
        tempRedTeam.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                cl.secureSend(new MultiplayerTools.ClientPickedTeam(0));
                tempRedTeam.setVelocity(-30,0);
                tempBluTeam.setVelocity(30,0);
            }
        });
        tempBluTeam.setCallback(new MenuTools.OnClick(){
            @Override
            public void action() {
                cl.secureSend(new MultiplayerTools.ClientPickedTeam(1));
                tempRedTeam.setVelocity(-30,0);
                tempBluTeam.setVelocity(30,0);
            }
        });
        tempRedTeam.setPressedTexture(MenuTools.mGTR("Desktop.jpg",getManager()));//new TextureRegion((Texture)getManager().get("desktop.jpg")));
        tempRedTeam.setUnpressedTexture(MenuTools.mGTR("menubackdrops/canvas.png",getManager()));
        tempBluTeam.setPressedTexture(MenuTools.mGTR("4k-image-santiago.jpg",getManager()));
        tempBluTeam.setUnpressedTexture(MenuTools.mGTR("4914003-galaxy-wallpaper-png.png",getManager()));
        super.addButton(tempRedTeam);
        super.addButton(tempBluTeam);
    }
}
