package me.dumfing.client.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

/**
 * Created by dumpl on 4/21/2017.
 */
public class LoadingMenu extends Menu {
    //AssetManager manager;
    public LoadingMenu(Array<BitmapFontCache> bmfc, AssetManager manager, OrthographicCamera camera){
        super(bmfc,manager, camera);
    }

    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        sb.begin();
            this.spriteDraw(sb);
        sb.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
            this.shapeDraw(sr);
        sr.end();
        super.draw(sb, sr);
    }

    @Override
    public void update() {
        super.getManager().update();
        super.update();
    }

    @Override
    public void shapeDraw(ShapeRenderer sr) {
        super.shapeDraw(sr);
        sr.setColor(1,0.96f,0.89f,1);
        sr.rect(Gdx.graphics.getWidth()/2-200,20,400,30);
        sr.setColor(0,0.5f,1,1);
        sr.rect(Gdx.graphics.getWidth()/2-198,22,396f*super.getManager().getProgress(),26);
    }
    public boolean doneLoading(){
        return super.getManager().getProgress() == 1;
    }
}
