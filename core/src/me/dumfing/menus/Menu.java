package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * TODO: Text boxes<br>
 * WORK IN PROGRESS<br>
 * Menu.java<br>
 * Aaron Li<br>
 * Provides basic functions for all menus, animated backgrounds, buttons, images, textboxes
 */
public class Menu implements InputProcessor{
    private LinkedList<MenuTools.Button> buttons;
    private int backgroundFrame, frameTime, frameCount; // allows for animated backgrounds
    private boolean animatedBackground;
    private Array<TextureRegion> background; // all images used for background
    private LinkedList<MenuTools.TextureRect> images; // sprites for images, they can have their own position and texture
    private LinkedList<MenuTools.TextField> textFields;
    private LinkedList<MenuTools.ColourRect> colRects;
    private LinkedList<MenuBox> menuBoxes;
    private LinkedList<MenuTools.QueueText> queuedText;
    private Array<BitmapFontCache> fontCaches;
    private MenuTools.TextField focused; // the textbox that the user will be typing into

    /**
     * Constructor for the menu
     * @param bmfc BitMapFontCache for drawing all text in the menu
     */
    public Menu(Array<BitmapFontCache> bmfc){
        buttons = new LinkedList<MenuTools.Button>();
        backgroundFrame = 0;
        background = new Array<TextureRegion>();
        images = new LinkedList<MenuTools.TextureRect>();
        textFields = new LinkedList<MenuTools.TextField>();
        colRects = new LinkedList<MenuTools.ColourRect>();
        menuBoxes = new LinkedList<MenuBox>();
        queuedText = new LinkedList<MenuTools.QueueText>();
        this.fontCaches = bmfc;
    }

    /**
     * Sets this menu as the input processor <br>
     * Allows this menu to handle input like mouse and key presses
     */
    public void setInputProcessor(){
        Gdx.input.setInputProcessor(this);
    }

    /**
     * Adds a Button to the menu
     * @param btIn The Button object to add to the menu
     */
    public void addButton(MenuTools.Button btIn){
        this.buttons.add(btIn);
    }

    /**
     * Adds a TextField to the menu
     * @param txtIn The TextField object to add to the menu
     */
    public void addTextBox(MenuTools.TextField txtIn){
        this.textFields.add(txtIn);
    }

    /**
     * Adds a Coloured Rectangle to the Menu
     * @param rIn The ColourRect object to add to the menu
     */
    public void addColRect(MenuTools.ColourRect rIn){
        this.colRects.add(rIn);
    }

    /**
     * Adds a MenuBox to the Menu
     * @param mbIn The MenuBox object to add to the menu
     */
    public void addMenuBox(MenuBox mbIn){
        this.menuBoxes.add(mbIn);
    }

    /**
     * Adds a QueueText to the Menu
     * @param qtIn The QueueText object to add to the menu
     */
    public void addQueueText(MenuTools.QueueText qtIn){
        this.queuedText.add(qtIn);
    }
    /**
     * Removes a TextField from the Menu
     * @param toRm The TextField object to remove
     */
    public void removeTextField(MenuTools.TextField toRm){
        this.textFields.remove(toRm); // removes the box with the same memory address in the Array of Text Boxes
    }

    /**
     * Updates all components of the Menu
     */
    public void update(){
        for(MenuTools.Button bt : buttons){ //check all the buttons if they're currently pressed and the mouse is hovering over them
            bt.setPressed(bt.collidepoint(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY()) && Gdx.input.isButtonPressed(0)); // if the button is colliding with the mouse and the mouse is left clicking, make it look like it's pressed
            bt.update();
        }
        if(this.animatedBackground){ // if this Menu has a non-static background
            if(this.frameCount%this.frameTime == 0){ // if it's time to change frames
                this.backgroundFrame = (this.backgroundFrame+1)%this.background.size; // increase the background frame, going to 0 when at the max limit of frames
            }
        }
        else{
            this.backgroundFrame = 0; // use the first frame in the array (which is the one that is set by calling setBackground)
        }
        for(MenuTools.TextField tb : textFields){ // go through all TextBoxes in the Menu
            tb.update(fontCaches,focused ==tb); // update the textbox
        }
        for(MenuTools.ColourRect cR : colRects){
            cR.update();
        }
        for(MenuBox mb : menuBoxes){
            mb.update(focused);
        }
        for(MenuTools.QueueText qt : queuedText){
            qt.update();
        }
        this.frameCount++; // increase frameCounter
    }

    /**
     * Draws all texture related components of the Menu
     * @param sb The SpriteBatch used to draw the Textures
     */
    public void spriteDraw(SpriteBatch sb){ // draw all SpriteBatch related things
        sb.draw(background.get(backgroundFrame),0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()); // draw the appropriate background frame, stretching it if needed
        for(MenuTools.Button bt : this.buttons) { // check all the buttons in the Menu
            if (bt.getClicked()) { // if the button has been clicked
                sb.draw(bt.getPressed(), bt.getRect().x, bt.getRect().y, bt.getRect().width, bt.getRect().height); // draw the clicked button
            } else {
                sb.draw(bt.getUnpressed(), bt.getRect().x, bt.getRect().y, bt.getRect().width, bt.getRect().height); // draw the unclicked button
            }
        }
        for(MenuTools.TextureRect sp : images){ // draw all Sprites in the Menu
            sp.spriteDraw(sb);
        }
        for(MenuBox mb : menuBoxes){
            mb.spriteDraw(sb);
        }
        for(MenuTools.QueueText qt : queuedText){
            qt.queue(fontCaches);
        }
    }

    /**
     * Draws the Shape related portions of the menu
     * @param sr The ShapeRenderer to be used to draw the Menu
     */
    public void shapeDraw(ShapeRenderer sr){ // draw all shape related things
        for(MenuTools.ColourRect cR : colRects){
            cR.draw(sr);
        }
        for(MenuTools.TextField tb : textFields){ // TextBoxes look like two rectangles drawn on eachother
            tb.draw(sr,fontCaches,tb == focused); // draw the textBox, if it's the focused one then it'll have the cursor being drawn
        }
        for(MenuBox mb : menuBoxes){
            mb.shapeDraw(sr,focused);
        }
    }

    /**
     * Draws all components of the menu
     * @param sb The SpriteBatch used to draw the Textures
     * @param sr The ShapeRenderer used to draw the Shapes
     */
    public void draw(SpriteBatch sb, ShapeRenderer sr){ // Singular draw method to take up less space if the order of drawing doesn't matter
        this.spriteDraw(sb);
        this.shapeDraw(sr);
    }

    /**
     * Adds a Sprite based Image to the Menu
     * @param imgIn A Sprite object to draw on the Menu
     */
    public void addImage(MenuTools.TextureRect imgIn){ // add a sprite for drawing over buttons
        this.images.add(imgIn);
    } // add Images to the Menu

    /**
     * Adds a Sprite based Image to the Menu
     * @param img The Texture for the Sprite
     * @param x The X position of the Sprite
     * @param y The Y position of the Sprite
     */
    public void addImage(Texture img, int x, int y){ // add the components of a TextureRect
        MenuTools.TextureRect temp = new MenuTools.TextureRect(x,y, img.getWidth(),img.getHeight());
        temp.setRectTexture(img);
    }

    /**
     * Sets the background<br>
     * This will disable the animated background and will only show this TextureRegion if this is called after addBackground
     * @param bg The TextureRegion to be used as a background
     */
    public void setBackground(TextureRegion bg){ // set the background, disabling background animations and only using this frame
        if(this.background.size == 0){
            this.background.add(bg);
        }
        else {
            this.background.set(0, bg);
        }
        this.animatedBackground = false;
    }

    /**
     * Add more backgrounds to the Menu for an animated background
     * @param bg Background image to add as TextureRegion
     */
    public void addBackground(TextureRegion bg){ // add backgrounds to an animated background
        this.background.add(bg);
        this.animatedBackground = true;
    };

    /**
     * Sets the framerate at which to play the background animation
     * @param fps int for the fps of the animation (frames per second) must be less than 60
     */
    public void setFrameTime(int fps){ // set the framerate of the menu's background
        if(fps>60){
            throw new Error("Frame rate must be less than 60fps");
        }
        int updatesPerFrame = 60/fps; // find approximate frame time for each frame
        this.frameTime = updatesPerFrame;
    }

    /**
     * Allows you to populate the menu's components with assets that are loaded from the assetManager
     * @param assetManager
     * @param bmfc
     */
    public void init(AssetManager assetManager, BitmapFontCache bmfc){

    }
    /**
     *  Clears the currently focused TextField so it can stop listening to keypresses
     */
    public void clearFocused(){
        this.focused = null;
    }

    /**
     * Returns an Array of BitmapFontCaches
     * @return
     */
    public Array<BitmapFontCache> getFonts(){
        return this.fontCaches;
    }
    /**
     * The event that handles Keypresses
     * @param keycode The keycode of the key that was pressed
     * @return Whether or not the input was processed
     */
    @Override
    public boolean keyDown(int keycode) { // on Key Pressed event
        if(focused!=null) { // if there is a focused textBox
            focused.keyDown(keycode); // tell the TextField that a key has been pressed
        }
        return true;
    }

    /**
     * The event that handles Key releases
     * @param keycode The keycode of the key that was released
     * @return Whether or not the input was processed
     */
    @Override
    public boolean keyUp(int keycode) {
        if(focused!=null) { // if there is a focused TextField
            focused.keyUp(keycode); // tell it thate a key has been lifted
        }
        return true;
    }

    /**
     * Handles typed keys from the keyboard
     * @param character The char that was typed
     * @return Whether or not the input was processed
     */
    @Override
    public boolean keyTyped(char character) {
        if(focused!=null) { // if there is a focused textBox
            focused.keyTyped(character); // tell it a character has been typed
        }
        return true;
    }

    /**
     * Handles TouchDown events
     * @param screenX The x position the screen was pressed at
     * @param screenY The y position the screen was pressed at, using the top of the screen as 0 and the bottom as the largest number
     * @param pointer Which pointer was being used (?????????????????????????????????) TODO: figure this out
     * @param button Which button on the mouse was rpessed
     * @return Whether or not the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight()-screenY;
        for(MenuTools.TextField tb : textFields){
            if(tb.collidePoint(screenX,screenY)){
                focused = tb;
                return true;
            }
        }
        for(MenuBox mb : menuBoxes){
            focused = mb.textFieldsClicked(screenX,screenY);
        }
        return true;
    }

    /**
     * Handles touchUp events
     * @param screenX The x position of the touch location
     * @param screenY The y position of the touch location, uses the top of the screen as 0 and the bottom as the max height
     * @param pointer The pointer that was used (?????????????) TODO: figure this out
     * @param button The button that was released
     * @return Whether or not the input was processed
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight() - screenY; // invert screenY because screenY is from top of screen
        for(MenuTools.Button bt : buttons){ // check all buttons
            bt.setPressed(false); // since mouse is being unpressed, all buttons can be set to unpressed
            if(bt.collidepoint(screenX,screenY)){ // if the mouse is hovering over a button
                bt.activate(); // activate that button's function
            }
        }
        for(MenuBox mb : menuBoxes){
            mb.checkButtonsPressed(screenX,screenY);
        }
        return true;
    }

    /**
     * Handles TouchDragged events
     * @param screenX The X position of the cursor
     * @param screenY The Y position of the cursor, uses the top of the screen as 0 and the bottom of the screen as the max height
     * @param pointer The pointer that was used (???????????) TODO: figure this out
     * @return Whether or not the input was processed
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Handles events when the mouse is moved
     * @param screenX The X position of the cursor
     * @param screenY The Y position of the cursor, uses the top of the screen as 0 and the bottom of the screen as the max height
     * @return Whether or not the input was processed
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Handles mouseScrolled events
     * @param amount The amount the mouse was scrolled
     * @return Whether or not the input was processed
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}