package me.dumfing.gdxtools;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.DrawTools;
import org.w3c.dom.css.Rect;

import java.util.Arrays;

import static me.dumfing.maingame.MainGame.DAGGER40;

/**
 * TODO: getting text from text box
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
    public interface OnEnter{
        void action(String sIn);
    }

    /**
     * Allows you to queue text to write on the Menu
     */
    public static class QueueText extends MenuObject{
        String qText;
        int preferredFont;
        float textWidth = 0;
        public QueueText(float x, float y, float w, float h) {
            super(x, y, w, h);
            preferredFont = DAGGER40;
            this.qText = "";
        }
        public void setPreferredFont(int fontID){
            this.preferredFont = fontID;
        }
        public void setText(String tIn,Array<BitmapFontCache> bmfc){
            this.qText = tIn;
            textWidth = new GlyphLayout(bmfc.get(preferredFont).getFont(),tIn).width;
        }
        public void queue(Array<BitmapFontCache> fontCaches){
            fontCaches.get(preferredFont).addText(this.qText,super.shape.x,super.shape.y,textWidth,0,false);
        }
    }
    /**
     * A Rectangular area for Textures and TextureRegions, similar to a sprite but can have velocity
     */
    public static class TextureRect extends MenuObject {
        private Rectangle rectShape;
        private TextureRegion rectTexture;
        private float vX = 0;
        private float vY = 0;

        /**
         * Constructor for TextureRect
         * @param x The x position of the TextureRect
         * @param y The y position of the TextureRect
         * @param w The width of the TextureRect
         * @param h The height of the TextureRect
         */
        public TextureRect(float x, float y, float w, float h){
            super(x,y,w,h);
        }
        public void setRectTexture(TextureRegion tr){
            this.rectTexture = tr;
        }
        public void setRectTexture(Texture t){
            this.rectTexture = new TextureRegion(t);
        }

        @Override
        public void spriteDraw(SpriteBatch sb) {
            DrawTools.textureRect(sb,super.shape,this.rectTexture);
        }
    }
    /**
     * A Rectangular area that can be drawn with a ShapeRenderer
     */
    public static class ColourRect extends MenuObject{
        private Rectangle rectShape;
        private Color rectColor;
        private float vX, vY;
        /**
         * The constructor for the ColourRect
         * @param x The X position of the Rectangle
         * @param y The Y position of the Rectangle
         * @param w The width of the Rectangle
         * @param h The height of the Rectangle
         */
        public ColourRect(float x, float y, float w, float h){
            super(x,y,w,h);
        }
        public void setColor(float r, float g, float b, float a){
            this.rectColor = new Color(r,g,b,a);
        }
        /**
         * Draws the ColourRect
         * @param sr ShapeRenderer used to draw the ColourRect
         */
        public void draw(ShapeRenderer sr){
            sr.setColor(this.rectColor);
            DrawTools.rec(sr,super.shape);
        }


        /**
         * Returns the rectangular area of the ColourRect
         * @return The Rectangle object used to draw the ColourRect
         */
        public Rectangle getRectShape(){
            return super.shape;
        }
    }

    /**
     * A Button that can be pressed call a OnClick method
     * The method is called on releasing the LMB on the Button
     */
    public static class Button extends MenuObject {
        private TextureRegion unpressed, pressed; // how the button looks when it's held down and when it's not held down
        boolean isPressed = false;
        OnClick callback;
        float vX = 0;
        float vY = 0;
        /**
         * Constructor for the Button
         * @param x The X position of the Button
         * @param y The Y position of the Button
         * @param width The width of the Button
         * @param height The height of the Button
         */
        public Button(float x, float y, float width, float height){
            super(x,y,width,height);
        }
        public void setCallback(OnClick callback){
            this.callback = callback;
        }

        /**
         * Sets the state of the button
         * @param pressed boolean for whether or not it's pressed
         */
        public void setPressed(boolean pressed){
            this.isPressed = pressed;
        }

        /**
         * Calls the OnClick method assigned to the button
         */
        public void activate(){
            callback.action();
        }

        /**
         * Checks if a coordinate is within the area of the button
         * @param mX The x portion of the coordinate to check with
         * @param mY The y portion of the coordinate to check with
         * @return Returns a boolean that determines whether or not the coordinates lies within the area of the button
         */
        public boolean collidepoint(float mX, float mY){
            return super.shape.contains(mX, mY);
            //return isPressed;
        }

        /**
         * Gets whether or not the button is pressed or not
         * @return boolean for if the button is being pressed
         */
        public boolean getClicked(){
            return this.isPressed;
        }

        /**
         * Draws the button. Will not scale or stretch the TextureRegion if it's shape isn't the same as the Button<br>
         * @param sr ShapeRenderer to draw the button if in debug mode
         * @param batch The SpriteBatch to draw the button if not in debug mode
         * @param debug Flag to turn on debug mode for the button
         */
        public void draw(ShapeRenderer sr, SpriteBatch batch, boolean debug){
            if(debug){
                if(this.isPressed) {
                    sr.setColor(Color.RED);
                    DrawTools.rec(sr, super.getRect());
                }
                else{
                    sr.setColor(Color.BLUE);
                    DrawTools.rec(sr,super.getRect());
                }
            }
            else{
                if(this.isPressed){
                    //batch.draw(pressed,super.getRect().getX(),super.getRect().getY(),super.getRect().getWidth(),super.getRect().getHeight());
                    DrawTools.textureRect(batch,super.getRect(),pressed);
                }
                else{
                    //System.out.println(super.vX);
                    DrawTools.textureRect(batch,super.getRect(),unpressed);
                    //batch.draw(unpressed,super.getRect().getX(),super.getRect().getY(),super.getR);
                }
            }
        }
        /**
         * Sets the texture to be used for when the button is being pressed
         * @param pressedTexture TextureRegion to be drawn when the button is pressed
         */
        public void setPressedTexture(TextureRegion pressedTexture){
            this.pressed = pressedTexture;
        }

        /**
         * Sets the texture to be used for when the button is not being pressed
         * @param unpressedTexture TextureRegion to be drawn when the button is not pressed
         */
        public void setUnpressedTexture(TextureRegion unpressedTexture){
            this.unpressed = unpressedTexture;
        }

        /**
         * Gets the TextureRegion to be used when the button is being pressed
         * @return The TextureRegion that the button uses when it's pressed
         */
        public TextureRegion getPressed() {
            return pressed;
        }

        /**
         * Gets the TextureRegion to be used when the button is not being pressed
         * @return The TextureRegion that the button uses when it's not pressed
         */
        public TextureRegion getUnpressed() {
            return unpressed;
        }

        /**
         * Draws the Shape portion of the button
         * Essentialy calls the main draw method in debug mode
         * @param sr The ShapeRenderer used to draw the button's rectangular portion
         */
        public void draw(ShapeRenderer sr){
            this.draw(sr,null,true);
        }

        /**
         * Draws the TextureRegion portion of the button
         * Essentially calls the main draw method without debug mode
         * @param batch The SpriteBatch to be used to draw the button
         */
        public void draw(SpriteBatch batch){
            this.draw(null,batch,false);
        }

    }

    /**
     * TextField for getting String input from the user
     */
    public static class TextField extends MenuObject{
        float vX,vY;
        Rectangle boxShape;
        StringBuilder sOut;
        int curPos, frameCount, fontId;
        int[] heldKeys; // shift everything up by one because anykey is -1
        boolean showCursor;
        OnEnter enterText;

        /**
         * Constructor for TextField
         * @param x The x position of the TextField
         * @param y The y position of the TextField
         * @param width The width of the TextField
         * @param height The height of the TextField
         */
        public TextField(float x, float y, float width, float height){
            super(x,y,width,height);
            initVars();
        }

        /**
         * Set the action for the TextField to call when the user presses enter
         * @param et OnEnter object for the TextField to call
         */
        public void setEnterAction(OnEnter et){
            this.enterText = et;
        }

        /**
         * Updates the TextField
         * @param bmfc The BitmapFontCache for drawing the text in the TextField
         * @param focused Whether or not this TextField is being typed into
         */
        public void update(Array<BitmapFontCache> bmfc, boolean focused){
            handleHeldKeys();
            bmfc.get(fontId).setColor(Color.BLACK);
            bmfc.get(fontId).addText(this.sOut.toString(),super.shape.getX()+3,super.shape.getY()+super.shape.getHeight() - 7,0,this.sOut.length(),super.shape.getWidth(), Align.left,false,"");
            if(focused){
                this.frameCount++; // Integer.MAX_VALUE frames is around 414 days to overflow, if a user has the text box open for 414 days, the game probably won't be worth keeping open
            }
            else{
                this.frameCount = 0; // keeps delay consistent when refocusing on a textbox (also aids in staying away from Integer.MAX_VALUE)
            }
            if(this.frameCount%40 == 0){
                this.showCursor = !showCursor;
            }
            for(int i = 0; i<257; i++){
                if(heldKeys[i]>-1) {
                    this.heldKeys[i]++;
                }
            }
            super.update();
        }

        /**
         * Determines whether a coordinate point lies within the bounds of this TextField
         * @param mX The x position of the coordinate
         * @param mY The y position of the coordinate
         * @return boolean that determines whether or not the point lies within the bounds of the rectangle
         */
        public boolean collidePoint(float mX, float mY){
            return super.shape.contains(mX,mY);
        }

        /**
         * Draws the TextField
         * @param sr ShapeRenderer for drawing the TextField
         * @param fnt BitmapFont for determining the width of the text, should be the same font that the BitmapFontCache is using
         * @param focused boolean for whether or not this TextField is currently focused on
         */
        public void draw(ShapeRenderer sr, Array<BitmapFontCache> fnt, boolean focused){
            sr.setColor(Color.BLACK);
            DrawTools.rec(sr,super.shape);
            sr.setColor(Color.WHITE);
            sr.rect(super.shape.x+2,super.shape.y+2,super.shape.width-4,super.shape.height-4);
            sr.setColor(Color.BLACK);
            float tWidth = new GlyphLayout(fnt.get(this.fontId).getFont(),this.sOut.toString().substring(0,curPos)).width;
            if(tWidth<super.shape.width-12 && focused && showCursor) { // for the line to be drawn, the width fo the text must be within the width of the box, and the box must be focused
                sr.rect(super.shape.getX() + tWidth + 5, super.shape.getY() + 5, 1, super.shape.getHeight() - 10);
            }
        }

        /**
         * Sets the font to use for drawing
         * @param fontId The id of the font
         */
        public void setFont(int fontId){
            this.fontId = fontId;
        }
        /**
         * Singular method to initialize the variables for all constructors
         */
        private void initVars(){
            this.sOut = new StringBuilder();
            this.curPos = this.frameCount = 0;
            this.vX = this.vY = 0;
            this.heldKeys = new int[257];
            this.showCursor = false;
            this.fontId = DAGGER40;
            Arrays.fill(this.heldKeys,-1);
        }

        /**
         * Handles keypresses received from KeyPress events
         * @param keycode The keycode of the key that was pressed
         */
        public void keyDown(int keycode){
            this.heldKeys[keycode+1] = 0;
            this.showCursor = true;
            this.frameCount = 0;
            //Keys that should only be pressed once
            if(keycode == Input.Keys.ENTER){
                if(this.enterText != null) {
                    this.enterText.action(this.sOut.toString());
                }
                this.sOut = new StringBuilder();
                this.curPos = 0;
            }
        }


        /**
         * Handler for when you release a key
         * @param keycode The keycode for the key that was released
         */
        public void keyUp(int keycode){
            this.heldKeys[keycode+1] = -1;
        }

        /**
         * Handles held keys, if a key is being held this will periodically execute the keys function
         */
        public void handleHeldKeys(){
            //Keys that need to be repressed periodically
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
                    keyPressed = true;
                }
            }
            if(this.heldKeys[Input.Keys.FORWARD_DEL+1] % keyTime == 0){
                if(this.curPos<this.sOut.length()){
                    this.sOut.deleteCharAt(this.curPos);
                    keyPressed = true;
                }
            }
            if(keyPressed){
                this.showCursor = true;
                this.frameCount = 0;
            }
        }

        /**
         * Handles events from when the user is normally typing
         * @param keyIn The char for the letter that was entered
         */
        public void keyTyped(char keyIn){
            if(legalChars.contains(""+Character.toLowerCase(keyIn))){
                this.sOut.insert(curPos,keyIn);
                this.showCursor = true;
                this.frameCount = 0;
                this.curPos++;
            }
        }

        /**
         * gets the text out of the StringBuilder (and the TextField)
         * @return The text the user has typed into the TextField thus far
         */
        public String getText(){
            return this.sOut.toString();
        }
    }

    /**
     * Will increase or decrease a number towards 0 by steps of 0.5
     * @param numIn The number to be modified
     * @return The number 0.5 units closer to 0
     */
    public static float towardsZero(float numIn){
        if(numIn > 0) {
            return Math.max(0, numIn - 0.5f);
        }
        else if(numIn < 0){
            return Math.min(0, numIn + 0.5f);
        }
        else{
            return numIn;
        }
    }

    /**
     * Returns the length of text with a given font
     * @param fntIn The font to use
     * @param sIn The text to check the width of
     * @return The width the text will take up
     */
    public static float textWidth(BitmapFont fntIn, String sIn){
        return new GlyphLayout(fntIn,sIn).width;
    }
}
