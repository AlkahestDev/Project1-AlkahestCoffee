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
 * The Lobby the clients will stay in while waiting for more people to connect
 */
public class ClientLobbyMenu extends Menu{
    /**
     * Constructor for the menu
     *
     * @param bmfc    BitMapFontCache for drawing all text in the menu
     * @param manager
     * @param camera
     */
    public ClientLobbyMenu(Array<BitmapFontCache> bmfc, AssetManager manager, OrthographicCamera camera) {
        super(bmfc, manager, camera);
    }

    public void init(final MultiplayerClient client) {
        MenuTools.TextField sendChatMessage = new MenuTools.TextField(Gdx.graphics.getWidth()-305,45,400,40);
        sendChatMessage.setEnterAction(new MenuTools.OnEnter() {
            @Override
            public void action(String sIn) {
                client.quickSend(new MultiplayerTools.ClientSentChatMessage(sIn));
            }
        });

        super.addTextField(sendChatMessage);
    }

}
