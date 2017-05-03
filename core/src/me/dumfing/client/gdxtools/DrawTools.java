package me.dumfing.client.gdxtools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by dumpl on 4/20/2017.
 */
public class DrawTools {
    /**
     * Draws a rectangle using a ShapeRenderer and a Rectangle
     * @param sr ShapeRenderer used to draw the rectangle
     * @param rect Rectangular area used to be drawn
     */
    public static void rec(ShapeRenderer sr, Rectangle rect){
        sr.rect(rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight());
    }

    /**
     * Draws a Texture with the SpriteBatch, stretching it to the area of the Rectangle.
     * @param sb SpriteBatch used to draw the Texture
     * @param rect Rectangular area to draw the Texture over
     * @param text The Texture to be drawn
     */
    public static void textureRect(SpriteBatch sb, Rectangle rect, Texture text){
        sb.draw(text,rect.x,rect.y,rect.width,rect.height);
    }

    /**
     * Draws a TextureRegion with the SpriteBatch, stretching it to the area of the Rectangle.
     * @param sb SpriteBatch used to draw the TextureRegion
     * @param rect Rectangular area to draw the TextureRegion over
     * @param text The TextureRegion to be drawn
     */
    public static void textureRect(SpriteBatch sb, Rectangle rect, TextureRegion text){
        sb.draw(text,rect.x,rect.y,rect.width,rect.height);
    }
}
