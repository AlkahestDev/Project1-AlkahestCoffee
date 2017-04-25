package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
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
    public void shapeDraw(ShapeRenderer sr){

    }
    public void spriteDraw(SpriteBatch sb){

    }
}
