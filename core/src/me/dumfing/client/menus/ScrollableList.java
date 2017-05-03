package me.dumfing.client.menus;
//FILENAME
//Aaron Li  4/28/2017
//EXPLAIN

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import me.dumfing.client.gdxtools.MenuObject;
import me.dumfing.client.gdxtools.MenuTools;

import static me.dumfing.client.maingame.MainGame.DAGGER40;

public class ScrollableList extends MenuObject{
    float scrollHeight = 0;
    int font = DAGGER40;
    Array<ScrollableListElement> elements;
    public static class ScrollableListElement{
        private MenuTools.OnClick whenSelected;
        private String display;
        public ScrollableListElement(String display, MenuTools.OnClick whenSelected){
            this.whenSelected = whenSelected;
            this.display = display;
        }
        public void spriteDraw(SpriteBatch sb, BitmapFont fontToDrawWith){

        }
    }
    public ScrollableList(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

}
