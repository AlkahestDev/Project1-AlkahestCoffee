package me.dumfing.gdxtools;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import me.dumfing.gdxtools.DrawTools;

import java.util.Arrays;

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
     * A Rectangular area that can be drawn with a ShapeRenderer
     */
    public static class ColourRect{
        private Rectangle rectShape;
        private Color rectColor;
        private float vX, vY;
        /**
         * The constructor for the ColourRect
         * @param x The X position of the Rectangle
         * @param y The Y position of the Rectangle
         * @param w The width of the Rectangle
         * @param h The height of the Rectangle
         * @param r The red component of the ColourRect
         * @param g The green component of the ColourRect
         * @param b The blue component of the ColourRect
         * @param a The alpha value of the ColourRect
         */
        public ColourRect(float x, float y, float w, float h, float r, float g, float b, float a){
            this.rectColor = new Color(r,g,b,a);
            this.rectShape = new Rectangle(x,y,w,h);
        }

        /**
         * The constructor for the ColourRect
         * @param rectShape A Rectangle object for the ColourRect
         * @param r The red component of the ColourRect
         * @param g The green component of the ColourRect
         * @param b The blue component of the ColourRect
         * @param a The alpha value of the ColourRect
         */
        public ColourRect(Rectangle rectShape, float r,float g,float b,float a){
            this.rectColor = new Color(r,g,b,a);
            this.rectShape = rectShape;
        }

        /**
         * The constructor for the ColourRect
         * @param rectShape A Rectangle object for the ColourRect
         * @param rectCol A Color object to determine the color for the ColourRect
         */
        public ColourRect(Rectangle rectShape, Color rectCol){
            this.rectShape = rectShape;
            this.rectColor = rectCol;
        }

        /**
         * Draws the ColourRect
         * @param sr ShapeRenderer used to draw the ColourRect
         */
        public void draw(ShapeRenderer sr){
            sr.setColor(this.rectColor);
            DrawTools.rec(sr,this.rectShape);
        }

        /**
         * Moves the ColourRect based on it's velocity
         */
        public void update(){
            this.rectShape.x+=this.vX;
            this.rectShape.y+=this.vY;
            if(this.vX > 0){
                this.vX = Math.max(0,this.vX-0.5f);
            }
            else if(this.vX < 0){
                this.vX = Math.min(0,this.vX +0.5f);
            }
            if(this.vY > 0){
                this.vY = Math.max(0,this.vY-0.5f);
            }
            else if(this.vY < 0){
                this.vY = Math.min(0,this.vY + 0.5f);
            }
        }

        /**
         * Set the X and Y components of the ColourRect's motion
         * @param x The X component of the ColourRect's velocity
         * @param y The Y component of the ColourRect's velocity
         */
        public void setVelocity(float x, float y){
            this.vX = x;
            this.vY = y;
        }

        /**
         * Returns the rectangular area of the ColourRect
         * @return The Rectangle object used to draw the ColourRect
         */
        public Rectangle getRectShape(){
            return this.rectShape;
        }
    }

    /**
     * A Button that can be pressed call a OnClick method
     * The method is called on releasing the LMB on the Button
     */
    public static class Button{
        private TextureRegion unpressed, pressed; // how the button looks when it's held down and when it's not held down
        private Rectangle buttonArea;
        boolean isPressed = false;
        OnClick callback;

        /**
         * Constructor for the Button
         * @param x The X position of the Button
         * @param y The Y position of the Button
         * @param width The width of the Button
         * @param height The height of the Button
         * @param callback The action to be taken when the button is clicked
         */
        public Button(float x, float y, float width, float height,OnClick callback){
            buttonArea = new Rectangle(x,y,width,height);
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
            return this.buttonArea.contains(mX, mY);
            //return isPressed;
        }

        /**
         * Returns the Rectangular area of the Button
         * @return The Rectangle Object used to determine the buttons shape and area
         */
        public Rectangle getButtonArea(){
            return this.buttonArea;
        }

        /**
         * Gets whether or not the button is pressed or not
         * @return boolean for if the button is being pressed
         */
        public boolean getClicked(){
            return this.isPressed;
        }

        /**
         * Draws the button. Will not scale or stretch the TextureRegion if it's shape isn't the same as the Button
         * @param sr ShapeRenderer to draw the button if in debug mode
         * @param batch The SpriteBatch to draw the button if not in debug mode
         * @param debug Flag to turn on debug mode for the button
         */
        public void draw(ShapeRenderer sr, SpriteBatch batch, boolean debug){
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
    public static class TextField {
        float vX,vY;
        Rectangle boxShape;
        StringBuilder sOut;
        int curPos, frameCount;
        int[] heldKeys; // shift everything up by one because anykey is -1
        boolean showCursor;
        OnEnter enterText;

        /**
         * Constructor for TextField
         * @param bShape A Rectangle object for the area the TextField takes up
         * @param eT An OnEnter interface that determines what the TextField does when the user presses enter
         */
        public TextField(Rectangle bShape, OnEnter eT){
            this.boxShape = bShape;
            this.enterText = eT;
            initVars();
        }

        /**
         * Constructor for TextField
         * @param x The x position of the TextField
         * @param y The y position of the TextField
         * @param width The width of the TextField
         * @param height The height of the TextField
         * @param eT The OnEnter interface that determines what the TextField does when the user presses enter
         */
        public TextField(float x, float y, float width, float height, OnEnter eT){
            this.boxShape = new Rectangle(x,y,width,height);
            this.enterText = eT;
            initVars();
        }

        /**
         * Constructor for TextField, leaves entertext null for setting it later with setEnterAction
         * @param x The x position of the TextField
         * @param y The y position of the TextField
         * @param width The width of the TextField
         * @param height The height of the TextField
         */
        public TextField(float x, float y, float width, float height){
            this.boxShape = new Rectangle(x,y,width,height);
            this.enterText = null;
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
        public void update(BitmapFontCache bmfc, boolean focused){
            this.boxShape.x+=vX;
            this.boxShape.y+=vY;
            if(vX > 0) {
                this.vX = Math.max(0, this.vX - 0.5f);
            }
            else if(vX < 0){
                this.vX = Math.min(0, this.vX + 0.5f);
            }
            if(vY > 0){
                this.vY = Math.max(0, this.vY - 0.5f);
            }
            else if(this.vY < 0){
                this.vY = Math.min(0,this.vY + 0.5f);
            }
            handleHeldKeys();
            bmfc.setColor(Color.BLACK);
            bmfc.addText(this.sOut.toString(),this.boxShape.getX()+3,this.boxShape.getY()+this.boxShape.getHeight() - 7,0,this.sOut.length(),this.boxShape.getWidth(), Align.left,false,"");
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

        /**
         * Determines whether a coordinate point lies within the bounds of this TextField
         * @param mX The x position of the coordinate
         * @param mY The y position of the coordinate
         * @return boolean that determines whether or not the point lies within the bounds of the rectangle
         */
        public boolean collidePoint(float mX, float mY){
            return this.boxShape.contains(mX,mY);
        }

        /**
         * Draws the TextField
         * @param sr ShapeRenderer for drawing the TextField
         * @param fnt BitmapFont for determining the width of the text, should be the same font that the BitmapFontCache is using
         * @param focused boolean for whether or not this TextField is currently focused on
         */
        public void draw(ShapeRenderer sr, BitmapFont fnt, boolean focused){
            sr.setColor(Color.BLACK);
            DrawTools.rec(sr,this.boxShape);
            sr.setColor(Color.WHITE);
            sr.rect(this.boxShape.x+2,this.boxShape.y+2,this.boxShape.width-4,this.boxShape.height-4);
            sr.setColor(Color.BLACK);
            float tWidth = new GlyphLayout(fnt,this.sOut.toString().substring(0,curPos)).width;
            if(tWidth<this.boxShape.width-12 && focused && showCursor) { // for the line to be drawn, the width fo the text must be within the width of the box, and the box must be focused
                sr.rect(this.boxShape.getX() + tWidth + 5, this.boxShape.getY() + 5, 1, this.boxShape.getHeight() - 10);
            }

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
         * Set the velocity of the TextField for animated movement
         * @param x The x component of the TextField's velocity
         * @param y The y component of the TextField's velocity
         */
        public void setVelocity(float x, float y){
            this.vX = x;
            this.vY = y;
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
}
