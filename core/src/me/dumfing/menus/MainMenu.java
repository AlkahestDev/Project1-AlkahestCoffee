package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.maingame.GameState;
import me.dumfing.maingame.MainGame;

/**
 * Created by dumpl on 4/20/2017.
 */
public class MainMenu extends Menu{
    public MainMenu(final BitmapFontCache bmfc, final AssetManager assetManager) {
        super(bmfc);
    }
    @Override
    public void init(AssetManager am, BitmapFontCache bmfc){
        this.setBackground(new TextureRegion((Texture)am.get("4914003-galaxy-wallpaper-png.png")));
        MenuBox askUserNameBox = new MenuBox(Gdx.graphics.getWidth()/2-165,Gdx.graphics.getHeight()/2-85,330,170,bmfc);
        askUserNameBox.addTextField(new MenuTools.TextField(5, 5, 320, 40, new MenuTools.OnEnter() {
            @Override
            public void action(String sIn) {
                System.out.println(sIn);
            }
        }));
        askUserNameBox.setBackground(new TextureRegion((Texture)am.get("menubackdrops/canvas.png")));
        super.addMenuBox(askUserNameBox);
    }
        //super.addColRect(askUserNameBorder);
        //super.addColRect(askUserNameBack);
        //super.addTextBox(askUserName);
}
