package me.dumfing.menutools;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import me.dumfing.gdxtools.DrawTools;

import java.util.Arrays;

/**
 * TODO: textbox arrow keys, deleting, and getting text
 * MenuTools.java
 * Various elements used in menus
 * So far there is a button and textbox(WIP)
 */
public class MenuTools {
    public static final int keyTime = 7; // number of frames before a key is registered again
    private static final String legalChars = " `~1!2@3#4$5%6^7&8*9(0)-_=+qwertyuiopasdfghjklzxcvbnm,<.>/?;:'\"\\|][}{";
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
    public static class TextBox{
        Rectangle boxShape;
        StringBuilder sOut;
        int curPos, frameCount;
        int[] heldKeys; // shift everything up by one because anykey is -1
        boolean showCursor;
        public TextBox(Rectangle bShape){
            this.boxShape = bShape;
            initVars();
        }
        public TextBox(float x, float y, float width, float height){
            this.boxShape = new Rectangle(x,y,width,height);
            initVars();
        }
        public void update(BitmapFontCache ch, boolean focused){
            handleHeldKeys();
            ch.setColor(Color.BLACK);
            ch.addText(this.sOut.toString(),this.boxShape.getX()+3,this.boxShape.getY()+this.boxShape.getHeight(),0,this.sOut.length(),this.boxShape.getWidth(), Align.left,false,"");
            if(focused){
                this.frameCount++; // Integer.MAX_VALUE frames is around 414 days to overflow, if a user has the text box open for 414 days, the game probably won't be worth keeping open
            }
            else{
                this.frameCount = 0; // keeps delay consistent when refocusing on a textbox (also aids in staying away from Integer.MAX_VALUE)
            }
            //System.out.println(this.frameCount+" "+this.frameCount%30);
            if(this.frameCount%40 == 0){
                this.showCursor = !showCursor;
            }
            for(int i = 0; i<257; i++){
                if(heldKeys[i]>-1) {
                    this.heldKeys[i]++;
                }
            }
        }
        public boolean collidePoint(float mX, float mY){
            return this.boxShape.contains(mX,mY);
        }
        public void draw(ShapeRenderer sr, BitmapFont fnt, boolean focused){
            sr.setColor(Color.BLACK);
            DrawTools.rec(sr,this.boxShape);
            sr.setColor(Color.WHITE);
            sr.rect(this.boxShape.x+2,this.boxShape.y+2,this.boxShape.width-4,this.boxShape.height-4);
            sr.setColor(Color.BLACK);
            float tWidth = new GlyphLayout(fnt,this.sOut.toString().substring(0,curPos)).width;
            if(tWidth<this.boxShape.width-12 && focused && showCursor) { // for the line to be drawn, the width fo the text must be within the width of the box, and the box must be focused
                sr.rect(this.boxShape.getX() + tWidth + 5, this.boxShape.getY() + 3, 1, this.boxShape.getHeight() - 6);
            }

        }
        public void initVars(){
            this.sOut = new StringBuilder();
            this.curPos = this.frameCount = 0;
            this.heldKeys = new int[257];
            this.showCursor = false;
            Arrays.fill(this.heldKeys,-1);
        }
        public void keyDown(int keycode){
            this.heldKeys[keycode+1] = 0;
            this.showCursor = true;
            this.frameCount = 0;
        }
        public void keyUp(int keycode){
            this.heldKeys[keycode+1] = -1;
        }
        public void handleHeldKeys(){
            boolean keyPressed = false;
            if(this.heldKeys[Input.Keys.LEFT+1] % keyTime == 0){
                this.curPos = Math.max(0,this.curPos-1);
                keyPressed = true;
            }
            if(this.heldKeys[Input.Keys.RIGHT+1] % keyTime == 0){
                this.curPos = Math.min(this.sOut.length(),this.curPos+1);
                keyPressed = true;
            }
            if(this.heldKeys[Input.Keys.BACKSPACE+1] % keyTime == 0){
                if(this.curPos>0){
                    this.sOut.deleteCharAt(this.curPos-1);
                    this.curPos--;
                }
            }
            if(this.heldKeys[Input.Keys.FORWARD_DEL+1] % keyTime == 0){
                if(this.curPos<this.sOut.length()){
                    this.sOut.deleteCharAt(this.curPos);
                }
            }
            if(keyPressed){
                this.showCursor = true;
                this.frameCount = 0;
            }
        }
        public void keyTyped(char keyIn){
            if(legalChars.contains(""+Character.toLowerCase(keyIn))){
                this.sOut.insert(curPos,keyIn);
                this.showCursor = true;
                this.frameCount = 0;
                this.curPos++;
            }
        }
    }
}
