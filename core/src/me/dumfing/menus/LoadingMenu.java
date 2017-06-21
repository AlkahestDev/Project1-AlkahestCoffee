package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;


public class LoadingMenu extends Menu {
    //Refer to commented out sections for how to use the GifDecoder
    //AssetManager manager;
    //float stateTime = 0;
    //Animation newBG = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP,Gdx.files.internal("1337hax.gif").read());
    public LoadingMenu(Array<BitmapFontCache> bmfc, AssetManager manager, OrthographicCamera camera){
        super(bmfc,manager, camera);

    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        super.draw(sb, sr);
        sb.begin();
        this.spriteDraw(sb);
        sb.end();
        //sb.begin();
        //sb.draw((TextureRegion) newBG.getKeyFrame(stateTime),0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //sb.end();
        sr.begin(ShapeRenderer.ShapeType.Filled);
        this.shapeDraw(sr);
        sr.end();
    }

    @Override
    public void update() {
        super.getManager().update();
        //stateTime= (stateTime+Gdx.graphics.getDeltaTime())%newBG.getAnimationDuration();
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
