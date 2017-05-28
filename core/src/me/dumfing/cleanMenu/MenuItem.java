package me.dumfing.cleanMenu;

import com.badlogic.gdx.graphics.*;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.menus.Menu;

public class MenuItem {

    ItemType type;

    public Texture mainTexture;
    public float x;
    public float y;

    // Button Stuff
    public boolean pressed;
    public Texture texture;
    public Texture texture2;
    OnClick action;
    public interface OnClick{void action();}

    // Image Constructor
    MenuItem(ItemType type, Texture texture, float x, float y){

        this.type = type;
        this.mainTexture = texture;
        this.x = x;
        this.y = y;

    }

    // Button Constructor
    MenuItem(ItemType type, Texture texture, Texture texture2, float x, float y, OnClick action){

        this.type = type;
        this.texture = texture;
        this.texture2 = texture2;
        mainTexture = this.texture;
        this.x = x;
        this.y = y;
        pressed = false;
        this.action = action;

    }

    void activate(){
        action.action();
    }

    boolean collidePoint(float cX, float cY){
        return x <= cX && x + texture.getWidth() >= cX && y <= cY && y + texture.getHeight() >= y;
    }

}
