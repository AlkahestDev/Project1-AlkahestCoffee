package me.dumfing.client.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.menus.MenuBox;
import me.dumfing.multiplayerTools.*;

import java.util.HashMap;

import static me.dumfing.client.maingame.MainGame.DAGGER30;
import static me.dumfing.client.maingame.MainGame.DAGGER40;
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
    private TextureRegion arrowTexture, blueArrow, redArrow;
    private Array<BitmapFontCache> fonts;
    private boolean onlineMode = true;
    private MenuBox gameInfoBox;
    private static float HEALTH_BAR_HEIGHT = 40;
    public ClientGameInstance(MultiplayerClient gameClient, HashMap<Integer, PlayerSoldier> players, OrthographicCamera camera, AssetManager manager, Array<BitmapFontCache> fonts){
        this.gameClient = gameClient;
        this.fonts = fonts;
        this.playWorld = new ConcurrentGameWorld(players);
        this.camera=camera;
        this.manager = manager;
        this.arrowTexture = MenuTools.mGTR("projectiles/arrow.png",manager);
        this.redArrow = MenuTools.mGTR("redArrow.png",manager);
        this.blueArrow = MenuTools.mGTR("blueArrow.png",manager);
        onlineMode = true;
        setupGameInfoBox();
    }

    /**
     * This constructor is only to be used in offline mode
     * @param players
     * @param camera
     * @param manager
     * @param fonts
     */
    public ClientGameInstance(HashMap<Integer, PlayerSoldier> players, OrthographicCamera camera, AssetManager manager, Array<BitmapFontCache> fonts){
        this.fonts = fonts;
        this.playWorld = new ConcurrentGameWorld(players);
        this.camera = camera;
        this.manager = manager;
        this.arrowTexture = MenuTools.mGTR("projectiles/arrow.png",manager);
        onlineMode = false;
        this.redArrow = MenuTools.mGTR("redArrow.png",manager);
        this.blueArrow = MenuTools.mGTR("blueArrow.png",manager);
        setupGameInfoBox();
    }
    public void update(){
        if(keyUpdate){
            if(onlineMode) {
                gameClient.quickSend(new MultiplayerTools.ClientKeysUpdate(keysDown));
            }
            keyUpdate = false;
        }
        if(onlineMode) {
            if (gameClient.isHasNewPlayerInfo()) {
                playWorld.updatePlayers(gameClient.getPlayers());
            }
            if (gameClient.isHasNewProjectileInfo()) {
                playWorld.updateProjectiles(gameClient.getProjectiles());
            }
            if(gameClient.isHasNewFlagInfo()){
                playWorld.updateFlags(gameClient.getFlags());
            }
        }
        playWorld.updatePlayerKeys(onlineMode?gameClient.getConnectionID():0, keysDown);
        gameInfoBox.update();
        playWorld.update();
    }
    public PlayerSoldier getPlayer(int connectionID){
        return playWorld.getPlayers().get(connectionID);
    }
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, SpriteBatch uiBatch, ShapeRenderer uiShapeRenderer){
        batch.begin();
        playWorld.getMap().drawBG(batch,camera.position.x,camera.position.y);
        for(CaptureFlag flag : playWorld.getFlags()){
            flag.draw(batch,playWorld.getPlayers());
        }
        for(PlayerSoldier p : playWorld.getPlayers().values()){
            //DrawTools.rec(renderer,p.getRect());
            p.draw(batch,clientSoldier().equals(p));
            if(clientSoldier().equals(p)) {
                batch.draw(p.getTeam() == 0 ? redArrow : blueArrow, p.getX() + 0.3f, p.getY() + 2.1f, 0.4f, 0.4f);
            }
        }
        for(Projectile proj : playWorld.getProjectiles()){
            switch (proj.getProjectileType()){
                case 0:
                    proj.draw(batch,arrowTexture);
                    break;
            }
        }
        playWorld.getMap().draw(batch);
        playWorld.getMap().drawFG(batch,camera.position.x,camera.position.y);
        //batch.draw(playWorld.getMap().getVisualComponent(),0,0);
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if(onlineMode) {
            for (PlayerSoldier playerSoldier : gameClient.getPlayers().values()) {
                //DrawTools.rec(shapeRenderer,playerSoldier.getRect());
            }
        }
        shapeRenderer.end();
        // Draw Sprites for UI
        uiBatch.begin();
        drawHudSprites(uiBatch, clientSoldier());
        gameInfoBox.spriteDraw(uiBatch);
        uiBatch.end();
        // Draw shapes for UI
        uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawHudShapes(uiShapeRenderer,clientSoldier());
        gameInfoBox.shapeDraw(uiShapeRenderer);
        uiShapeRenderer.end();
        //Draw fonts for ui
        uiBatch.begin();
        for(BitmapFontCache bmfc : fonts){
            bmfc.draw(uiBatch);
        }
        uiBatch.end();
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
        PlayerSoldier clientPlayer = getPlayer(onlineMode?gameClient.getConnectionID():0);
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

    private void drawHudSprites(Batch batch, PlayerSoldier center){
        fonts.get(DAGGER30).addText(center.getName(),5,25+HEALTH_BAR_HEIGHT);
        fonts.get(DAGGER30).addText(Integer.toString(center.getHealth()),5,25);
        fonts.get(DAGGER30).addText(String.format("[WHITE]%2.2f %2.2f",center.getX(),center.getY()),5,Gdx.graphics.getHeight()-55);
        fonts.get(DAGGER40).addText(String.format("[WHITE]%d",playWorld.getBluScore()),400,Gdx.graphics.getHeight()-10);
        fonts.get(DAGGER40).addText(String.format("[WHITE]%d",playWorld.getRedScore()),Gdx.graphics.getWidth()-420,Gdx.graphics.getHeight()-10);
    }
    private void drawHudShapes(ShapeRenderer shapeRenderer, PlayerSoldier center){
        float gHt = Gdx.graphics.getHeight();
        float gWh = Gdx.graphics.getWidth();
        float nameWidth = MenuTools.textWidth(fonts.get(DAGGER30).getFont(),center.getName());
        float healthBarPercent = (Math.min((float)center.getHealth()+5,(float)center.getMaxHealth()-5))/((float)center.getMaxHealth()-5);
        float healthTrianglePercent = Math.max((((float)center.getHealth()-95)/((float)center.getMaxHealth()-95)),0);
        if(center.getHealth() ==0){
            healthBarPercent = 0;
        }
        //System.out.printf("%f %f %f\n",healthBarPercent,(Math.min((float)center.getHealth()+5,(float)center.getMaxHealth()-5)),((float)center.getMaxHealth()-5));
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setColor(1,0.2f,0.2f,0.6f);
        //Red flag background triangle
        shapeRenderer.triangle(355,gHt+1,425,gHt+1,425,gHt-50);
        shapeRenderer.setColor(0.2f,0.2f,1,0.6f);
        //Blue flag background triangle
        shapeRenderer.triangle(gWh-355,gHt+1,gWh-425,gHt+1,gWh-425,gHt-51);
        shapeRenderer.setColor(0.2f,0.2f,0.2f,0.6f);
        shapeRenderer.rect(425,gHt-50,gWh-(425*2),70);
        //player info
        shapeRenderer.rect(0,HEALTH_BAR_HEIGHT,nameWidth+5,40);
        shapeRenderer.triangle(nameWidth+5,40+HEALTH_BAR_HEIGHT,nameWidth+45,HEALTH_BAR_HEIGHT,nameWidth+5,HEALTH_BAR_HEIGHT);
        shapeRenderer.triangle(270, HEALTH_BAR_HEIGHT, 270 + HEALTH_BAR_HEIGHT, 0, 270, 0);
        //player health
        shapeRenderer.rect(0,0,270,HEALTH_BAR_HEIGHT);
        shapeRenderer.setColor(1-healthBarPercent,healthBarPercent,0.2f,0.4f);
        shapeRenderer.rect(0,0,270f*healthBarPercent,HEALTH_BAR_HEIGHT);
        //little triangle bit at the end of the health
        shapeRenderer.rect(270,0,HEALTH_BAR_HEIGHT*healthTrianglePercent,HEALTH_BAR_HEIGHT*(1-healthTrianglePercent));
        shapeRenderer.triangle(270,HEALTH_BAR_HEIGHT*(1-healthTrianglePercent),270,HEALTH_BAR_HEIGHT,270+HEALTH_BAR_HEIGHT*healthTrianglePercent,HEALTH_BAR_HEIGHT*(1-healthTrianglePercent));
    }
    private PlayerSoldier clientSoldier(){ // I can't be sure the pointer is always the same since the hashmap is always being updated from the server
        return playWorld.getPlayers().get(onlineMode?client.getConnectionID():0);
    }
    private void setupGameInfoBox(){
        gameInfoBox = new MenuBox(Gdx.graphics.getWidth()-200,Gdx.graphics.getHeight()-50,200,50,fonts);
        gameInfoBox.setBackground(MenuTools.mGTR("simpleBG.png",manager));
    }
}
