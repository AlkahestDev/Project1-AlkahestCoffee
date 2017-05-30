package me.dumfing;

// A class just to test stuff out in, without having to worry about loading things up etc, before they go in the actual game.

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import me.dumfing.cleanMenu.*;

public class fakeMainGame extends ApplicationAdapter {


    Viewport viewport;
    SpriteBatch batch;
    Texture img;
    CleanMenu menu = new CleanMenu();

    @Override
    public void create () {

        viewport = new FitViewport(1080, 720, new PerspectiveCamera());

        batch = new SpriteBatch();
        img = new Texture("badAlkahest.png");

        // Creating the menu
        menu.create(MenuState.MAIN);
        Gdx.input.setInputProcessor(menu);

    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.draw(img, 0, 0);


        // Rendering Menu
        for (String key : menu.items.keySet()){
            MenuItem item = menu.items.get(key);
            batch.draw(item.mainTexture, item.x, item.y);
        }
        menu.update();

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Called when the viewport is scaled
        viewport.update(width, height);
    }

    @Override
    public void dispose () {
        batch.dispose();
        img.dispose();
    }



}
