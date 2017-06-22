package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;


public class LoadingMenu extends Menu {
    //Refer to commented out sections for how to use the GifDecoder
    //AssetManager manager;
    //float stateTime = 0;
    //Animation newBG = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP,Gdx.files.internal("1337hax.gif").read());
    float animationTime = 0;
    TextureAtlas loadingAtlas = new TextureAtlas(Gdx.files.internal("loading/LoadingAnimation.atlas"));
    BitmapFont ailerons = new BitmapFont(Gdx.files.internal("fonts/Ailerons90.fnt"));
    Animation loadingArcher = new Animation(0.09f,loadingAtlas.findRegions("archerLoad"), Animation.PlayMode.LOOP);
    public LoadingMenu(Array<BitmapFontCache> bmfc, AssetManager manager, OrthographicCamera camera){
        super(bmfc,manager, camera);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        super.draw(sb, sr);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(61f/255f,61f/255f,61f/255f,1);
        sr.rect(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        sr.end();
        float archerDrawWidth = 250;
        float archerDrawRatio = ((TextureRegion)loadingArcher.getKeyFrame(animationTime)).getRegionHeight()/((TextureRegion)loadingArcher.getKeyFrame(animationTime)).getRegionWidth();
        sb.begin();
        float width = MenuTools.textWidth(ailerons,"ALKAHEST");
        float height = MenuTools.textHeight(ailerons,"ALKAHEST");
        ailerons.setColor(255f/255f,180/255f,0/255f,1);
        ailerons.draw(sb,"ALKAHEST",Gdx.graphics.getWidth()/2-width/2,Gdx.graphics.getHeight()/1.7f);
        sb.draw((TextureRegion) loadingArcher.getKeyFrame(animationTime),Gdx.graphics.getWidth()/2-archerDrawWidth/2,60,archerDrawWidth, archerDrawWidth*archerDrawRatio);
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
        animationTime+=Gdx.graphics.getDeltaTime();
        super.getManager().update();
        //stateTime= (stateTime+Gdx.graphics.getDeltaTime())%newBG.getAnimationDuration();
        super.update();
    }

    @Override
    public void shapeDraw(ShapeRenderer sr) {
        super.shapeDraw(sr);
        sr.setColor(30f/255f,30f/255f,30f/255f,1);
        sr.rect(Gdx.graphics.getWidth()/2-200,20,400,30);
        sr.setColor(255/255f,180/255f,0/255f,1);
        sr.rect(Gdx.graphics.getWidth()/2-198,22,396f*super.getManager().getProgress(),26);
    }
    public boolean doneLoading(){
        return super.getManager().getProgress() == 1;
    }
}
