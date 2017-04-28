package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

/**
 * Created by dumpl on 4/21/2017.
 */
public class LoadingMenu extends Menu {
    AssetManager manager;
    public LoadingMenu(Array<BitmapFontCache> bmfc, AssetManager manager){
        super(bmfc);
        this.manager = manager;
    }

    @Override
    public void update() {
        manager.update();
        super.update();
    }

    @Override
    public void shapeDraw(ShapeRenderer sr) {
        super.shapeDraw(sr);
        sr.setColor(1,0.96f,0.89f,1);
        sr.rect(Gdx.graphics.getWidth()/2-200,20,400,30);
        sr.setColor(0,0.5f,1,1);
        sr.rect(Gdx.graphics.getWidth()/2-198,22,396f*manager.getProgress(),26);
    }
    public boolean doneLoading(){
        return this.manager.getProgress() == 1;
    }
}