package me.dumfing.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.utils.Array;
import me.dumfing.gdxtools.MenuTools;
import me.dumfing.multiplayerTools.MultiplayerClient;
import me.dumfing.multiplayerTools.MultiplayerTools;

import static me.dumfing.client.maingame.MainGame.DAGGER40;
import static me.dumfing.multiplayerTools.PlayerSoldier.ARCHER;
import static me.dumfing.multiplayerTools.PlayerSoldier.KNIGHT;

/**
 * TODO: it is currently possible to bypass the team picking lock[1] by picking a team at the same time as the other person<br>1. If client knows that there are too many people on one team it will block you from joining that team
 *
 */

public class ClientPickingInfoMenu extends Menu{
    MenuTools.QueueText redTeamNumbers;
    MenuTools.QueueText bluTeamNumbers;
    MenuTools.Button redTeamButton, bluTeamButton, knightButton, archerButton, tankButton;
    boolean inOriginalPosition = true; // the buttons are in the positions they started in
    /**
     * Constructor for the menu
     *
     * @param bmfc    BitMapFontCache for drawing all text in the menu
     * @param manager
     * @param camera
     */
    public ClientPickingInfoMenu(Array<BitmapFontCache> bmfc, AssetManager manager, OrthographicCamera camera) {
        super(bmfc, manager, camera);
    }

    public void init(final MultiplayerClient cl) {
        redTeamButton  =new MenuTools.Button(5,5, Gdx.graphics.getWidth()/2-10,Gdx.graphics.getHeight()-10);
        bluTeamButton = new MenuTools.Button(Gdx.graphics.getWidth()/2+5,5,Gdx.graphics.getWidth()/2-10,Gdx.graphics.getHeight()-10);
        knightButton = new MenuTools.Button(5,Gdx.graphics.getHeight(),Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/2);
        archerButton = new MenuTools.Button(Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/3-5,Gdx.graphics.getHeight(),Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight()/2);
        redTeamButton.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                if(cl.getRedTeam()<cl.getrLimit()) {
                    cl.secureSend(new MultiplayerTools.ClientPickedTeam(0));
                    redTeamButton.setVelocity(-30, 0);
                    bluTeamButton.setVelocity(30, 0);
                    redTeamNumbers.setVelocity(-30, 0);
                    bluTeamNumbers.setVelocity(30, 0);
                    knightButton.setVelocity(0,-20);
                    archerButton.setVelocity(0,-20);
                    inOriginalPosition = false;
                }
            }
        });
        bluTeamButton.setCallback(new MenuTools.OnClick(){
            @Override
            public void action() {
                if(cl.getBlueTeam()<cl.getbLimit()) {
                    cl.secureSend(new MultiplayerTools.ClientPickedTeam(1));
                    redTeamButton.setVelocity(-30, 0);
                    bluTeamButton.setVelocity(30, 0);
                    redTeamNumbers.setVelocity(-30, 0);
                    bluTeamNumbers.setVelocity(30, 0);
                    knightButton.setVelocity(0,-20);
                    archerButton.setVelocity(0,-20);
                    inOriginalPosition = false;
                }
            }
        });
        redTeamButton.setPressedTexture(MenuTools.mGTR("simpleBG.png",getManager()));//new TextureRegion((Texture)getManager().get("desktop.jpg")));
        redTeamButton.setUnpressedTexture(MenuTools.mGTR("menubackdrops/canvas.png",getManager()));
        bluTeamButton.setPressedTexture(MenuTools.mGTR("simpleBG.png",getManager()));
        bluTeamButton.setUnpressedTexture(MenuTools.mGTR("simpleBG.png",getManager()));
        redTeamNumbers = new MenuTools.QueueText(10,Gdx.graphics.getHeight()-10,0,0);
        bluTeamNumbers = new MenuTools.QueueText(Gdx.graphics.getWidth()/2+10,Gdx.graphics.getHeight()-10,0,0);
        redTeamNumbers.setFont(DAGGER40);
        bluTeamNumbers.setFont(DAGGER40);
        knightButton.setPressedTexture(MenuTools.mGTR("archive/L1.png",getManager()));
        knightButton.setUnpressedTexture(MenuTools.mGTR("archive/R1.png",getManager()));
        knightButton.setCallback(new MenuTools.OnClick() {
            @Override
            public void action() {
                cl.secureSend(new MultiplayerTools.ClientPickedLoadout(KNIGHT));
            }
        });
        archerButton.setPressedTexture(MenuTools.mGTR("archive/L2.png",getManager()));
        archerButton.setUnpressedTexture(MenuTools.mGTR("archive/R2.png",getManager()));
        archerButton.setCallback(new MenuTools.OnClick() {
            public void action() {
                cl.secureSend(new MultiplayerTools.ClientPickedLoadout(ARCHER));
            }
        });
        super.addButton(archerButton);
        super.addButton(knightButton);
        super.addQueueText(redTeamNumbers);
        super.addQueueText(bluTeamNumbers);
        super.addButton(redTeamButton);
        super.addButton(bluTeamButton);
    }
    public void resetButtonPosses(){
        if(!inOriginalPosition) {
            redTeamButton.setVelocity(30, 0);
            bluTeamButton.setVelocity(-30, 0);
            redTeamNumbers.setVelocity(30, 0);
            bluTeamNumbers.setVelocity(-30, 0);
            knightButton.setVelocity(0,20);
            archerButton.setVelocity(0,20);
            inOriginalPosition = true;
        }
    }
    public void updateTeamNumbers(int redTeam, int blueTeam, int rMax, int bMax){
        redTeamNumbers.setText(String.format("%s%d|%d",redTeam>=rMax?"[RED]":"",redTeam,rMax),getFonts());
        bluTeamNumbers.setText(String.format("%s%d|%d",blueTeam>=bMax?"[RED]":"",blueTeam,bMax),getFonts());
    }
}
