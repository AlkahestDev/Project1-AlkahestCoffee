package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.utils.Array;
import me.dumfing.client.maingame.MainGame;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.multiplayerTools.MultiplayerClient;
import me.dumfing.multiplayerTools.MultiplayerTools;
import me.dumfing.multiplayerTools.PlayerSoldier;

import static me.dumfing.client.maingame.MainGame.DAGGER30;
import static me.dumfing.client.maingame.MainGame.DAGGER50;

/**
 * The Lobby the clients will stay in while waiting for more people to connect
 */

public class ClientLobbyMenu extends Menu{
    MenuBox chatBox, connectedPlayers, countDownBar;
    MenuTools.QueueText countdown;
    boolean barOut = false;
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
        MenuTools.TextField sendChatMessage = new MenuTools.TextField(5,5,400,40);
        sendChatMessage.setEnterAction(new MenuTools.OnEnter() {
            @Override
            public void action(String sIn) {
                if(sIn.replaceAll(" ","").length()>0) {
                    client.quickSend(new MultiplayerTools.ClientSentChatMessage(sIn));
                }
            }
        });
        chatBox = new MenuBox(Gdx.graphics.getWidth()-415,5,410,480,getFonts());
        chatBox.setBackground(MenuTools.mGTR("Menu/canvas.png",getManager()));
        chatBox.addTextField(sendChatMessage);
        super.addMenuBox(chatBox);
        connectedPlayers = new MenuBox(5,5,400,660,getFonts());
        connectedPlayers.setBackground(MenuTools.mGTR("Menu/canvas.png",getManager()));
        super.addMenuBox(connectedPlayers);
        countDownBar = new MenuBox(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()-60,200,60,getFonts());
        countDownBar.setBackground(MenuTools.mGTR("simpleBG.png",getManager()));
        countdown = new MenuTools.QueueText(5,50,0,0);
        countdown.setFont(DAGGER50);
        countDownBar.addQueueText(countdown);
        super.addMenuBox(countDownBar);
    }
    public void update(MultiplayerClient client) {
        chatBox.clearText();
        connectedPlayers.clearText();
        int textLevel = 0;
        for(String text : client.getMessages()){
            MenuTools.QueueText tempMessage = new MenuTools.QueueText(5,75+(textLevel*32),0,0);
            tempMessage.setText(text,getFonts());
            tempMessage.setFont(DAGGER30);
            chatBox.addQueueText(tempMessage);
            textLevel++;
        }
        textLevel = 0;
        for(PlayerSoldier player : client.getPlayers().values()){
            MenuTools.QueueText tempName = new MenuTools.QueueText(5,45+(textLevel*42),0,0);
            tempName.setText(player.getName(),getFonts());
            connectedPlayers.addQueueText(tempName);
            textLevel++;
        }
        if(client.getGameStarted() > -1){
            if(barOut){
                countdown.setText(""+ MainGame.client.getGameStarted(),getFonts());
            }
            else{
                countDownBar.setVelocity(-13.9f,0); // extend bar
                barOut = true;
            }
        }
        else if(barOut){ // the bar is out but doesn't show any useful info
            countDownBar.setVelocity(13.9f,0); //retract bar
            barOut = false;
        }
        super.update();
    }
}
