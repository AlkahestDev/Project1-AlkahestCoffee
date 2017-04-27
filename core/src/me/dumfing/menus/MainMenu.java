package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.maingame.GameState;
import me.dumfing.maingame.MainGame;

/**
 * Created by dumpl on 4/20/2017.
 */
public class MainMenu extends Menu{
    private AssetManager manager;
    public MainMenu(BitmapFontCache bmfc, AssetManager assetManager) {
        super(bmfc);
        this.manager = assetManager;
    }
    @Override
    public void init(AssetManager am, BitmapFontCache bmfc){
        this.setBackground(new TextureRegion((Texture)am.get("4914003-galaxy-wallpaper-png.png")));
        final MenuBox askUserNameBox = new MenuBox(Gdx.graphics.getWidth()/2-165,Gdx.graphics.getHeight()/2-65,330,110,bmfc);
        final MenuTools.TextField askUserNameField = new MenuTools.TextField(5, 5, 320, 40);
        askUserNameField.setEnterAction(new MenuTools.OnEnter() {
            @Override
            public void action(String sIn) {
                int gHeight = Gdx.graphics.getHeight();
                float vY = -18.4f;
                if(gHeight >=660) {
                    vY = -(20f + (gHeight - 660) / 60f);
                }
                askUserNameBox.setVelocity(0,vY);
                askUserNameField.setEnterAction(new MenuTools.OnEnter() {public void action(String sIn) {}}); // set the action to do nothing so you can't reset the velocity
                MainGame.clientSoldier.setName(sIn);
                System.out.println("Username: "+sIn);
                addMenuButtons();
            }
        });
        askUserNameBox.addTextField(askUserNameField);
        askUserNameBox.setBackground(new TextureRegion((Texture)am.get("menubackdrops/canvas.png")));
        String tempMessage = "Pick a username";
        MenuTools.QueueText tempQt = new MenuTools.QueueText(165-MenuTools.textWidth(bmfc.getFont(),tempMessage)/2,90,0,0);
        tempQt.setText(tempMessage,bmfc);

        askUserNameBox.addQueueText(tempQt);
        super.addMenuBox(askUserNameBox);
    }

    private void addMenuButtons(){
        MenuTools.Button playButton = new MenuTools.Button(-300,450,300,70);
        MenuTools.Button settingsButton = new MenuTools.Button(-400, 350, 300, 70);
        MenuTools.Button quitButton = new MenuTools.Button(-500, 250,300,70);
        playButton.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                MainGame.state = GameState.SERVERBROWSER;
            }
        });
        playButton.setVelocity(17.2f,0);
        settingsButton.setVelocity(19.862f,0);
        quitButton.setVelocity(22.21f,0);
        playButton.setPressedTexture(new TextureRegion((Texture) manager.get("4k-image-santiago.jpg")));
        playButton.setUnpressedTexture(new TextureRegion((Texture)manager.get("Desktop.jpg")));
        settingsButton.setPressedTexture(new TextureRegion((Texture) manager.get("4k-image-santiago.jpg")));
        settingsButton.setUnpressedTexture(new TextureRegion((Texture)manager.get("Desktop.jpg")));
        quitButton.setPressedTexture(new TextureRegion((Texture) manager.get("4k-image-santiago.jpg")));
        quitButton.setUnpressedTexture(new TextureRegion((Texture)manager.get("Desktop.jpg")));
        super.addButton(settingsButton);
        super.addButton(playButton);
        super.addButton(quitButton);
    }
}
