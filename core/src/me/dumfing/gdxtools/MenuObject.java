package me.dumfing.gdxtools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import static me.dumfing.gdxtools.MathTools.towardsZero;


/**
 * An element that exists on a menu
 */
public class MenuObject {
    Rectangle shape;
    float vX = 0;
    float vY = 0;
    boolean executed = true;
    Runnable onDoneMoving;
    public MenuObject(float x, float y, float w, float h){
        shape = new Rectangle(x,y,w,h);
    }

    /**
     * Draws the object
     * @param sb SpriteBatch to be used for drawing
     * @param sr ShapeRenderer to be used for drawing
     */
    public void draw(SpriteBatch sb, ShapeRenderer sr){


    }

    /**
     * Draws the texture related components of the menu object
     * @param sb The Spritebatch to be used for drawing
     */
    public void spriteDraw(SpriteBatch sb){

    }

    /**
     * Draws the shape related components of the menu object
     * @param sr The ShapeRenderer to be used for drawing
     */
    public void shapeDraw(ShapeRenderer sr){

    }
    /**
     * Moves the object with it's velocity<br>
     * REMEMBER TO ADD EVERYTHING TO THE MENUBOX BEFORE SETTING IT'S VELOCITY
     */
    public void update(){
        this.translate(vX,vY);
        //System.out.println(this.vX+" "+this.vY);
        this.vX = towardsZero(vX, 0.5f);
        this.vY = towardsZero(vY, 0.5f);
        if(this.vX+this.vY == 0 && this.onDoneMoving!=null && !executed){
            this.onDoneMoving.run();
            executed = true;
        }
    }
    public float getvX(){
        return this.vX;
    }
    public float getvY(){
        return this.vY;
    }
    /**
     * Sets the velocity of the object
     * @param x The x component of the velocity
     * @param y The y component of the velocity
     */
    public void setVelocity(float x, float y){
        this.vX = x;
        this.vY = y;
    }

    /**
     * Moves the object a given x and y amount
     * @param x The amount to move the object in the x axis (negative for left)
     * @param y The amount to move the object in the y axis (negative for down)
     */
    public void translate(float x, float y){
        this.shape.x+=x;
        this.shape.y+=y;
    }

    public void setOnDoneMoving(Runnable onDoneMoving) {
        this.onDoneMoving = onDoneMoving;
        executed = false;
    }

    public Rectangle getRect(){
        return this.shape;
    }
}
