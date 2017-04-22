package me.dumfing.menutools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import me.dumfing.gdxtools.DrawTools;

/**
 * Created by dumpl on 4/20/2017.
 */
public class MenuTools {
    public interface OnClick{
        void action();
    }
    public static class Button{
        TextureRegion unpressed, pressed; // how the button looks when it's held down and when it's not held down
        private Rectangle buttonArea;
        boolean isPressed = false;
        OnClick callback;
        public Button(float x, float y, float width, float height,OnClick callback){
            buttonArea = new Rectangle(x,y,width,height);
            this.callback = callback;
        }
        public void setPressed(boolean pressed){
            this.isPressed = pressed;
        }
        public void activate(){
            callback.action();
        }
        public boolean collidepoint(float mX, float mY){
            return this.buttonArea.contains(mX, mY);
            //return isPressed;
        }
        public Rectangle getButtonArea(){
            return this.buttonArea;
        }
        public boolean getClicked(){
            return this.isPressed;
        }
        public void draw(ShapeRenderer sr, Batch batch, boolean debug){
            if(debug){
                if(this.isPressed) {
                    sr.setColor(Color.RED);
                    DrawTools.rec(sr, this.buttonArea);
                }
                else{
                    sr.setColor(Color.BLUE);
                    DrawTools.rec(sr,this.buttonArea);
                }
            }
            else{
                if(this.isPressed){
                    batch.draw(pressed,buttonArea.getX(),buttonArea.getY());
                }
                else{
                    batch.draw(unpressed,buttonArea.getX(),buttonArea.getY());
                }
            }
        }
        public void setPressedTexture(TextureRegion pressedTexture){
            this.pressed = pressedTexture;
        }
        public void setUnpressedTexture(TextureRegion unpressedTexture){
            this.unpressed = unpressedTexture;
        }
        public void draw(ShapeRenderer sr){
            this.draw(sr,null,true);
        }
        public void draw(Batch batch){
            this.draw(null,batch,false);
        }

    }
}
