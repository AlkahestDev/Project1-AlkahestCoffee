package me.dumfing.cleanMenu;

import com.badlogic.gdx.graphics.*;
import me.dumfing.menus.Menu;

public class MenuItem {

    ItemType type;

    public Texture texture;
    public float x;
    public float y;


    // Image Constructor
    MenuItem(ItemType type, Texture texture, float x, float y){

        this.type = type;
        this.texture = texture;
        this.x = x;
        this.y = y;

    }

    // Button Constructor ToDo
    MenuItem(ItemType type, Texture texture, Texture texure2, float x, float y, int action){

        this.type = type;
        this.texture = texture;
        this.x = x;
        this.y = y;

    }


}
