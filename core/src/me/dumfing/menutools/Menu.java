package me.dumfing.menutools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.DrawTools;

/**
 * Created by dumpl on 4/21/2017.
 */
public class Menu implements InputProcessor{
    private Array<MenuTools.Button> buttons;
    int backgroundFrame, frameTime, frameCount; // allows for animated backgrounds
    boolean animatedBackground;
    Array<TextureRegion> background; // all images used for background
    Array<Sprite> images = new Array<Sprite>(); // sprites for images, they can have their own position and texture
    public Menu(){
        buttons = new Array<MenuTools.Button>();
        backgroundFrame = 0;
        background = new Array<TextureRegion>();
        images = new Array<Sprite>();
    }
    public void setInputProcessor(){
        Gdx.input.setInputProcessor(this);
    }
    public void addButton(MenuTools.Button btIn){
        this.buttons.add(btIn);
    }
    public void update(){
        for(MenuTools.Button bt : buttons){ //check all the buttons if they're currently pressed and the mouse is hovering over them
            bt.setPressed(bt.collidepoint(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY()) && Gdx.input.isButtonPressed(0));
        }
        if(this.animatedBackground){
            if(this.frameCount%this.frameTime == 0){
                this.backgroundFrame = (this.backgroundFrame+1)%this.background.size;
            }
        }
        else{
            this.backgroundFrame = 0;
        }
        this.frameCount++;
    }
    public void draw(SpriteBatch sb, ShapeRenderer sr, boolean debug){
        sb.draw(background.get(backgroundFrame),0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        if(debug){
            for(MenuTools.Button bt : this.buttons){
                if(bt.getClicked()){
                    sr.setColor(Color.BLUE);
                    DrawTools.rec(sr,bt.getButtonArea());
                }
                else {
                    sr.setColor(Color.RED);
                    DrawTools.rec(sr, bt.getButtonArea());
                }
            }
        }
        else{
            for(MenuTools.Button bt : this.buttons){
                if(bt.getClicked()){
                    sb.draw(bt.pressed,bt.getButtonArea().x,bt.getButtonArea().y);
                }
                else {
                    sb.draw(bt.unpressed,bt.getButtonArea().x,bt.getButtonArea().y);
                }
            }
        }
        for(Sprite sp : images){
            sp.draw(sb);
        }
    }
    public void draw(SpriteBatch sb, ShapeRenderer sr){
        this.draw(sb,sr,false);
    }
    public void addImage(Sprite imgIn){ // add a sprite for drawing over buttons
        this.images.add(imgIn);
    }
    public void addImage(Texture img, int x, int y){ // add the components of a sprite
        Sprite temp = new Sprite(img);
        temp.setPosition(x,y);
        addImage(temp);
    }
    public void setBackground(TextureRegion bg){
        if(this.background.size == 0){
            this.background.add(bg);
        }
        else {
            this.background.set(0, bg);
        }
        this.animatedBackground = false;
    }
    public void addBackground(TextureRegion bg){
        this.background.add(bg);
        this.animatedBackground = true;
    };
    public void setFrameTime(int fps){
        int updatesPerFrame = 60/fps;
        this.frameTime = updatesPerFrame;
    }
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight() - screenY; // invert screenY because screenY is from top of screen
        for(MenuTools.Button bt : buttons){ // check all buttons
            bt.setPressed(false); // since mouse is being unpressed, all buttons can be set to unpressed
            if(bt.collidepoint(screenX,screenY)){ // if the mouse is hovering over a button
                bt.activate(); // activate that button's function
            }
        }
        return false;
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
