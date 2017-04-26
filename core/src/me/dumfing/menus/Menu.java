package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;

import java.util.LinkedList;

/**
 * TODO: Text boxes
 * WORK IN PROGRESS
 * Menu.java
 * Aaron Li
 * Provides basic functions for all menus, animated backgrounds, buttons, images, textboxes(wip)
 */
public class Menu implements InputProcessor{
    private LinkedList<MenuTools.Button> buttons;
    private int backgroundFrame, frameTime, frameCount; // allows for animated backgrounds
    private boolean animatedBackground;
    private Array<TextureRegion> background; // all images used for background
    private LinkedList<Sprite> images; // sprites for images, they can have their own position and texture
    private LinkedList<MenuTools.TextBox> textBoxes;
    private LinkedList<MenuTools.ColourRect> colRects;
    private LinkedList<MenuBox> menuBoxes;
    private BitmapFontCache textCache;
    private MenuTools.TextBox focused; // the textbox that the user will be typing into
    public Menu(BitmapFontCache bmfc){
        buttons = new LinkedList<MenuTools.Button>();
        backgroundFrame = 0;
        background = new Array<TextureRegion>();
        images = new LinkedList<Sprite>();
        textBoxes = new LinkedList<MenuTools.TextBox>();
        colRects = new LinkedList<MenuTools.ColourRect>();
        menuBoxes = new LinkedList<MenuBox>();
        this.textCache = bmfc;
    }
    public void setInputProcessor(){
        Gdx.input.setInputProcessor(this);
    }
    public void addButton(MenuTools.Button btIn){
        this.buttons.add(btIn);
    }
    public void addTextBox(MenuTools.TextBox txtIn){
        this.textBoxes.add(txtIn);
    }
    public void addColRect(MenuTools.ColourRect rIn){
        this.colRects.add(rIn);
    }
    public void addMenuBox(MenuBox mbIn){
        this.menuBoxes.add(mbIn);
    }
    public void removeTextBox(MenuTools.TextBox toRm){
        this.textBoxes.remove(toRm); // removes the box with the same memory address in the Array of Text Boxes
    }
    public void update(){
        for(MenuTools.Button bt : buttons){ //check all the buttons if they're currently pressed and the mouse is hovering over them
            bt.setPressed(bt.collidepoint(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY()) && Gdx.input.isButtonPressed(0)); // if the button is colliding with the mouse and the mouse is left clicking, make it look like it's pressed
        }
        if(this.animatedBackground){ // if this Menu has a non-static background
            if(this.frameCount%this.frameTime == 0){ // if it's time to change frames
                this.backgroundFrame = (this.backgroundFrame+1)%this.background.size; // increase the background frame, going to 0 when at the max limit of frames
            }
        }
        else{
            this.backgroundFrame = 0; // use the first frame in the array (which is the one that is set by calling setBackground)
        }
        for(MenuTools.TextBox tb : textBoxes){ // go through all TextBoxes in the Menu
            tb.update(textCache,focused ==tb); // update the textbox
        }
        for(MenuTools.ColourRect cR : colRects){
            cR.update();
        }
        for(MenuBox mb : menuBoxes){
            mb.update();
        }
        this.frameCount++; // increase frameCounter
    }
    public void spriteDraw(SpriteBatch sb){ // draw all SpriteBatch related things
        sb.draw(background.get(backgroundFrame),0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight()); // draw the appropriate background frame, stretching it if needed
        for(MenuTools.Button bt : this.buttons) { // check all the buttons in the Menu
            if (bt.getClicked()) { // if the button has been clicked
                sb.draw(bt.getPressed(), bt.getButtonArea().x, bt.getButtonArea().y, bt.getButtonArea().width, bt.getButtonArea().height); // draw the clicked button
            } else {
                sb.draw(bt.getUnpressed(), bt.getButtonArea().x, bt.getButtonArea().y, bt.getButtonArea().width, bt.getButtonArea().height); // draw the unclicked button
            }
        }
        for(Sprite sp : images){ // draw all Sprites in the Menu
            sp.draw(sb);
        }
        for(MenuBox mb : menuBoxes){
            mb.spriteDraw(sb);
        }
    }
    public void shapeDraw(ShapeRenderer sr){ // draw all shape related things
        for(MenuTools.ColourRect cR : colRects){
            cR.draw(sr);
        }
        for(MenuTools.TextBox tb : textBoxes){ // TextBoxes look like two rectangles drawn on eachother
            tb.draw(sr,textCache.getFont(),tb == focused); // draw the textBox, if it's the focused one then it'll have the cursor being drawn
        }
        for(MenuBox mb : menuBoxes){
            mb.shapeDraw(sr);
        }
    }
    public void draw(SpriteBatch sb, ShapeRenderer sr){ // Singular draw method to take up less space if the order of drawing doesn't matter
        this.spriteDraw(sb);
        this.shapeDraw(sr);
    }
    public void addImage(Sprite imgIn){ // add a sprite for drawing over buttons
        this.images.add(imgIn);
    } // add Images to the Menu
    public void addImage(Texture img, int x, int y){ // add the components of a sprite
        Sprite temp = new Sprite(img);
        temp.setPosition(x,y);
        addImage(temp);
    }
    public void setBackground(TextureRegion bg){ // set the background, disabling background animations and only using this frame
        if(this.background.size == 0){
            this.background.add(bg);
        }
        else {
            this.background.set(0, bg);
        }
        this.animatedBackground = false;
    }
    public void addBackground(TextureRegion bg){ // add backgrounds to an animated background
        this.background.add(bg);
        this.animatedBackground = true;
    };
    public void setFrameTime(int fps){ // set the framerate of the menu's background
        int updatesPerFrame = 60/fps; // find approximate frame time for each frame
        this.frameTime = updatesPerFrame;
    }
    @Override
    public boolean keyDown(int keycode) { // on Key Pressed event
        if(focused!=null) { // if there is a focused textBox
            focused.keyDown(keycode); // tell the TextBox that a key has been pressed
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(focused!=null) { // if there is a focused TextBox
            focused.keyUp(keycode); // tell it thate a key has been lifted
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if(focused!=null) { // if there is a focused textBox
            focused.keyTyped(character); // tell it a character has been typed
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenY = Gdx.graphics.getHeight()-screenY;
        for(MenuTools.TextBox tb : textBoxes){
            if(tb.collidePoint(screenX,screenY)){
                focused = tb;
                return false;
            }
        }
        focused = null;
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
