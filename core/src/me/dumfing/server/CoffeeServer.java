package me.dumfing.server;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.menus.LoadingMenu;
import me.dumfing.menus.Menu;
import me.dumfing.menus.ServerInfoMenu;
import me.dumfing.menus.ServerRunningGameMenu;
import me.dumfing.multiplayerTools.AnimationManager;
import me.dumfing.multiplayerTools.WorldMap;

import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by dumpl on 4/19/2017.
 */
public class CoffeeServer extends ApplicationAdapter implements InputProcessor{


    public enum ServerState{
        LOADING,
        SERVERCONFIG,
        GAMELOBBY,
        RUNNINGGAME;
    }
    MainServer sv;
    Scanner kb = new Scanner(System.in);
    public static ServerState svState = ServerState.LOADING;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    Menu askConfig;
    AssetManager manager;
    Array<BitmapFontCache> fonts = new Array<BitmapFontCache>();
    OrthographicCamera camView;
    LoadingMenu loadingMenu;
    ServerRunningGameMenu serverRunningMenu;
    ServerInfoMenu serverInfo;
    ServerGameInstance instance;
    public static HashSet<Integer> redTeamMembers = new HashSet<Integer>();
    public static HashSet<Integer> bluTeamMembers = new HashSet<Integer>();
    @Override
    public void create() {
        batch = new SpriteBatch();
        camView = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        camView.translate(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);
        camView.update();
        batch.setProjectionMatrix(camView.combined);
        manager = new AssetManager();
        shapeRenderer = new ShapeRenderer();
        loadingMenu = new LoadingMenu(fonts,manager,camView);
        readyLoadingBackground();
        manager.load("simpleBG.png",Texture.class);
        manager.load("simpleBGB.png",Texture.class);
        //manager.load("4k-image-santiago.jpg",Texture.class);
        //manager.load("4914003-galaxy-wallpaper-png.png",Texture.class);
        manager.load("pixmapTest.png",Texture.class);
        manager.load("pixmapVisual.png",Texture.class);
        manager.load("SpriteSheets/KnightSprites.atlas", TextureAtlas.class);
        manager.load("SpriteSheets/ArcherSprites.atlas",TextureAtlas.class);
        manager.load("SpriteSheets/FlagSprites.atlas",TextureAtlas.class);
        BitmapFont dagger30 = new BitmapFont(Gdx.files.internal("fonts/dagger30.fnt"));
        dagger30.getData().markupEnabled = true;
        serverInfo = new ServerInfoMenu(fonts,manager,camView);
        serverRunningMenu = new ServerRunningGameMenu(fonts,manager,camView);
        fonts.add(new BitmapFontCache( dagger30));
    }

    @Override
    public void render() {
        for(BitmapFontCache bmfc : fonts){
            bmfc.clear();
        }
        switch (svState){
            case LOADING:
                if(Gdx.input.getInputProcessor() != loadingMenu){
                    loadingMenu.setInputProcessor();
                }
                if(loadingMenu.doneLoading()){
                    svState = ServerState.SERVERCONFIG;
                    askConfig = createServerForm();
                    serverInfo.init();
                    serverRunningMenu.init();
                    AnimationManager.init(manager);
                }
                loadingMenu.update();
                loadingMenu.draw(batch,shapeRenderer);
                break;
            case SERVERCONFIG:
                if(Gdx.input.getInputProcessor()!=askConfig){
                    askConfig.setInputProcessor();
                }
                askConfig.update();
                askConfig.draw(batch,shapeRenderer);
                break;
            case GAMELOBBY:
                if(Gdx.input.getInputProcessor()!=serverInfo){
                    serverInfo.setInputProcessor();
                }
                serverInfo.update(sv,redTeamMembers.size(),bluTeamMembers.size(),sv.getMaxPlayers());
                serverInfo.draw(batch,shapeRenderer);
                break;
            case RUNNINGGAME:
                if(Gdx.input.getInputProcessor() != serverRunningMenu){
                    serverRunningMenu.setInputProcessor();
                    instance = new ServerGameInstance(sv.getPlayers());
                    instance.setWorldMap(new WorldMap(MenuTools.mGTR("pixmapTest.png",manager),MenuTools.mGTR("pixmapVisual.png",manager)));
                    //instance.world.setCollisionBoxes(Gdx.files.internal("pixmapTest.png"));
                }
                instance.update(sv);
                serverRunningMenu.updateMenuInfo(sv);
                serverRunningMenu.update();
                serverRunningMenu.draw(batch,shapeRenderer);
                break;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
    private Menu createServerForm(){
        Menu out = new Menu(fonts,manager,camView);
        out.setBackground(new TextureRegion((Texture)manager.get("simpleBGB.png")));
        final MenuTools.TextField serverNameField = new MenuTools.TextField(Gdx.graphics.getWidth()/2-150,Gdx.graphics.getHeight()-90,300,45);
        final MenuTools.TextField maxPlayersField = new MenuTools.TextField(Gdx.graphics.getWidth()/2-150,Gdx.graphics.getHeight()-140,300,45);
        MenuTools.Button submitInfo = new MenuTools.Button(Gdx.graphics.getWidth()/2-80,Gdx.graphics.getHeight()-200,160,55);
        final MenuTools.QueueText errorText = new MenuTools.QueueText(5,Gdx.graphics.getHeight()-20,0,0);
        errorText.setFont(0);
        errorText.setText("",fonts);
        maxPlayersField.setFont(0);
        serverNameField.setFont(0);
        submitInfo.setUnpressedTexture(new TextureRegion((Texture)manager.get("simpleBG.png")));
        submitInfo.setPressedTexture(new TextureRegion((Texture)manager.get("simpleBG.png")));
        submitInfo.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                if(serverNameField.getText().equals("")){
                    serverFormError("Server name cannot be empty!",errorText);
                    return;
                }
                else if(maxPlayersField.getText().equals("")){
                    serverFormError("Max people connected cannot be empty!",errorText);
                    return;
                }
                try{
                    int numplayers = Integer.parseInt(maxPlayersField.getText());
                    if(numplayers>8){
                        serverFormError("Max players per server is 8!",errorText);
                    }
                    else if(numplayers == 0){
                        serverFormError("Max players must be greater than 0!",errorText);
                    }
                    else{
                        sv = new MainServer(serverNameField.getText(),numplayers);
                        sv.start();
                        svState = ServerState.GAMELOBBY;
                        Gdx.input.setInputProcessor(null);
                    }
                }
                catch (NumberFormatException e){
                    serverFormError("Max people connected must be an integer!",errorText);
                    return;
                }
            }
        });
        out.addQueueText(errorText);
        out.addTextField(serverNameField);
        out.addTextField(maxPlayersField);
        out.addButton(submitInfo);
        return out;
    }
    private void readyLoadingBackground(){
        for(int i = 0;i<39;i++){
            this.loadingMenu.addBackground(new TextureRegion(new Texture(Gdx.files.internal(String.format("loading/loadingKnight/loadingKnight%d.png",i)))));
        }
        loadingMenu.setFrameRate(25);
    }
    private void serverFormError(String message, final MenuTools.QueueText queueText){
        Timer.instance().clear();
        queueText.setText("[RED]"+message,fonts);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                queueText.clearText();
            }
        },3);
    }
    private void updateServerInfo(){

    }
    public static boolean teamContainsID(HashSet<Integer> team, Integer connectionId){
        for(Integer cID : team){
            if(cID == connectionId.intValue()){
                return true;
            }
        }
        return false;
    }
    /*private HashMap<Connection,MultiplayerTools.ServerPlayerInfo> getPlayerInfo(){
        HashMap<Connection,MultiplayerTools.ServerPlayerInfo> simpleInfo = new HashMap<Connection, MultiplayerTools.ServerPlayerInfo>();
        for(Connection c : sv.getPlayers().keySet()){
            simpleInfo.put(c,sv.getPlayers().get(c));
        }
        return simpleInfo;
    }*/

}
