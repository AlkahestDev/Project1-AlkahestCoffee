package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import me.dumfing.gdxtools.DrawTools;
import me.dumfing.gdxtools.MenuObject;
import me.dumfing.gdxtools.MenuTools;

import java.util.LinkedList;

/**
 *  TODO: Make TextBoxes clickable and usable<br>
 * MenuBox.java<br>
 * Aaron Li<br>
 * Works like a menu but has the ability to be added to a menu to be drawn and can move around
 * will have it's own sprites, textboxes, buttons, etc.
 * Won't have animated image backgrounds
 */
public class MenuBox extends MenuObject{
    private LinkedList<MenuTools.Button> buttons;
    private TextureRegion background; // all images used for background
    private LinkedList<MenuTools.TextureRect> images; // sprites for images, they can have their own position and texture
    private LinkedList<MenuTools.TextField> textFields;
    private LinkedList<MenuTools.ColourRect> colRects;
    private LinkedList<MenuTools.QueueText> textToDraw;
    private BitmapFontCache textCache;
    private MenuTools.TextField focused; // the textbox that the user will be typing into
    private float vX, vY;

    /**
     * Constructor method for the MenuBox
     * @param x The x position of the MenuBox
     * @param y The y position of the MenuBox
     * @param w The width of the MenuBox
     * @param h The height of the MenuBox
     * @param bmfc The BitMapFontCache used to draw text in the MenuBox
     */
    public MenuBox(float x, float y, float w, float h, BitmapFontCache bmfc){
        super(x,y,w,h);
        this.buttons = new LinkedList<MenuTools.Button>();
        this.images = new LinkedList<MenuTools.TextureRect>();
        this.textFields = new LinkedList<MenuTools.TextField>();
        this.colRects = new LinkedList<MenuTools.ColourRect>();
        this.textToDraw = new LinkedList<MenuTools.QueueText>();
        this.textCache = bmfc;
        this.vX = 0;
        this.vY = 0;
    }
    public void addTextField(MenuTools.TextField tfIn){
        tfIn.translate(super.getRect().x,super.getRect().y);
        this.textFields.add(tfIn);
    }
    public void addQueueText(MenuTools.QueueText qtIn){
        qtIn.translate(super.getRect().x,super.getRect().y);
        this.textToDraw.add(qtIn);
    }
    /**
     * Sets the background for the MenuBox
     * @param bg The background to be used
     */
    public void setBackground(TextureRegion bg){
        this.background = bg;
    }

    /**
     * Updates all elements in the MenuBox that need to be updated
     */
    public void update(MenuTools.TextField focused){
        super.translate(this.vX,this.vY);
        vX = MenuTools.towardsZero(vX);
        vY = MenuTools.towardsZero(vY);
        for(MenuTools.Button bt : buttons){ //check all the buttons if they're currently pressed and the mouse is hovering over them
            bt.setPressed(bt.collidepoint(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY()) && Gdx.input.isButtonPressed(0)); // if the button is colliding with the mouse and the mouse is left clicking, make it look like it's pressed
        }
        for(MenuTools.TextField tb : textFields){ // go through all TextBoxes in the Menu
            tb.update(textCache,focused ==tb); // update the textbox
        }
        for(MenuTools.ColourRect cR : colRects){
            cR.update();
        }
        for(MenuTools.QueueText qt : textToDraw){
            qt.update();
        }
    }

    /**
     * Draws all shape related portions of the MenuBox
     * @param sr The ShapeRenderer used to draw shapes
     *
     */
    public void shapeDraw(ShapeRenderer sr, MenuTools.TextField focused){
        //DrawTools.rec(sr,this.menuArea);
        for(MenuTools.ColourRect cR : colRects){
            cR.draw(sr);
        }
        for(MenuTools.TextField tb : textFields){ // TextBoxes look like two rectangles drawn on eachother
            tb.draw(sr,textCache.getFont(),tb == focused); // draw the textBox, if it's the focused one then it'll have the cursor being drawn
        }
        for(MenuTools.QueueText qt : this.textToDraw){
            qt.queue(textCache);
        }
    }

    /**
     * Draws all texture related portions of the MenuBox
     * @param sb The SpriteBatch used to render the textures
     */
    public void spriteDraw(SpriteBatch sb){
        DrawTools.textureRect(sb,super.getRect(),this.background);
        for(MenuTools.Button bt : this.buttons) { // check all the buttons in the Menu
            if (bt.getClicked()) { // if the button has been clicked
                sb.draw(bt.getPressed(), bt.getButtonArea().x, bt.getButtonArea().y, bt.getButtonArea().width, bt.getButtonArea().height); // draw the clicked button
            } else {
                sb.draw(bt.getUnpressed(), bt.getButtonArea().x, bt.getButtonArea().y, bt.getButtonArea().width, bt.getButtonArea().height); // draw the unclicked button
            }
        }
        for(MenuTools.QueueText qt : this.textToDraw){
            qt.queue(textCache);
        }
        for(MenuTools.TextureRect sp : images){ // draw all Sprites in the Menu
            sp.spriteDraw(sb);
        }
    }

    /**
     * Returns the first TextField that a given point is hovering over
     * @param mX The x position of the point
     * @param mY The y position of the point
     * @return The first TextField under the point
     */
    public MenuTools.TextField textFieldsClicked(float mX, float mY){
        for(MenuTools.TextField tf : textFields){
            if(tf.collidePoint(mX,mY)){
                return tf;
            }
        }
        return null;
    }
    public void setVelocity(float x, float y){
        this.vX = x;
        this.vY = y;
        for(MenuTools.TextField tf: textFields){
            tf.setVelocity(x,y);
        }
        for(MenuTools.Button bt : buttons){
            bt.setVelocity(x, y);
        }
        for(MenuTools.TextureRect sp : images){
            sp.setVelocity(x,y);
        }
        for(MenuTools.ColourRect cr : colRects){
            cr.setVelocity(x,y);
        }
        for(MenuTools.QueueText qt : textToDraw){
            qt.setVelocity(x,y);
        }
    }
}
