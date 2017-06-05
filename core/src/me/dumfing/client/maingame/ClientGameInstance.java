package me.dumfing.client.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.DrawTools;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.multiplayerTools.*;

import java.util.ArrayList;
import java.util.HashMap;

import static me.dumfing.client.maingame.MainGame.DAGGER20S;
import static me.dumfing.client.maingame.MainGame.DAGGER30;
import static me.dumfing.client.maingame.MainGame.client;

/**
 * Created by dumpl on 5/15/2017.
 */
public class ClientGameInstance implements InputProcessor{
    private MultiplayerTools.ClientControlObject[] keysDown = new MultiplayerTools.ClientControlObject[10];
    private boolean keyUpdate = false;
    MultiplayerClient gameClient;
    private ConcurrentGameWorld playWorld;
    private OrthographicCamera camera;
    private AssetManager manager;
    private TextureRegion arrowTexture;
    private Array<BitmapFontCache> fonts;
    public ClientGameInstance(MultiplayerClient gameClient, HashMap<Integer, PlayerSoldier> players, OrthographicCamera camera, AssetManager manager, Array<BitmapFontCache> fonts){
        this.gameClient = gameClient;
        this.fonts = fonts;
        this.playWorld = new ConcurrentGameWorld(players);
        this.camera=camera;
        this.manager = manager;
        this.arrowTexture = MenuTools.mGTR("projectiles/arrow.png",manager);
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
        if(gameClient.isHasNewProjectileInfo()){
            playWorld.updateProjectiles(gameClient.getProjectiles());
        }
        playWorld.updatePlayerKeys(gameClient.getConnectionID(),keysDown);
        //System.out.println("updateplayworld");
        playWorld.update();
    }
    public PlayerSoldier getPlayer(int connectionID){
        return playWorld.getPlayers().get(connectionID);
    }
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, SpriteBatch uiBatch, ShapeRenderer uiShapeRenderer){
        batch.begin();
        for(PlayerSoldier p : playWorld.getPlayers().values()){
            //DrawTools.rec(renderer,p.getRect());
            p.draw(batch,clientSoldier().equals(p));
        }
        for(Projectile proj : playWorld.getProjectiles()){
            switch (proj.getProjectileType()){
                case 0:
                    proj.draw(batch,arrowTexture);
                    break;
            }
        }
        //batch.draw(playWorld.getMap().getVisualComponent(),0,0);
        batch.end();
        uiBatch.begin();
        drawHud(uiBatch,uiShapeRenderer, clientSoldier());
        for(BitmapFontCache bmfc : fonts){
            bmfc.draw(uiBatch);
        }
        uiBatch.end();
        playWorld.getMap().draw(batch);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for(PlayerSoldier playerSoldier : gameClient.getPlayers().values()){
            //DrawTools.rec(shapeRenderer,playerSoldier.getRect());
        }
        /*for(PlayerSoldier playerSoldier :gameClient.getPlayers().values()) {
            System.out.println(playerSoldier.getX()-playerSoldier.getCenterX());//playerSoldier.getMouseAngle());
            renderer.setColor(Color.RED);
            renderer.line(playerSoldier.getCenterX(),
                    playerSoldier.getCenterY(),
                    playerSoldier.getCenterX()+(5*(float)Math.cos(Math.toRadians(playerSoldier.getMouseAngle()))),
                    playerSoldier.getCenterY()+(5*(float)Math.sin(Math.toRadians(playerSoldier.getMouseAngle()))));
            renderer.setColor(Color.BLUE);
            DrawTools.rec(renderer, new Rectangle((int) (playerSoldier.getX()), (int) (playerSoldier.getY() + playerSoldier.getvY()), 1, 1));
        }*/
        shapeRenderer.end();
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
        keysDown[MultiplayerTools.Keys.ANGLE] = new MultiplayerTools.ClientControlObject(getPointerAngle(screenX,screenY));
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
        screenY=Gdx.graphics.getHeight()-screenY;
        keysDown[MultiplayerTools.Keys.ANGLE] = new MultiplayerTools.ClientControlObject(getPointerAngle(screenX,screenY));
        keyUpdate=true;
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
    public float getPointerAngle(float screenX, float screenY){
        float xLeg = screenX-(getPlayerOnscreenX()+(clientSoldier().getWidth()/2));
        float yLeg = screenY-(getPlayerOnscreenY()+(clientSoldier().getHeight()/2));
        return (float)Math.toDegrees(Math.atan2(yLeg,xLeg));
    }
    /**
     * Gets the client's player x coordinate relative to the bottom left of the screen
     * @return
     */
    public float getPlayerOnscreenX(){
        float playerScX = camera.viewportWidth/2-((camera.position.x-clientSoldier().getX())/camera.zoom);
        return playerScX;
    }

    /**
     * Gets the client's player y coordinate relative to the bottom left of the screen
     * @return
     */
    public float getPlayerOnscreenY(){
        float playerScY = camera.viewportHeight/2-((camera.position.y-clientSoldier().getY())/camera.zoom);
        return playerScY;
    }
    private void drawHud(Batch batch, ShapeRenderer shapeRenderer, PlayerSoldier center){
        fonts.get(DAGGER30).addText(center.getName(),5,Gdx.graphics.getHeight()-30);
        fonts.get(DAGGER30).addText(String.format("%2.2f %2.2f",center.getX(),center.getY()),5,Gdx.graphics.getHeight()-55);
    }
    private PlayerSoldier clientSoldier(){ // I can't be sure the pointer is always the same since the hashmap is always being updated from the server
        return playWorld.getPlayers().get(client.getConnectionID());
    }
}
