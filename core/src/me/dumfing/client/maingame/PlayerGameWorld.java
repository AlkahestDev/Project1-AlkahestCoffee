package me.dumfing.client.maingame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.esotericsoftware.kryonet.Client;

/**
 * Created by dumpl on 5/11/2017.
 */
public class PlayerGameWorld {
    private Client infoClient;
    public PlayerGameWorld(Client cl){
        this.infoClient = cl;
    }
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer){
        batch.begin();
        batch.end();
        shapeRenderer.begin();
        shapeRenderer.end();
    }
}
