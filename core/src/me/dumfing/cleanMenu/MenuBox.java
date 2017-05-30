package me.dumfing.cleanMenu;


import com.badlogic.gdx.graphics.Texture;

import java.util.*;

public class MenuBox {

    HashMap<String, MenuItem> items = new HashMap<String, MenuItem>();
    int originX, originY;

    MenuBox(int originX, int originY){
        this.originX = originX;
        this.originY = originY;
    }

    // Adding a Texture
    void add(String name, ItemType type, Texture texture, float x, float y){
        items.put(name, new MenuItem(type, texture, x, y));
    }

    // Adding a Button
    void add(String name, ItemType type, Texture texture, Texture texture2, float x, float y, MenuItem.OnClick action){
        items.put(name, new MenuItem(type, texture, texture2, x, y, action));
    }

    // Moving all items as a group
    void moveAll(int x, int y){

        for (String key : items.keySet()){
            items.get(key).x += x;
            items.get(key).y += y;
        }

    }

    // Moving a specific item
    void move(String key, int x, int y){
        items.get(key).x += x;
        items.get(key).y += y;
    }





}
