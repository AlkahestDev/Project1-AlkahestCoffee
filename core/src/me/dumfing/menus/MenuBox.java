package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.DrawTools;
import me.dumfing.gdxtools.MenuTools;

import java.util.LinkedList;

/** TODO: Drawing methods (Spritedraw and Shapedraw)
 *  TODO: Make all elements on MenuBox work
 * MenuBox.java
 * Aaron Li
 * Works like a menu but has the ability to be added to a menu to be drawn and can move around
 * will have it's own sprites, textboxes, buttons, etc.
 * Won't have animated image backgrounds
 */
public class MenuBox {
    private Rectangle menuArea;
    private LinkedList<MenuTools.Button> buttons;
    private TextureRegion background; // all images used for background
    private LinkedList<Sprite> images; // sprites for images, they can have their own position and texture
    private LinkedList<MenuTools.TextBox> textBoxes;
    private LinkedList<MenuTools.ColourRect> colRects;
    private BitmapFontCache textCache;
    private MenuTools.TextBox focused; // the textbox that the user will be typing into
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
        this.menuArea = new Rectangle(w,y,w,h);
        this.buttons = new LinkedList<MenuTools.Button>();
        this.images = new LinkedList<Sprite>();
        this.textBoxes = new LinkedList<MenuTools.TextBox>();
        this.colRects = new LinkedList<MenuTools.ColourRect>();
        this.textCache = bmfc;
        this.vX = 0;
        this.vY = 0;
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
    public void update(){
        for(MenuTools.Button bt : buttons){ //check all the buttons if they're currently pressed and the mouse is hovering over them
            bt.setPressed(bt.collidepoint(Gdx.input.getX(),Gdx.graphics.getHeight()-Gdx.input.getY()) && Gdx.input.isButtonPressed(0)); // if the button is colliding with the mouse and the mouse is left clicking, make it look like it's pressed
        }
        for(MenuTools.TextBox tb : textBoxes){ // go through all TextBoxes in the Menu
            tb.update(textCache,focused ==tb); // update the textbox
        }
        for(MenuTools.ColourRect cR : colRects){
            cR.update();
        }
    }

    /**
     * Draws all shape related portions of the MenuBox
     * @param sr The ShapeRenderer used to draw shapes
     *
     */
    public void shapeDraw(ShapeRenderer sr){
        for(MenuTools.ColourRect cR : colRects){
            cR.draw(sr);
        }
        for(MenuTools.TextBox tb : textBoxes){ // TextBoxes look like two rectangles drawn on eachother
            tb.draw(sr,textCache.getFont(),tb == focused); // draw the textBox, if it's the focused one then it'll have the cursor being drawn
        }
    }

    /**
     * Draws all texture related portions of the MenuBox
     * @param sb The SpriteBatch used to render the textures
     */
    public void spriteDraw(SpriteBatch sb){
        DrawTools.textureRect(sb,this.menuArea,this.background);
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
    }
}
