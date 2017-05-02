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
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.maingame.MainGame;
import me.dumfing.multiplayerTools.MultiplayerTools;

import java.util.HashMap;

import static me.dumfing.maingame.MainGame.*;

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
        serverList = new ServerBrowserList(880, 0, 400, 2000, super.getFonts(), this.getManager());
        super.addMenuBox(serverList);
        MenuTools.Button refreshServers = new MenuTools.Button(Gdx.graphics.getWidth()-485,Gdx.graphics.getHeight()-85,80,80);
        refreshServers.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                if(!player.isFindingServers()) {
                    MainGame.player.pingServers();
                }
            }
        });
        refreshServers.setPressedTexture(new TextureRegion((Texture)getManager().get("L1.png")));
        refreshServers.setUnpressedTexture(new TextureRegion((Texture)getManager().get("L2.png")));
        super.addButton(refreshServers);
    }
        /*serverList = new MenuBox(Gdx.graphics.getWidth()-400,Gdx.graphics.getHeight()-2000,400,2000,super.getFonts());
        final String[] serverNames = {"PARTY SERVER","[RED]SOVIET RUSSIA", "Hei","DumfingServer1"};
        for(int i = 0; i<serverNames.length;i++){
            MenuTools.Button bt = new MenuTools.Button(0,2000-((i+1)*60),400,60);
            bt.setPressedTexture(new TextureRegion((Texture) super.getManager().get("Desktop.jpg")));
            bt.setUnpressedTexture(new TextureRegion((Texture) super.getManager().get("volcano-30238.png")));
            final int finalI = i;
            bt.setCallback(new MenuTools.OnClick() {
                public void action() {
                    System.out.println(serverNames[finalI]);//new String(serverNames[finalI]));
                }
            });
            serverList.addButton(bt);
            MenuTools.QueueText qt = new MenuTools.QueueText(5,2000-(i*60+10),0,0);
            qt.setText(new String(serverNames[finalI]),super.getFonts());
            qt.setFont(DAGGER20);
            serverList.addQueueText(qt);
            super.addMenuBox(serverList);
        }
    }
    */
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
            System.out.println(serverList);
            int btHeight = 60; //Height of all buttons to be added
            int bNum = 0;
            super.clearButtons();
            super.clearText();
            for(final String k : serverList.keySet()){
                float btPosY = bNum*btHeight;
                MultiplayerTools.ServerSummary svInfo = serverList.get(k);
                String tOut = String.format("%s%d|%d [%s]%5d",(svInfo.num>=svInfo.max?"[RED]":""),svInfo.num,svInfo.max,ratePing(svInfo.ping),svInfo.ping);
                MenuTools.Button bt= new MenuTools.Button(0,Gdx.graphics.getHeight()-btPosY-btHeight,super.getRect().getWidth(),btHeight);
                MenuTools.QueueText sName = new MenuTools.QueueText(5,Gdx.graphics.getHeight()-btPosY-btHeight/2+7,0,0);
                MenuTools.QueueText peopleLimit = new MenuTools.QueueText(super.getRect().getWidth()-MenuTools.textWidth(getFonts().get(DAGGER30).getFont(),tOut)-2,Gdx.graphics.getHeight() - btPosY-btHeight/2+7,0,0);
                System.out.println(sName.getRect()+" "+peopleLimit.getRect());
                peopleLimit.setFont(DAGGER30);
                sName.setFont(DAGGER30);
                peopleLimit.setText(tOut,super.getFontCaches());
                sName.setText(serverList.get(k).serverName,super.getFontCaches());
                bt.setPressedTexture(new TextureRegion((Texture)assets.get("4914003-galaxy-wallpaper-png.png")));
                bt.setUnpressedTexture(new TextureRegion((Texture)assets.get("Desktop.jpg")));
                bt.setCallback(new MenuTools.OnClick() {
                    @Override
                    public void action() {
                        System.out.println(k);
                    }
                });
                super.addQueueText(peopleLimit);
                super.addQueueText(sName);
                super.addButton(bt);
                bNum++;
            }
        }
        public void onScroll(int amount){
            if(amount>0){
                if(this.getRect().getY()>0){
                    System.out.println(this.getRect()+" "+amount);
                    this.translate(0,amount);
                }
            }
            else{
                if(this.getRect().getY()<0){
                    System.out.println(this.getRect()+" "+amount);
                    this.translate(0,amount);
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
