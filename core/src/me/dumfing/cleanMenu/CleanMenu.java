package me.dumfing.cleanMenu;

import com.badlogic.gdx.graphics.Texture;
import java.util.*;

public class CleanMenu {

    // Variables
    public ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

    // Constructor
    public CleanMenu(){

    }

    // Methods
    public void create(MenuState phase){

        if (phase == MenuState.MAIN){
            createMain();
        }

    }

    public void update(){

    }


    void createMain(){
        menuItems.clear();
        menuItems.add(new MenuItem(ItemType.IMAGE, new Texture("badAlkahest.png"), 10, 10));
    }

}
