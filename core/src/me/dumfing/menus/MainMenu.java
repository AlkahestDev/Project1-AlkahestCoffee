package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.client.maingame.GameState;
import me.dumfing.client.maingame.MainGame;

import static me.dumfing.client.maingame.MainGame.DAGGER20;
import static me.dumfing.client.maingame.MainGame.DAGGER40;
import static me.dumfing.client.maingame.MainGame.DAGGER50;

/**
 * Created by dumpl on 4/20/2017.
 */
public class MainMenu extends Menu{
    //private AssetManager manager;
    public MainMenu(Array<BitmapFontCache> bmfc, AssetManager assetManager, OrthographicCamera camera) {
        super(bmfc,assetManager, camera);
        //this.manager = assetManager;
    }

    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        super.draw(sb, sr);
        sb.begin();
            this.spriteDraw(sb);
        sb.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
            this.shapeDraw(sr);
        sr.end();
        sb.begin();
            this.fontDraw(sb);
        sb.end();
    }

    public void init(){
        this.setBackground(new TextureRegion((Texture)super.getManager().get("4914003-galaxy-wallpaper-png.png")));
        final MenuBox askUserNameBox = new MenuBox(Gdx.graphics.getWidth()/2-180,Gdx.graphics.getHeight()/2-70,360,140,super.getFonts());
        final MenuTools.TextField askUserNameField = new MenuTools.TextField(5, 5, 350, 40);
        final MenuTools.QueueText userNameError = new MenuTools.QueueText(5,60,0,0);
        askUserNameField.setEnterAction(new MenuTools.OnEnter() {
            @Override
            public void action(String sIn) {
                int gHeight = Gdx.graphics.getHeight();
                if(sIn.contains(" ")){
                    Timer.instance().clear(); // prevents the previous timer from clearing this text prematurely
                    userNameError.setText("[RED]Username cannot contain spaces!",MainMenu.super.getFonts());
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            userNameError.clearText();
                        }
                    },2);
                }
                else if(sIn.length() == 0){
                    Timer.instance().clear(); // prevents the previous timer from clearing this text prematurely
                    userNameError.setText("[RED]Username cannot be empty!",MainMenu.super.getFonts());
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            userNameError.clearText();
                        }
                    },2);
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
                    MainGame.clientSoldier.setName(sIn); // At the current state you can use colourmarkup to change the colour of your username. I'm keeping this in because it looks cool
                    //I might have to remove the ColourMarkup if it messes with calculating lengths of things
                    System.out.println("Username: " + sIn);
                    addMenuButtons();
                }
            }
        });
        userNameError.setFont(DAGGER20);
        askUserNameBox.addQueueText(userNameError);
        askUserNameBox.addTextField(askUserNameField);
        askUserNameBox.setBackground(new TextureRegion((Texture)super.getManager().get("menubackdrops/canvas.png")));
        String tempMessage = "Enter a username";
        MenuTools.QueueText tempQt = new MenuTools.QueueText(180-MenuTools.textWidth(super.getFonts().get(DAGGER40).getFont(),tempMessage)/2,120,0,0);
        tempQt.setText(tempMessage,super.getFonts());
        askUserNameBox.addQueueText(tempQt);
        super.addMenuBox(askUserNameBox);
    }

    private void addMenuButtons(){
        MenuBox playBox = new MenuBox(-400,Gdx.graphics.getHeight()/2+85,400,150,super.getFonts());
        MenuBox settingsBox = new MenuBox(-500,Gdx.graphics.getHeight()/2-75,400,150,super.getFonts());
        MenuBox quitBox = new MenuBox(-600,Gdx.graphics.getHeight()/2-235,400,150,super.getFonts());
        MenuTools.Button playButton = new MenuTools.Button(0,0,400,150);
        MenuTools.Button settingsButton = new MenuTools.Button(0, 0, 400, 150);
        MenuTools.Button quitButton = new MenuTools.Button(0, 0,400,150);
        playButton.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                MainGame.state = GameState.State.SERVERBROWSER;
            }
        });
        settingsButton.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                MainGame.state = GameState.State.MAINMENUSETTINGS;
            }
        });
        quitButton.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                MainGame.state = GameState.State.QUIT;
            }
        });
        playButton.setPressedTexture(new TextureRegion((Texture) super.getManager().get("4k-image-santiago.jpg")));
        playButton.setUnpressedTexture(new TextureRegion((Texture)super.getManager().get("volcano-30238.png")));
        settingsButton.setPressedTexture(new TextureRegion((Texture) super.getManager().get("4k-image-santiago.jpg")));
        settingsButton.setUnpressedTexture(new TextureRegion((Texture)super.getManager().get("volcano-30238.png")));
        quitButton.setPressedTexture(new TextureRegion((Texture) super.getManager().get("4k-image-santiago.jpg")));
        quitButton.setUnpressedTexture(new TextureRegion((Texture)super.getManager().get("volcano-30238.png")));
        settingsBox.addButton(settingsButton);
        playBox.addButton(playButton);
        quitBox.addButton(quitButton);
        MenuTools.QueueText playText, settingsText, quitText;
        playText = new MenuTools.QueueText(200- MenuTools.textWidth(super.getFonts().get(DAGGER40).getFont(),"Play")/2,75,0,0);
        settingsText = new MenuTools.QueueText(200-MenuTools.textWidth(super.getFonts().get(DAGGER40).getFont(),"Settings")/2,75,0,0);
        quitText = new MenuTools.QueueText(200-MenuTools.textWidth(super.getFonts().get(DAGGER40).getFont(),"Quit")/2,75,0,0);
        playText.setText("[WHITE]Play",getFonts());
        settingsText.setText("[WHITE]Settings",getFonts());
        quitText.setText("[WHITE]Quit",getFonts());
        playBox.addQueueText(playText);
        settingsBox.addQueueText(settingsText);
        quitBox.addQueueText(quitText);
        MenuTools.QueueText gameName = new MenuTools.QueueText(-415,Gdx.graphics.getHeight()-30,0,0);
        gameName.setText("[WHITE]Alkahest Coffee Corp",super.getFonts());
        gameName.setFont(DAGGER50);
        gameName.setVelocity(22.7f,0);
        super.addQueueText(gameName);
        MenuTools.QueueText versionNumber = new MenuTools.QueueText(-415, Gdx.graphics.getHeight()-70,0,0);
        versionNumber.setText("[WHITE]Version: "+MainGame.versionNumber,getFonts());
        versionNumber.setFont(DAGGER20);
        versionNumber.setVelocity(14.6f,0);
        super.addQueueText(versionNumber);
        playBox.setVelocity(19.862f,0);
        settingsBox.setVelocity(22.21f,0);
        quitBox.setVelocity(24.32f,0);
        super.addMenuBox(settingsBox);
        super.addMenuBox(playBox);
        super.addMenuBox(quitBox);
    }
}
