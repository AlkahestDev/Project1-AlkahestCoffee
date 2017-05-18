package me.dumfing.client.maingame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.dumfing.gdxtools.DrawTools;
import me.dumfing.multiplayerTools.ConcurrentGameWorld;
import me.dumfing.multiplayerTools.MultiplayerClient;
import me.dumfing.multiplayerTools.MultiplayerTools;
import me.dumfing.multiplayerTools.PlayerSoldier;

import java.util.HashMap;

/**
 * Created by dumpl on 5/15/2017.
 */
public class ClientGameInstance implements InputProcessor{
    private boolean[] keysDown = new boolean[8];
    private boolean keyUpdate = false;
    MultiplayerClient gameClient;
    private ConcurrentGameWorld playWorld;
    public ClientGameInstance(MultiplayerClient gameClient, HashMap<Integer, PlayerSoldier> players){
        this.gameClient = gameClient;
        this.playWorld = new ConcurrentGameWorld(players);
    }
    public void update(){
        if(keyUpdate){
            //System.out.println("send keys");
            gameClient.quickSend(new MultiplayerTools.ClientKeysUpdate(keysDown));
            keyUpdate = false;
        }
        if(gameClient.isHasNewPlayerInfo()) {
            //System.out.println("new client info");
            //System.out.println(gameClient.getPlayers().values());
            System.out.println("receive info");
            playWorld.updatePlayers(gameClient.getPlayers());
        }
        playWorld.updatePlayerKeys(gameClient.getConnectionID(),keysDown);
        //System.out.println("updateplayworld");
        playWorld.update();
    }
    public PlayerSoldier getPlayer(int connectionID){
        return playWorld.getPlayers().get(connectionID);
    }
    public void draw(SpriteBatch batch, ShapeRenderer renderer){
        batch.begin();
        //batch.draw(playWorld.getMap().getVisualComponent(),0,0);
        batch.end();
        playWorld.getMap().draw(batch);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        for(PlayerSoldier p : playWorld.getPlayers().values()){
            DrawTools.rec(renderer,p.getRect());
        }
        renderer.end();
    }
    public void pickWorld(int worldID){
        playWorld.setWorld(MainGame.worldMaps[MainGame.DEBUGWORLD]);
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
    public boolean keyDown(int keycode) {
        System.out.println("Key down");
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
        if(button == 0){
            keysDown[MultiplayerTools.Keys.LMB] = false;
            keyUpdate = true;
        }
        if(button == 1){
            keysDown[MultiplayerTools.Keys.RMB] = false;
            keyUpdate = true;
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
