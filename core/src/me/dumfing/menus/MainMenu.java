package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.multishooter.MainShooter;

/**
 * Created by dumpl on 4/20/2017.
 */
public class MainMenu extends Menu{

    public MainMenu(BitmapFontCache bmfc) {
        super(bmfc);
        final MenuTools.TextBox askUserName =  new MenuTools.TextBox(Gdx.graphics.getWidth()/2-150, Gdx.graphics.getHeight()/2-20, 300, 40);
        askUserName.setEnterAction(new MenuTools.OnEnter() {
            public void action(String tIn) {
                int gHeight = Gdx.graphics.getHeight();
                if(gHeight<=660){
                    askUserName.setVelocity(0, -18.4f);
                }
                else {
                    askUserName.setVelocity(0, -(18.5f + (gHeight - 660) / 80f));
                }
                askUserName.setEnterAction(new MenuTools.OnEnter() {public void action(String sIn) {}}); // set the action to do nothing so you can't reset the velocity
                MainShooter.clientSoldier.setName(tIn);
                //MainShooter.player.connectToServer(tIn);
                //MainMenu.super.removeTextBox(askUserName);
            }
        });
        super.addTextBox(askUserName);
    }
}
