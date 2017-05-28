package me.dumfing.cleanMenu;

import com.badlogic.gdx.graphics.Texture;
import java.util.*;

public class CleanMenu {

    // Variables
    MenuState state;
    public HashMap<String, MenuItem> items = new HashMap<String, MenuItem>();

    // Constructor
    public CleanMenu(){

    }

    // Methods
    public void create(MenuState phase){

        state = phase;

        if (phase == MenuState.MAIN){
            createMain();
        }

    }

    public void update(){
        if (state == MenuState.MAIN){
            updateMain();
        }
    }


    void createMain(){
        items.clear();

        items.put("testImage", new MenuItem(ItemType.IMAGE, new Texture("badAlkahest.png"), 20, 20));

    }
    void updateMain(){

        // Handles everything regarding moving objects and clicking buttons

        items.get("testImage").x += 0.5;

    }


}
