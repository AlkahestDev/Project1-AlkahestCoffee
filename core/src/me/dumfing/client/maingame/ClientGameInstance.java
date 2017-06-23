package me.dumfing.client.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.menus.MenuBox;
import me.dumfing.multiplayerTools.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import static me.dumfing.client.maingame.MainGame.*;
import static me.dumfing.multiplayerTools.ConcurrentGameWorld.BOW;
import static me.dumfing.multiplayerTools.ConcurrentGameWorld.SWORD;

/**
 * Created by dumpl on 5/15/2017.
 */
public class ClientGameInstance implements InputProcessor{
    private MultiplayerTools.ClientControlObject[] keysDown = new MultiplayerTools.ClientControlObject[10];
    private boolean keyUpdate = false;
    private MultiplayerClient gameClient;
    private ConcurrentGameWorld playWorld;
    private OrthographicCamera camera;
    private AssetManager manager;
    private TextureRegion arrowTexture, blueArrow, redArrow;
    private Array<BitmapFontCache> fonts;
    private boolean onlineMode = true;
    private MenuBox pauseBox;
    private boolean boxOut = false;
    private static float HEALTH_BAR_HEIGHT = 40;
    private Array<ParticleEffectPool.PooledEffect> effects = new Array<ParticleEffectPool.PooledEffect>();
    private HashSet<String> startedEffects = new HashSet<String>();
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
        setupPauseMenuBox();
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
        setupPauseMenuBox();
    }
    public void update(){
        for(GridPoint2 hit : playWorld.getHits()){
            PlayerSoldier hitPlayer = playWorld.getPlayers().get(hit.y);
            addBloodParticle(hitPlayer.getX()+hitPlayer.getWidth()/2f,hitPlayer.getY()+hitPlayer.getHeight()/2f);
            MainGame.swordHit.play();
        }
        playWorld.clearHits();
        for(Projectile arrow : playWorld.getProjectiles()){
            if(!arrow.isParticlesStarted() && arrow.isHit()){
                if(arrow.getAttackPair().y!=-1) {
                    if(!startedEffects.contains(arrow.getServerHash())) {
                        PlayerSoldier hitTarget = playWorld.getPlayers().get(arrow.getAttackPair().y);
                        addBloodParticle(hitTarget.getX() + hitTarget.getWidth() / 2f, hitTarget.getY() + hitTarget.getHeight() / 2f);
                        arrow.setParticlesStarted(true);
                        startedEffects.add(arrow.getServerHash());
                        MainGame.arrowHit.play();
                    }
                }
            }
        }
        startedEffects.retainAll(existingHashes());
        //effectHandled.retainAll(playWorld.getProjectiles());
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
            if(gameClient.isHasNewRespawnInfo()){
                playWorld.updateRespawnTimes(gameClient.getRespawnTimes());
            }
            if(gameClient.isHasNewKillInfo()){
                playWorld.updateKillInfo(gameClient.getKillLog());
            }
        }
        for(Vector3 partInfo : playWorld.getParticles()){
            int team = (int)partInfo.z;
            if(team == 0){
                MainGame.redFlagCap.reset();
                MainGame.redFlagCap.setPosition(partInfo.x,partInfo.y);
                MainGame.redFlagCap.start();
                MainGame.redFlagCap.update(3.5f);
                MainGame.flagCap.play();
            }
            else{
                MainGame.bluFlagCap.reset();
                MainGame.bluFlagCap.setPosition(partInfo.x,partInfo.y);
                MainGame.bluFlagCap.start();
                MainGame.bluFlagCap.update(3.5f); // there's a 3.5 second delay for some reason. We offset the particle's timer by 3.5 to compensate
                MainGame.flagCap.play();
            }
        }
        playWorld.updatePlayerKeys(onlineMode?gameClient.getConnectionID():0, keysDown);
        pauseBox.update();
        playWorld.update();
    }
    public PlayerSoldier getPlayer(int connectionID){
        return playWorld.getPlayers().get(connectionID);
    }
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, SpriteBatch uiBatch, ShapeRenderer uiShapeRenderer){
        float deltaTime = Gdx.graphics.getDeltaTime();
        batch.begin();
        playWorld.getWorldMap().drawBG(batch,camera.position.x,camera.position.y);
        playWorld.getWorldMap().drawBGMedium(batch,camera.position.x,camera.position.y);
        playWorld.getWorldMap().drawBGClose(batch);
        for(CaptureFlag flag : playWorld.getFlags()){
            flag.draw(batch,playWorld.getPlayers());
        }
        for(PlayerSoldier p : playWorld.getPlayers().values()){
            //DrawTools.rec(renderer,p.getRect());
            if(p.isAlive()) {
                p.draw(batch);
                if (clientSoldier().equals(p)) {
                    batch.draw(p.getTeam() == 0 ? redArrow : blueArrow, p.getX() + 0.3f, p.getY() + 2.1f, 0.4f, 0.4f);
                }
            }
        }
        for(Projectile proj : playWorld.getProjectiles()){
            switch (proj.getProjectileType()){
                case 0:
                    proj.draw(batch,arrowTexture);
                    break;
            }
        }
        for(ParticleEffectPool.PooledEffect effectTemp : effects){
            effectTemp.draw(batch,deltaTime);
            if(effectTemp.isComplete()){
                effectTemp.free();
                effects.removeValue(effectTemp,true);
            }
        }
        playWorld.getWorldMap().draw(batch);
        //if(!MainGame.bluFlagCap.isComplete()){
            bluFlagCap.draw(batch,deltaTime);
        //}
        //if(!MainGame.redFlagCap.isComplete()){
            redFlagCap.draw(batch,deltaTime);
        //}
        playWorld.getWorldMap().drawFG(batch,camera.position.x,camera.position.y);
        //batch.draw(playWorld.getWorldMap().getVisualComponent(),0,0);
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
        if(clientSoldier().isAlive()) {
            drawHudSprites(uiBatch, clientSoldier());
        }
        else{
            drawDeathSprites(uiBatch,clientSoldier());
        }
        pauseBox.spriteDraw(uiBatch);
        uiBatch.end();
        // Draw shapes for UI
        uiShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        if(clientSoldier().isAlive()) {
            drawHudShapes(uiShapeRenderer, clientSoldier());
        }
        pauseBox.shapeDraw(uiShapeRenderer);
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
            case Input.Keys.ESCAPE:
                if(boxOut){
                    pauseBox.setPos(Gdx.graphics.getWidth()/2-pauseBox.getRect().width/2,Gdx.graphics.getHeight()/2-pauseBox.getRect().height/2);
                    pauseBox.setVelocity(0,-30);
                    boxOut = false;
                }
                else{
                    pauseBox.setVelocity(0,30);
                    boxOut = true;
                }
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
        pauseBox.checkButtonsPressed(screenX,screenY);
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
        float freeAng = (getPointerAngle(screenX,screenY)+360)%360;
        keysDown[MultiplayerTools.Keys.ANGLE] = new MultiplayerTools.ClientControlObject(freeAng);
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
        float freeDeg = (float)Math.toDegrees(Math.atan2(yLeg,xLeg)); // angle, not limited by facing direction
        return freeDeg;
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

    private void drawHudSprites(SpriteBatch batch, PlayerSoldier center){
        fonts.get(DAGGER30).addText(center.getName(),5,25+HEALTH_BAR_HEIGHT);
        fonts.get(DAGGER30).addText(Integer.toString(center.getHealth()),5,25);
        fonts.get(DAGGER40).addText(String.format("[WHITE]%d",playWorld.getRedScore()),400,Gdx.graphics.getHeight()-10);
        fonts.get(DAGGER40).addText(String.format("[WHITE]%d",playWorld.getBluScore()),Gdx.graphics.getWidth()-420,Gdx.graphics.getHeight()-10);
        fonts.get(DAGGER30).addText(String.format("[WHITE]Kills: %d",clientSoldier().getKills()),430,Gdx.graphics.getHeight()-15);
        String deathsText = "[WHITE]Deaths: "+clientSoldier().getDeaths();
        float deathsWidth = MenuTools.textWidth(fonts.get(DAGGER30).getFont(),deathsText);
        fonts.get(DAGGER30).addText(deathsText,Gdx.graphics.getWidth()-430-deathsWidth,Gdx.graphics.getHeight()-15);
        int numKills = playWorld.getKillLog().size();
        LinkedList<ConcurrentGameWorld.KillInfo> kills = playWorld.getKillLog();
        for(int i = 0;i<Math.min(4,numKills);i++){
            fonts.get(DAGGER20).addText("[BLACK]"+kills.get(numKills-1-i).toString(),5,Gdx.graphics.getHeight()-i*25-5);
            switch (kills.get(numKills-1-i).getWeapon()){
                case SWORD:
                    batch.draw(MainGame.swordSill,100,Gdx.graphics.getHeight()-i*25-23,17,17);
                    break;
                case BOW:
                    batch.draw(MainGame.bowSill,100,Gdx.graphics.getHeight()-i*25-23,17,17);
                    break;
            }
        }
    }
    private void drawDeathSprites(Batch batch, PlayerSoldier center){
        float timeRemaining = 0;
        if(onlineMode){
            timeRemaining = playWorld.getRespawnTimers().get(0).y;
        }
        else {
            for (GridPoint2 timePair : playWorld.getRespawnTimers()) {
                if ( timePair.x == client.getConnectionID()) {
                    timeRemaining = timePair.y;
                }
            }
        }
        String youDied = "YOU HAVE DIED";
        String timeRemainingText = String.format("Respawn in %d...",((int)timeRemaining/60)+1);
        fonts.get(DAGGER50).addText(youDied,Gdx.graphics.getWidth()/2-(MenuTools.textWidth(fonts.get(DAGGER50).getFont(),youDied)/2),Gdx.graphics.getHeight()/2-(MenuTools.textHeight(fonts.get(DAGGER50).getFont(),youDied)/2));
        fonts.get(DAGGER50).addText(timeRemainingText,Gdx.graphics.getWidth()/2-(MenuTools.textWidth(fonts.get(DAGGER50).getFont(),timeRemainingText)/2),Gdx.graphics.getHeight()/2-(MenuTools.textHeight(fonts.get(DAGGER50).getFont(),timeRemainingText)/2)-55);
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
    public PlayerSoldier clientSoldier(){ // I can't be sure the pointer is always the same since the hashmap is always being updated from the server
        return playWorld.getPlayers().get(onlineMode?client.getConnectionID():0);
    }
    private void setupPauseMenuBox(){
        float boxWidth = 400;
        float boxHeight = onlineMode?140:50;
        pauseBox = new MenuBox(Gdx.graphics.getWidth()/2-boxWidth/2,Gdx.graphics.getHeight()/2-boxHeight/2,boxWidth,boxHeight,fonts);
        pauseBox.setBackground(MainGame.bigButtonPress);
        if(onlineMode) {
            pauseBox.addMenuBox(MenuTools.createLabelledButton(5, boxHeight-45, boxWidth-10, 40, "[BLACK]Disconnect", new MenuTools.OnClick() {
                public void action() {
                    if(MainGame.state == GameState.State.OFFLINEDEBUG){
                        MainGame.state = GameState.State.SERVERBROWSER;
                    }
                    else {
                        client.disconnect();
                    }
                    MainGame.gameStarted = false;
                }
            }, MainGame.bigButtonPress,MainGame.bigButtonUn,fonts,DAGGER30));
            pauseBox.addMenuBox(MenuTools.createLabelledButton(5, boxHeight - 90, boxWidth - 10, 40, "[BLACK]Change Class", new MenuTools.OnClick() {
                public void action() {
                    MainGame.state = GameState.State.PICKINGINFO;
                    clientSoldier().setHealth(0);
                }
            }, MainGame.bigButtonPress,MainGame.bigButtonUn, fonts, DAGGER30));
        }
        pauseBox.addMenuBox(MenuTools.createLabelledButton(5, onlineMode?boxHeight - 135:5, boxWidth - 10, 40, "[RED]Quit", new MenuTools.OnClick() {
            public void action() {
                Gdx.app.exit();
            }
        },MainGame.bigButtonPress,MainGame.bigButtonUn,fonts,DAGGER30));
        pauseBox.setVelocity(0,-30);
    }
    public void addBloodParticle(float x, float y){
        ParticleEffectPool.PooledEffect effectTemp = bloodEffectPool.obtain();
        effectTemp.setPosition(x,y);
        effects.add(effectTemp);
    }

    /**
     * returns which arrows still exist on the server
     * @return
     */
    private HashSet<String> existingHashes(){
        HashSet<String> out = new HashSet<String>();
        for(Projectile proj : playWorld.getProjectiles()){
            out.add(proj.getServerHash());
        }
        return out;
    }
}
