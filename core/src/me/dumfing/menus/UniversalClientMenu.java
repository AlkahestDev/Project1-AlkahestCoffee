package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import me.dumfing.client.maingame.MainGame;
import me.dumfing.gdxtools.MenuTools;

import static me.dumfing.client.maingame.MainGame.DAGGER20;
import static me.dumfing.client.maingame.MainGame.DAGGER40;

/**
 * Created by dumpl on 5/12/2017.
 */
public class UniversalClientMenu extends Menu{
    //MAINMENU objects
    MenuBox askUsernameBox, playBox, settingsBox, exitBox;
    MenuTools.TextField askUsernameField;
    MenuTools.QueueText userNameError, gameVersion, gameName;
    boolean menuButtonsOut = false;
    /**
     * Constructor for the menu
     *
     * @param bmfc    BitMapFontCache for drawing all text in the menu
     * @param manager
     * @param camera
     */
    public UniversalClientMenu(Array<BitmapFontCache> bmfc, AssetManager manager, OrthographicCamera camera) {
        super(bmfc, manager, camera);
    }

    @Override
    public void init() {
        super.setBackground(MenuTools.mGTR("4914003-galaxy-wallpaper-png.png",getManager()));
        setupMainMenuButtons();
        askUsernameBox = new MenuBox(Gdx.graphics.getWidth()/2-180,Gdx.graphics.getHeight()/2-70,360,140,super.getFonts());
        askUsernameField = new MenuTools.TextField(5, 5, 350, 40);
        userNameError = new MenuTools.QueueText(5,60,0,0);
        askUsernameBox.setBackground(MenuTools.mGTR("menubackdrops/canvas.png",getManager()));
        askUsernameField.setEnterAction(new MenuTools.OnEnter() {
            @Override
            public void action(String sIn) {
                int gHeight = Gdx.graphics.getHeight();
                if(sIn.contains(" ")){
                    Timer.instance().clear(); // prevents the previous timer from clearing this text prematurely
                    userNameError.setText("[RED]Username cannot contain spaces!",getFonts());
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            userNameError.clearText();
                        }
                    },2);
                }
                else if(sIn.length() == 0){
                    Timer.instance().clear(); // prevents the previous timer from clearing this text prematurely
                    userNameError.setText("[RED]Username cannot be empty!",getFonts());
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
                    askUsernameBox.setVelocity(0, vY);
                    //askUserNameBox.moveTo(950,-20);
                    askUsernameField.setEnterAction(new MenuTools.OnEnter() {
                        public void action(String sIn) {
                        }
                    }); // set the action to do nothing so you can't reset the velocity
                    MainGame.clientSoldier.setName(sIn); // At the current state you can use colourmarkup to change the colour of your username. I'm keeping this in because it looks cool
                    //I might have to remove the ColourMarkup if it messes with calculating lengths of things
                    System.out.println("Username: " + sIn);
                    moveMainMenuButtons();
                }
            }
        });
        userNameError.setFont(DAGGER20);
        String tempMessage = "Enter a username";
        MenuTools.QueueText tempQt = new MenuTools.QueueText(180-MenuTools.textWidth(super.getFonts().get(DAGGER40).getFont(),tempMessage)/2,120,0,0);
        tempQt.setText(tempMessage,super.getFonts());
        askUsernameBox.addQueueText(tempQt);
        askUsernameBox.addTextField(askUsernameField);
        askUsernameBox.addQueueText(userNameError);
        super.addMenuBox(askUsernameBox);
    }

    @Override
    public void update() {
        //System.out.println(playBox.getvX());
        //playBox.translate(playBox.getvX(),0);
        //System.out.println(playBox.getRect().x);
        super.update();
    }

    public void moveMainMenuButtons(){
        if(menuButtonsOut){
            playBox.setVelocity(-19.862f,0);
            settingsBox.setVelocity(-22.21f,0);
            exitBox.setVelocity(-24.32f,0);
        }
        else{
            playBox.setVelocity(19.862f,0);
            settingsBox.setVelocity(22.21f,0);
            exitBox.setVelocity(24.32f,0);
        }
    }
    public void setupMainMenuButtons(){
        playBox = new MenuBox(-400,Gdx.graphics.getHeight()/2+85,400,150,super.getFonts());
        settingsBox = new MenuBox(-500,Gdx.graphics.getHeight()/2-75,400,150,super.getFonts());
        exitBox = new MenuBox(-600,Gdx.graphics.getHeight()/2-235,400,150,super.getFonts());
        MenuTools.Button playButton = new MenuTools.Button(0,0,400,150);
        MenuTools.Button settingsButton = new MenuTools.Button(0,0,400,150);
        MenuTools.Button exitButton = new MenuTools.Button(0,0,400,150);
        MenuTools.QueueText playText = new MenuTools.QueueText(200- MenuTools.textWidth(super.getFonts().get(DAGGER40).getFont(),"Play")/2,75,0,0);
        MenuTools.QueueText settingsText = new MenuTools.QueueText(200- MenuTools.textWidth(super.getFonts().get(DAGGER40).getFont(),"Settings")/2,75,0,0);
        MenuTools.QueueText exitText = new MenuTools.QueueText(200- MenuTools.textWidth(super.getFonts().get(DAGGER40).getFont(),"Quit")/2,75,0,0);
        playButton.setPressedTexture(MenuTools.mGTR("volcano-30238.png",getManager()));
        playButton.setUnpressedTexture(MenuTools.mGTR("4k-image-santiago.jpg",getManager()));
        playText.setText("[WHITE]Play",getFonts());
        playBox.addButton(playButton);
        playBox.addQueueText(playText);
        settingsButton.setPressedTexture(MenuTools.mGTR("volcano-30238.png",getManager()));
        settingsButton.setUnpressedTexture(MenuTools.mGTR("4k-image-santiago.jpg",getManager()));
        settingsText.setText("[WHITE]Settings",getFonts());
        settingsBox.addButton(settingsButton);
        settingsBox.addQueueText(settingsText);
        exitButton.setPressedTexture(MenuTools.mGTR("volcano-30238.png",getManager()));
        exitButton.setUnpressedTexture(MenuTools.mGTR("4k-image-santiago.jpg",getManager()));
        exitText.setText("[WHITE]Quit",getFonts());
        exitButton.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                Gdx.app.exit();
            }
        });
        exitBox.addButton(exitButton);
        exitBox.addQueueText(exitText);
        super.addMenuBox(playBox);
        super.addMenuBox(settingsBox);
        super.addMenuBox(exitBox);
    }
    public void setupServerBrowserButtons(){

    }
}
