package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import me.dumfing.menutools.Menu;
import me.dumfing.menutools.MenuTools;

/**
 * Created by dumpl on 4/20/2017.
 */
public class MainMenu extends Menu{
    Array<MenuTools.TextBox> textBoxes;
    BitmapFontCache textCache;
    MenuTools.TextBox focused; // the textbox that the user will be typing into
    public MainMenu(BitmapFontCache bmfc) {
        textBoxes = new Array<MenuTools.TextBox>();
        textBoxes.add(new MenuTools.TextBox(400,60,300,40));
        this.textCache = bmfc;
    }

    @Override
    public void shapeDraw(ShapeRenderer sr) {
        for(MenuTools.TextBox tb : textBoxes){
            tb.draw(sr,textCache.getFont(),tb == focused);
        }
        super.shapeDraw(sr);
    }

    @Override
    public void spriteDraw(SpriteBatch sb) {
        super.spriteDraw(sb);
    }

    @Override
    public void update() {
        for(MenuTools.TextBox tb : textBoxes){
            tb.update(textCache,focused ==tb);
        }
        super.update();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight()-screenY;
        for(MenuTools.TextBox tb : textBoxes){
            if(tb.collidePoint(screenX,screenY)){
                focused = tb;
                return super.touchDown(screenX,screenY,pointer,button);
            }
        }
        focused = null;
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(focused!=null) {
            focused.keyDown(keycode);
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if(focused!=null) {
            focused.keyUp(keycode);
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        if(focused!=null) {
            focused.keyTyped(character);
        }
        return super.keyTyped(character);
    }
}
