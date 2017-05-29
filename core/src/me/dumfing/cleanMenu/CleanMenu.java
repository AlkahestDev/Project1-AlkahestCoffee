package me.dumfing.cleanMenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import java.util.*;

public class CleanMenu implements InputProcessor{

    // Variables
    MenuState state;
    public HashMap<String, MenuItem> items = new HashMap<String, MenuItem>();

    // Constructor
    public CleanMenu(){}

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


    // Menu Phases
    private void createMain(){
        // Creates all the MenuItems to be displayed
        items.clear();

        items.put("testImage", new MenuItem(ItemType.IMAGE, new Texture("badAlkahest.png"), 20, 20));
        items.put("testButton", new MenuItem(ItemType.BUTTON, new Texture("badAlkahest.png"), new Texture("badAlkahest2.png"),
                40, 40, new MenuItem.OnClick() {
            @Override
            public void action() {
                System.out.println("HIT");
            }
        }));

    }
    private void updateMain(){
        // Handles everything regarding moving objects
        items.get("testImage").x += 0.5;
    }




    // InputProcessor Methods
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight() - screenY; // invert screenY because screenY is from top of screen
        for (String key : items.keySet()){
            MenuItem item = items.get(key);
            if (item.type == ItemType.BUTTON){

                if (item.collidePoint(screenX, screenY)){
                    item.mainTexture = item.texture2;
                }
            }
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight() - screenY; // invert screenY because screenY is from top of screen
        for (String key : items.keySet()){
            MenuItem item = items.get(key);
            if (item.type == ItemType.BUTTON){
                if (item.collidePoint(screenX, screenY)){
                    item.activate();
                }
                item.mainTexture = item.texture;
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
