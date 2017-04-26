package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.maingame.MainGame;

/**
 * Created by dumpl on 4/20/2017.
 */
public class MainMenu extends Menu{
    public MainMenu(BitmapFontCache bmfc) {
        super(bmfc);
        final MenuTools.ColourRect askUserNameBack = new MenuTools.ColourRect(Gdx.graphics.getWidth()/2-163,Gdx.graphics.getHeight()/2-83,326,166,1,0.96f,0.89f,1);
        final MenuTools.ColourRect askUserNameBorder = new MenuTools.ColourRect(Gdx.graphics.getWidth()/2-165, Gdx.graphics.getHeight()/2 - 85, 330,170,0,0,0,1);
        final MenuTools.TextField askUserName =  new MenuTools.TextField(Gdx.graphics.getWidth()/2-150, Gdx.graphics.getHeight()/2-70, 300, 40);
        askUserName.setEnterAction(new MenuTools.OnEnter() {
            public void action(String tIn) {
                int gHeight = Gdx.graphics.getHeight();
                float vY = -18.4f;
                if(gHeight >=660) {
                    vY = -(20f + (gHeight - 660) / 60f);
                }
                askUserName.setVelocity(0,vY);
                askUserNameBack.setVelocity(0,vY);
                askUserNameBorder.setVelocity(0,vY);
                askUserName.setEnterAction(new MenuTools.OnEnter() {public void action(String sIn) {}}); // set the action to do nothing so you can't reset the velocity
                MainGame.clientSoldier.setName(tIn);
                //MainShooter.player.connectToServer(tIn);
                //MainMenu.super.removeTextBox(askUserName);
            }
        });
        //super.addColRect(askUserNameBorder);
        //super.addColRect(askUserNameBack);
        //super.addTextBox(askUserName);
    }
}
