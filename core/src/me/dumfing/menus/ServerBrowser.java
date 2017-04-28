package me.dumfing.menus;
//FILENAME
//Aaron Li  4/28/2017
//EXPLAIN

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;

public class ServerBrowser extends Menu{
    private MenuBox serverList;
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

    public void init() {
        super.init();
        super.setBackground(new TextureRegion((Texture)super.getManager().get("tuzki.png")));
        serverList = new MenuBox(Gdx.graphics.getWidth()-400,Gdx.graphics.getHeight()-2000,400,2000,super.getFonts());
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
            serverList.addQueueText(qt);
            super.addMenuBox(serverList);
        }
    }

    public boolean scrolled(int amount) {
        float screenX = Gdx.input.getX();
        float screenY = Gdx.graphics.getHeight() - Gdx.input.getY();
        System.out.println("Scroll "+amount+String.format(" %f %f ",screenX,screenY));
        if(serverList.collidePoint(screenX,screenY)){
            if(amount < 0 && serverList.getRect().y > 0){
                serverList.translate(0,10*amount);
            }
            else if(amount > 0){
                serverList.translate(0,10*amount);
            }
            System.out.println(serverList.getRect().y);
        }
        return super.scrolled(amount);
    }

    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        super.draw(sb, sr);
        super.standardDraw(sb,sr);
    }
}
