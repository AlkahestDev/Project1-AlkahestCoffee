package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.maingame.GameState;
import me.dumfing.maingame.MainGame;

import java.util.HashMap;

import static me.dumfing.maingame.MainGame.DAGGER20;
import static me.dumfing.maingame.MainGame.DAGGER40;

/**
 * Created by dumpl on 4/20/2017.
 */
public class MainMenu extends Menu{
    private AssetManager manager;
    public MainMenu(Array<BitmapFontCache> bmfc, AssetManager assetManager) {
        super(bmfc);
        this.manager = assetManager;
    }

    @Override
    public void spriteDraw(SpriteBatch sb) {
        super.spriteDraw(sb);
    }

    public void init(AssetManager am){
        this.setBackground(new TextureRegion((Texture)am.get("4914003-galaxy-wallpaper-png.png")));
        final MenuBox askUserNameBox = new MenuBox(Gdx.graphics.getWidth()/2-165,Gdx.graphics.getHeight()/2-70,330,140,super.getFonts());
        final MenuTools.TextField askUserNameField = new MenuTools.TextField(5, 5, 320, 40);
        final MenuTools.QueueText userNameError = new MenuTools.QueueText(5,60,0,0);
        askUserNameField.setEnterAction(new MenuTools.OnEnter() {
            @Override
            public void action(String sIn) {
                int gHeight = Gdx.graphics.getHeight();
                if(sIn.contains(" ")){
                    userNameError.setText("[RED]Username cannot contain spaces!",MainMenu.super.getFonts());
                }
                else if(sIn.length() == 0){
                    userNameError.setText("[RED]Username cannot be empty!",MainMenu.super.getFonts());
                }
                else {
                    float vY = -18.4f;
                    if (gHeight >= 660) {
                        vY = -(20f + (gHeight - 660) / 60f);
                    }
                    askUserNameBox.setVelocity(0, vY);
                    askUserNameField.setEnterAction(new MenuTools.OnEnter() {
                        public void action(String sIn) {
                        }
                    }); // set the action to do nothing so you can't reset the velocity
                    MainGame.clientSoldier.setName(sIn);
                    System.out.println("Username: " + sIn);
                    addMenuButtons();
                }
            }
        });
        userNameError.setPreferredFont(DAGGER20);
        askUserNameBox.addQueueText(userNameError);
        askUserNameBox.addTextField(askUserNameField);
        askUserNameBox.setBackground(new TextureRegion((Texture)am.get("menubackdrops/canvas.png")));
        String tempMessage = "Pick a username";
        MenuTools.QueueText tempQt = new MenuTools.QueueText(165-MenuTools.textWidth(super.getFonts().get(DAGGER40).getFont(),tempMessage)/2,120,0,0);
        tempQt.setText(tempMessage,super.getFonts());

        askUserNameBox.addQueueText(tempQt);
        super.addMenuBox(askUserNameBox);
    }

    private void addMenuButtons(){
        MenuBox playBox = new MenuBox(-300,500,300,70,super.getFonts());
        MenuBox settingsBox = new MenuBox(-400,420,300,70,super.getFonts());
        MenuBox quitBox = new MenuBox(-500,340,300,70,super.getFonts());
        MenuTools.Button playButton = new MenuTools.Button(0,0,300,70);
        MenuTools.Button settingsButton = new MenuTools.Button(0, 0, 300, 70);
        MenuTools.Button quitButton = new MenuTools.Button(0, 0,300,70);
        playButton.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                MainGame.state = GameState.SERVERBROWSER;
            }
        });
        quitButton.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                Gdx.app.exit();
            }
        });
        playButton.setPressedTexture(new TextureRegion((Texture) manager.get("4k-image-santiago.jpg")));
        playButton.setUnpressedTexture(new TextureRegion((Texture)manager.get("Desktop.jpg")));
        settingsButton.setPressedTexture(new TextureRegion((Texture) manager.get("4k-image-santiago.jpg")));
        settingsButton.setUnpressedTexture(new TextureRegion((Texture)manager.get("Desktop.jpg")));
        quitButton.setPressedTexture(new TextureRegion((Texture) manager.get("4k-image-santiago.jpg")));
        quitButton.setUnpressedTexture(new TextureRegion((Texture)manager.get("Desktop.jpg")));
        settingsBox.addButton(settingsButton);
        playBox.addButton(playButton);
        quitBox.addButton(quitButton);
        playBox.setVelocity(17.2f,0);
        settingsBox.setVelocity(19.862f,0);
        quitBox.setVelocity(22.21f,0);
        super.addMenuBox(settingsBox);
        super.addMenuBox(playBox);
        super.addMenuBox(quitBox);
    }
}
