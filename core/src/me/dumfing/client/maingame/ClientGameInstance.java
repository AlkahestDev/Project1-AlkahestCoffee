package me.dumfing.client.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
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
    private MultiplayerTools.ClientControlObject[] keysDown = new MultiplayerTools.ClientControlObject[10];
    private boolean keyUpdate = false;
    MultiplayerClient gameClient;
    private ConcurrentGameWorld playWorld;
    private OrthographicCamera camera;
    public ClientGameInstance(MultiplayerClient gameClient, HashMap<Integer, PlayerSoldier> players, OrthographicCamera camera){
        this.gameClient = gameClient;
        this.playWorld = new ConcurrentGameWorld(players);
        this.camera=camera;
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
            //System.out.println("receive info");
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
        for(PlayerSoldier p : playWorld.getPlayers().values()){
            //DrawTools.rec(renderer,p.getRect());
            p.draw(batch,playWorld.getPlayers().get(gameClient.getConnectionID()).equals(p));
        }
        //batch.draw(playWorld.getMap().getVisualComponent(),0,0);
        batch.end();
        playWorld.getMap().draw(batch);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
            for(PlayerSoldier playerSoldier :gameClient.getPlayers().values()) {
                System.out.println(playerSoldier.getMouseAngle());
                renderer.line(playerSoldier.getX(),playerSoldier.getY(),playerSoldier.getX()+(5*(float)Math.cos(Math.toRadians(playerSoldier.getMouseAngle()))),playerSoldier.getY()+(5*(float)Math.sin(Math.toRadians(playerSoldier.getMouseAngle()))));
                DrawTools.rec(renderer, new Rectangle((int) (playerSoldier.getX()), (int) (playerSoldier.getY() + playerSoldier.getvY()), 1, 1));
            }
            renderer.setColor(Color.RED);
        renderer.end();
    }
    public void pickWorld(int worldID){
        playWorld.setWorld(MainGame.worldMaps[MainGame.DEBUGWORLD]);
    }
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.W :
                keysDown[MultiplayerTools.Keys.W] = new MultiplayerTools.ClientControlObject(false);
                break;
            case Input.Keys.A:
                keysDown[MultiplayerTools.Keys.A] = new MultiplayerTools.ClientControlObject(false);
                break;
            case Input.Keys.S:
                keysDown[MultiplayerTools.Keys.S] = new MultiplayerTools.ClientControlObject(false);
                break;
            case Input.Keys.D:
                keysDown[MultiplayerTools.Keys.D] = new MultiplayerTools.ClientControlObject(false);
                break;
            case Input.Keys.SPACE:
                keysDown[MultiplayerTools.Keys.SPACE] = new MultiplayerTools.ClientControlObject(false);
                break;
            case Input.Keys.SHIFT_LEFT:
                keysDown[MultiplayerTools.Keys.SHIFT] = new MultiplayerTools.ClientControlObject(false);
                break;
            case Input.Keys.CONTROL_LEFT:
                keysDown[MultiplayerTools.Keys.CONTROL] = new MultiplayerTools.ClientControlObject(false);
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
                keysDown[MultiplayerTools.Keys.W] = new MultiplayerTools.ClientControlObject(true);
                break;
            case Input.Keys.A:
                keysDown[MultiplayerTools.Keys.A] = new MultiplayerTools.ClientControlObject(true);
                break;
            case Input.Keys.S:
                keysDown[MultiplayerTools.Keys.S] = new MultiplayerTools.ClientControlObject(true);
                break;
            case Input.Keys.D:
                keysDown[MultiplayerTools.Keys.D] = new MultiplayerTools.ClientControlObject(true);
                break;
            case Input.Keys.SPACE:
                keysDown[MultiplayerTools.Keys.SPACE] = new MultiplayerTools.ClientControlObject(true);
                break;
            case Input.Keys.SHIFT_LEFT:
                keysDown[MultiplayerTools.Keys.SHIFT] = new MultiplayerTools.ClientControlObject(true);
                break;
            case Input.Keys.CONTROL_LEFT:
                keysDown[MultiplayerTools.Keys.CONTROL] = new MultiplayerTools.ClientControlObject(true);
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
        PlayerSoldier clientPlayer = getPlayer(gameClient.getConnectionID());
        screenY = Gdx.graphics.getHeight()-screenY;
        if(button == 0){
            keysDown[MultiplayerTools.Keys.LMB] = new MultiplayerTools.ClientControlObject(true);
            keyUpdate = true;
        }
        if(button == 1){
            keysDown[MultiplayerTools.Keys.RMB] = new MultiplayerTools.ClientControlObject(true);
            keyUpdate = true;
        }
        float xLeg = screenX-getPlayerOnscreenX();
        float yLeg = screenY-getPlayerOnscreenY();
        float mAngle=(float)Math.toDegrees(Math.atan2(yLeg,xLeg));
        keysDown[MultiplayerTools.Keys.ANGLE] = new MultiplayerTools.ClientControlObject(mAngle);
        return false;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button == 0){
            keysDown[MultiplayerTools.Keys.LMB] = new MultiplayerTools.ClientControlObject(false);
            keyUpdate = true;
        }
        if(button == 1){
            keysDown[MultiplayerTools.Keys.RMB] = new MultiplayerTools.ClientControlObject(false);
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

    /**
     * Gets the client's player x coordinate relative to the bottom left of the screen
     * @return
     */
    public float getPlayerOnscreenX(){
        float playerScX = camera.viewportWidth/2-((camera.position.x-playWorld.getPlayers().get(gameClient.getConnectionID()).getX())/camera.zoom);
        return playerScX;
    }

    /**
     * Gets the client's player y coordinate relative to the bottom left of the screen
     * @return
     */
    public float getPlayerOnscreenY(){
        float playerScY = camera.viewportHeight/2-((camera.position.y-playWorld.getPlayers().get(gameClient.getConnectionID()).getY())/camera.zoom);
        return playerScY;
    }
}
