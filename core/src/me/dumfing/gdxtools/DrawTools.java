package me.dumfing.gdxtools;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by dumpl on 4/20/2017.
 */
public class DrawTools {
    public static void rec(ShapeRenderer sr, Rectangle rect){
        sr.rect(rect.getX(),rect.getY(),rect.getWidth(),rect.getHeight());
    }
}
