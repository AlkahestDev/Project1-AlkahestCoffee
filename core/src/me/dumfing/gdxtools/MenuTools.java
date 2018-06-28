package me.dumfing.gdxtools;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import me.dumfing.menus.MenuBox;

import java.util.Arrays;

import static me.dumfing.client.maingame.MainGame.DAGGER40;

/**
 * MenuTools.java
 * Various elements used in menus
 * TODO: polygons
 *
 */
public class MenuTools {
    public static final int keyTime = 7; // number of frames before a key is registered again
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
        public void setFont(int fontID){
            this.preferredFont = fontID;
        }
        public String getText(){
            return this.qText;
        }
        public void setText(String tIn,Array<BitmapFontCache> bmfc){
            this.qText = tIn;
            textWidth = new GlyphLayout(bmfc.get(preferredFont).getFont(),tIn).width;
        }
        public void clearText(){
            this.qText = "";
            textWidth = 0;
        }
        public void queue(Array<BitmapFontCache> fontCaches){
            fontCaches.get(preferredFont).addText(this.qText,super.shape.x,super.shape.y,textWidth,Align.left,false);
        }
    }
    /**
     * A Rectangular area for Textures and TextureRegions, similar to a sprite but can have velocity
     */
    public static class TextureRect extends MenuObject {
        private Rectangle rectShape;
        private TextureRegion rectTexture;

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

        public TextureRect(Rectangle rect) {
            super(rect.x,rect.y,rect.width,rect.height);
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
        private Color rectColor;
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
    }

    /**
     * A Button that can be pressed call a OnClick method
     * The method is called on releasing the LMB on the Button
     */
    public static class Button extends MenuObject {
        private TextureRegion unpressed, pressed; // how the button looks when it's held down and when it's not held down
        boolean isPressed = false;
        OnClick callback;
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

        /**
         * Constructor for the Button
         * @param pressedTexture The texture for the button when it's pressed, the width and height of the button are derived by this texture's size
         * @param unpressedTexture The texture for the button when it's not pressed
         * @param x The x position of the button
         * @param y The y position of the button
         * @param action The action for the button to run when it's clicked
         */
        public Button(TextureRegion pressedTexture, TextureRegion unpressedTexture, float x, float y, OnClick action){
            super(x,y,pressedTexture.getRegionWidth(),pressedTexture.getRegionHeight());
            this.pressed = pressedTexture;
            this.unpressed = unpressedTexture;
            this.callback = action;
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
                    DrawTools.textureRect(batch,super.getRect(),pressed);
                }
                else{
                    DrawTools.textureRect(batch,super.getRect(),unpressed);
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
        Rectangle boxShape;
        StringBuilder sOut;
        int curPos, frameCount, fontId;
        int[] heldKeys; // shift everything up by one because anykey is -1
        boolean showCursor;
        OnEnter enterText;
        String hint;

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
        public TextField(float x, float y, float width, float height, String hint){
            super(x,y,width,height);
            initVars();
            this.hint = hint;
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
            if(this.sOut.toString().length()>0) {
                GlyphLayout testLayout = new GlyphLayout(bmfc.get(fontId).getFont(), this.sOut.toString());
                System.out.println(testLayout.height);
                bmfc.get(fontId).setColor(Color.BLACK);
                String cutString = cutToSize(this.sOut.toString(), super.shape.width, bmfc.get(fontId).getFont());
                bmfc.get(fontId).addText(cutString, super.shape.getX() + 3, super.shape.getY() + super.shape.getHeight()/2f + testLayout.height/2f, 0, cutString.length(), super.shape.getWidth(), Align.left, false);
            }
            else if(this.hint!=null && !focused){
                GlyphLayout testLayout = new GlyphLayout(bmfc.get(fontId).getFont(), this.hint);
                System.out.println(testLayout.height);
                bmfc.get(fontId).setColor(Color.LIGHT_GRAY);
                bmfc.get(fontId).addText(hint, super.shape.getX()+3, super.shape.getY()+super.shape.getHeight()/2f+testLayout.height/2f);
            }
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
        public void shapeDraw(ShapeRenderer sr, Array<BitmapFontCache> fnt, boolean focused){
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
            if(' '<=Character.toLowerCase(keyIn) && Character.toLowerCase(keyIn)<='~'){
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
     * Returns the length of text with a given font
     * @param fntIn The font to use
     * @param sIn The text to check the width of
     * @return The width the text will take up
     */
    public static float textWidth(BitmapFont fntIn, String sIn){
        return new GlyphLayout(fntIn,sIn).width;
    }

    /**
     * Returns the height of the text with a given font
     * @param fntIn The font to use
     * @param sIn The text to check the height of
     * @return The height the text will take up
     */
    public static float textHeight(BitmapFont fntIn, String sIn){
        return new GlyphLayout(fntIn,sIn).height;
    }
    /**
     * ManagerGetTextureRegion<br>
     * Gets a textureregion from an assetmanager
     * @param fileName The file name of the asset
     * @param manager The assetmanager to retrieve the file from
     * @return The textureregion to use
     */
    public static TextureRegion mGTR(String fileName, AssetManager manager){
        return new TextureRegion((Texture)manager.get(fileName));
    }

    /**
     * Cuts the string so it'll fit into the target width
     * @param textIn The text you wish to cut
     * @param targetWidth The target width you want the text to be cut into
     * @param fontIn The font to base the widths off of
     * @return A string that will be within the target width when rendered with the given font
     */
    public static String cutToSize(String textIn, float targetWidth, BitmapFont fontIn){
        String out=textIn;
        for(int i = textIn.length();i>0;i--) {
            String cutText = out.substring(0, i);
            if (textWidth(fontIn, cutText) <= targetWidth) {
                return cutText;
            }
        }
        return "";
    }

    /**
     * Creates a MenuBox that has a button and queue text centered over it
     * @param btX The x of the button
     * @param btY The y of the button
     * @param btW The width of the button
     * @param btH The height of the button
     * @param text The label for the butotn
     * @param btAction The action the button executes when it's clicked
     * @param pressed The pressed texture for the button
     * @param unpressed The unpressed texture for the button
     * @param fontCaches The font caches to use
     * @param pickedFont The font to draw with
     * @return A menubox with the button and label
     */
    public static MenuBox createLabelledButton(float btX, float btY, float btW, float btH, String text, OnClick btAction, TextureRegion pressed, TextureRegion unpressed, Array<BitmapFontCache> fontCaches, int pickedFont){
        MenuBox out = new MenuBox(btX,btY,btW,btH,fontCaches);
        Button actionButton = new Button(0,0,btW,btH);
        actionButton.setCallback(btAction);
        actionButton.setPressedTexture(pressed);
        actionButton.setUnpressedTexture(unpressed);
        out.addButton(actionButton);
        QueueText textLabel = new QueueText((btW/2)-(textWidth(fontCaches.get(pickedFont).getFont(),text)/2),(btH/2)+(textHeight(fontCaches.get(pickedFont).getFont(),text)/2),0,0);
        textLabel.setText(text,fontCaches);
        out.addQueueText(textLabel);
        return out;
    }
}
