package me.dumfing.menus;
//FILENAME
//Aaron Li  4/28/2017
//EXPLAIN

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import me.dumfing.client.maingame.GameState;
import me.dumfing.client.maingame.MainGame;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.multiplayerTools.MultiplayerTools;

import java.util.HashMap;

import static me.dumfing.client.maingame.MainGame.DAGGER30;
import static me.dumfing.client.maingame.MainGame.client;

@Deprecated
public class ServerBrowser extends Menu{
    private ServerBrowserList serverList;
    /**
     * Constructor for the menu
     *
     * @param bmfc    BitMapFontCache for drawing all text in the menu
     * @param manager
     * @param camera
     */
    public ServerBrowser(Array<BitmapFontCache> bmfc, AssetManager manager, OrthographicCamera camera) {
        super(bmfc, manager, camera);
    }

    /**
     * Initializes the ServerBrowser
     */
    public void init() {
        //super.init();
        super.setBackground(new TextureRegion((Texture) super.getManager().get("tuzki.png")));
        float serverListHeight = 3000;
        serverList = new ServerBrowserList(Gdx.graphics.getWidth()-400, Gdx.graphics.getHeight() - serverListHeight, 400, serverListHeight, super.getFonts(), this.getManager());
        super.addMenuBox(serverList);
        addRefreshButton();
        addBackButton();
        MenuTools.TextField directConnect = new MenuTools.TextField(5,5,400,40);
        directConnect.setEnterAction(new MenuTools.OnEnter() {
            @Override
            public void action(String sIn) {
                MainGame.client.connectServerPlay(sIn);
                MainGame.state = GameState.State.CONNECTINGTOSERVER;
            }
        });
        super.addTextField(directConnect);
    }
    private void addBackButton(){
        MenuTools.Button bt = new MenuTools.Button(0,Gdx.graphics.getHeight()-65,60,60);
        bt.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                MainGame.state = GameState.State.MAINMENU;
            }
        });
        bt.setPressedTexture(new TextureRegion((Texture)getManager().get("R1.png")));
        bt.setUnpressedTexture(new TextureRegion((Texture)getManager().get("R2.png")));
        super.addButton(bt);
    }
    private void addRefreshButton(){
        MenuTools.Button refreshServers = new MenuTools.Button(Gdx.graphics.getWidth()-485,Gdx.graphics.getHeight()-85,80,80);
        refreshServers.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                if(!client.isFindingServers()) {
                    MainGame.client.pingServers();
                }
            }
        });
        refreshServers.setPressedTexture(new TextureRegion((Texture)getManager().get("L1.png")));
        refreshServers.setUnpressedTexture(new TextureRegion((Texture)getManager().get("L2.png")));
        super.addButton(refreshServers);

    }
    public boolean scrolled(int amount) {
        serverList.onScroll(amount);
        return super.scrolled(amount);
    }
    public void populateServerList(HashMap<String, MultiplayerTools.ServerSummary> servers){
        this.serverList.refreshServers(servers);
    }
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        super.standardDraw(sb,sr);
        serverList.draw(sb,sr);
    }
    private class ServerBrowserList extends MenuBox{
        //private String[] pingAmounts = {"CYAN","GREEN","YELLOW","GOLD","ORANGE","RED"};
        private AssetManager assets;
        public ServerBrowserList(float x ,float y, float w, float h, Array<BitmapFontCache> fonts,AssetManager assets){
            super(x,y,w,h,fonts);
            this.assets = assets;
        }
        public void refreshServers(final HashMap<String, MultiplayerTools.ServerSummary> serverList){
            //System.out.println(serverList);
            int btHeight = 60; //Height of all buttons to be added
            int bNum = 0;
            super.clearButtons();
            super.clearText();
            for(final String k : serverList.keySet()){
                float btPosY = bNum*btHeight;
                MultiplayerTools.ServerSummary svInfo = serverList.get(k);
                String tOut = String.format("%s%d|%d [%s]%5d",(svInfo.num>=svInfo.max?"[RED]":""),svInfo.num,svInfo.max,ratePing(svInfo.ping),svInfo.ping);
                MenuTools.Button bt= new MenuTools.Button(0,super.getRect().getHeight()-btPosY-btHeight,super.getRect().getWidth(),btHeight);
                MenuTools.QueueText sName = new MenuTools.QueueText(5,super.getRect().getHeight()-btPosY-btHeight/2+7,0,0);
                MenuTools.QueueText peopleLimit = new MenuTools.QueueText(super.getRect().getWidth()-MenuTools.textWidth(getFonts().get(DAGGER30).getFont(),tOut)-2,super.getRect().getHeight()-btPosY-btHeight/2+7, 0,0);
                System.out.println(sName.getRect()+" "+peopleLimit.getRect());
                peopleLimit.setFont(DAGGER30);
                sName.setFont(DAGGER30);
                peopleLimit.setText(tOut,super.getFontCaches());
                sName.setText(serverList.get(k).serverName,super.getFontCaches());
                bt.setPressedTexture(MenuTools.mGTR("4914003-galaxy-wallpaper-png.png",getManager()));//new TextureRegion((Texture)assets.get("4914003-galaxy-wallpaper-png.png")));
                bt.setUnpressedTexture(MenuTools.mGTR("Desktop.jpg",getManager()));//new TextureRegion((Texture)assets.get("Desktop.jpg")));
                bt.setCallback(new MenuTools.OnClick() {
                    @Override
                    public void action() {
                            System.out.println(k);
                            String svIP = k;
                            svIP = svIP.replace("/", "").substring(0, svIP.indexOf(":") - 1); // the received ip is in the form "/ip:port", we only need the ip part so we remove the / and the :port
                            System.out.println(svIP);
                            MainGame.client.connectServerPlay(svIP);
                            MainGame.state = GameState.State.CONNECTINGTOSERVER;

                    }
                });
                super.addQueueText(peopleLimit);
                super.addQueueText(sName);
                super.addButton(bt);
                bNum++;
            }
        }

        public void onScroll(int amount){
            System.out.println(this.getRect()+" "+amount);
            if(amount>0){
                if(this.getRect().getY()<0){// <= Gdx.graphics.getHeight()-this.getRect().getHeight()){
                    this.translate(0,5*amount);
                }
            }
            else{
                if(this.getRect().getY() > Gdx.graphics.getHeight()-this.getRect().getHeight()){
                    this.translate(0,5*amount);
                }
            }
        }
        public String ratePing(int ping){
            if(ping<=10){
                return "CYAN";
            }
            else if(ping <= 30){
                return "GREEN";
            }
            else if(ping <=50){
                return "YELLOW";
            }
            else if(ping<=100){
                return "GOLD";
            }
            else if(ping<=150){
                return"ORANGE";
            }
            else{
                return "RED";
            }
        }
    }
}
