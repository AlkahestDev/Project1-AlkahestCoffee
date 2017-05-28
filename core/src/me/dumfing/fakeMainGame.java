package me.dumfing;

// A class just to test stuff out in, without having to worry about loading things up etc, before they go in the actual game.

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import me.dumfing.cleanMenu.*;

public class fakeMainGame extends ApplicationAdapter {


    SpriteBatch batch;
    Texture img;
    CleanMenu menu = new CleanMenu();

    @Override
    public void create () {
        batch = new SpriteBatch();
        img = new Texture("badAlkahest.png");

        // Creating the menu
        menu.create(MenuState.MAIN);

    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.draw(img, 0, 0);


        // Rendering Menu
        for (String key : menu.items.keySet()){
            MenuItem item = menu.items.get(key);
            batch.draw(item.texture, item.x, item.y);
        }
        menu.update();

        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }
}
