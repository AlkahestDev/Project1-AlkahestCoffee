package me.dumfing.client.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.dumfing.gdxtools.DrawTools;
import me.dumfing.multiplayerTools.MultiplayerClient;
import me.dumfing.multiplayerTools.MultiplayerTools;

import java.util.Arrays;

/**
 * Created by dumpl on 5/11/2017.
 */
public class ClientGameWorld implements InputProcessor{
    private MultiplayerClient infoClient;
    private boolean[] keysDown;
    private TextureRegion temp = new TextureRegion(new Texture(Gdx.files.internal("pixmapVisual.png")));
    private boolean keyUpdate = false;
    public ClientGameWorld(MultiplayerClient cl){
        this.infoClient = cl;
        keysDown = new boolean[8];
        Arrays.fill(keysDown,false);
    }
    public void update(){
        if(keyUpdate){
            infoClient.quickSend(new MultiplayerTools.ClientKeysUpdate(keysDown));
            keyUpdate=false;
        }
        for(MultiplayerTools.ServerPlayerInfo p : infoClient.getPlayers().values()){
            System.out.println(p.getvX()+" "+p.getvY());
            movePlayer(p);
        }
    }
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer){
        batch.begin();
        batch.draw(temp,0,0);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(MultiplayerTools.ServerPlayerInfo p : infoClient.getPlayers().values()){
            DrawTools.rec(shapeRenderer,p.getRect());
        }
        shapeRenderer.end();
    }
    private void movePlayer(MultiplayerTools.ServerPlayerInfo p){
        //p.translateX(p.getvX());
        if(p.getvY()>0){
            p.setvY(p.getvY()+MultiplayerTools.GRAVITY);
        }
        p.translate(p.getvX(),p.getvY());
    }
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.W :
                keysDown[MultiplayerTools.Keys.W] = true;
                break;
            case Input.Keys.A:
                keysDown[MultiplayerTools.Keys.A] = true;
                break;
            case Input.Keys.S:
                keysDown[MultiplayerTools.Keys.S] = true;
                break;
            case Input.Keys.D:
                keysDown[MultiplayerTools.Keys.D] = true;
                break;
            case Input.Keys.SPACE:
                keysDown[MultiplayerTools.Keys.SPACE] = true;
                break;
            case Input.Keys.SHIFT_LEFT:
                keysDown[MultiplayerTools.Keys.SHIFT] = true;
                break;
            case Input.Keys.CONTROL_LEFT:
                keysDown[MultiplayerTools.Keys.CONTROL] = true;
                break;
        }
        keyUpdate = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.W :
                keysDown[MultiplayerTools.Keys.W] = false;
                break;
            case Input.Keys.A:
                keysDown[MultiplayerTools.Keys.A] = false;
                break;
            case Input.Keys.S:
                keysDown[MultiplayerTools.Keys.S] = false;
                break;
            case Input.Keys.D:
                keysDown[MultiplayerTools.Keys.D] = false;
                break;
            case Input.Keys.SPACE:
                keysDown[MultiplayerTools.Keys.SPACE] = false;
                break;
            case Input.Keys.SHIFT_LEFT:
                keysDown[MultiplayerTools.Keys.SHIFT] = false;
                break;
            case Input.Keys.CONTROL_LEFT:
                keysDown[MultiplayerTools.Keys.CONTROL] = false;
                break;
        }
        keyUpdate = true;
        //infoClient.sendUDP(new MultiplayerTools.ClientKeysUp(keysDown));
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == 0){
            keysDown[MultiplayerTools.Keys.LMB] = true;
            keyUpdate = true;
        }
        if(button == 1){
            keysDown[MultiplayerTools.Keys.RMB] = true;
            keyUpdate = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
