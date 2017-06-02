package me.dumfing.multiplayerTools;
//FILENAME
//Aaron Li  5/23/2017
//EXPLAIN

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import javax.xml.soap.Text;

public class PlayerAnimations {
    private static final TextureAtlas knightSprites = new TextureAtlas(Gdx.files.internal("SpriteSheets/KnightSprites.atlas"));
    private static final TextureAtlas archerSprites = new TextureAtlas(Gdx.files.internal("SpriteSheets/ArcherSprites.atlas"));
    private static final float WALKINGFRAMETIME = 0.07f;
    private static final float IDLEFRAMETIME = 0.3f;
    //file names are in format adctl
    //a is the animation (walk, jump, fall, etc.)
    //d is the direction (left, right)
    //c is the colour (red, blue)
    //t is the type (player, nonplayer)
    //l is the loadout (knight, archer)
    private static final Animation<TextureRegion> redNonPlayerWalkLeftKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wlrnk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redNonPlayerWalkRightKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wrrnk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redNonPlayerWalkLeftArcher = new Animation<TextureRegion>(WALKINGFRAMETIME,archerSprites.findRegions("wlrna"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redNonPlayerWalkRightArcher = new Animation<TextureRegion>(WALKINGFRAMETIME,archerSprites.findRegions("wrrna"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluNonPlayerWalkLeftKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wlbnk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluNonPlayerWalkRightKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wrbnk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluNonPlayerWalkLeftArcher = new Animation<TextureRegion>(WALKINGFRAMETIME,archerSprites.findRegions("wlbna"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluNonPlayerWalkRightArcher = new Animation<TextureRegion>(WALKINGFRAMETIME,archerSprites.findRegions("wrbna"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerWalkRightKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wrrpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerWalkLeftKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wlrpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerWalkRightArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wrrpa"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerWalkLeftArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wlrpa"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerWalkRightKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wrbpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerWalkLeftKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wlbpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerWalkRightArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wrbpa"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerWalkLeftArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wlbpa"), Animation.PlayMode.LOOP);

    private static final Animation<TextureRegion> redNonPlayerIdleLeftKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/ilrnk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redNonPlayerIdleRightKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/irrnk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redNonPlayerIdleLeftArcher = new Animation<TextureRegion>(IDLEFRAMETIME,archerSprites.findRegions("ilrna"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redNonPlayerIdleRightArcher = new Animation<TextureRegion>(IDLEFRAMETIME,archerSprites.findRegions("irrna"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluNonPlayerIdleLeftKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/ilbnk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluNonPlayerIdleRightKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/irbnk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluNonPlayerIdleLeftArcher = new Animation<TextureRegion>(IDLEFRAMETIME,archerSprites.findRegions("ilbna"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluNonPlayerIdleRightArcher = new Animation<TextureRegion>(IDLEFRAMETIME,archerSprites.findRegions("irbna"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerIdleLeftKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/ilrpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerIdleRightKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/irrpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerIdleLeftArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("ilrpa"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerIdleRightArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("irrpa"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerIdleLeftKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/ilbpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerIdleRightKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/irbpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerIdleLeftArcher = new Animation<TextureRegion>(IDLEFRAMETIME,archerSprites.findRegions("ilbpa"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerIdleRightArcher = new Animation<TextureRegion>(IDLEFRAMETIME,archerSprites.findRegions("irbpa"), Animation.PlayMode.LOOP);
    //animations will be accessed with redPlayer[direction][animationID]
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int WALK = 2;
    public static final int FALL = 4;
    public static final int JUMP = 8;
    public static final int ATTACK = 16;
    public static final int IDLE = 32;
    public static final int DIRECTION = 1;
    public static final int ISWALKING = 2;
    public static final int ISFALLING = 4;
    public static final int ISJUMPING = 8;
    public static final int ISATTACK = 16;
    public static final int ISIDLE = 32;
    public static final Animation[][][] redPlayer = {{{redPlayerWalkLeftKnight,redPlayerWalkLeftArcher},null,null,null, {redPlayerIdleLeftKnight,redPlayerIdleLeftArcher}},{{redPlayerWalkRightKnight,redPlayerWalkRightArcher},null,null,null, {redPlayerIdleRightKnight,redPlayerIdleRightArcher}}};
    public static final Animation[][][] redNonPlayer = {{{redNonPlayerWalkLeftKnight,redNonPlayerWalkLeftArcher},null,null,null, {redNonPlayerIdleLeftKnight,redNonPlayerIdleLeftArcher}},{{redNonPlayerWalkRightKnight,redNonPlayerWalkRightArcher},null,null,null, {redNonPlayerIdleRightKnight,redNonPlayerIdleRightArcher}}};
    public static final Animation[][][] bluPlayer = {{{bluPlayerWalkLeftKnight,bluPlayerWalkLeftArcher},null,null,null, {bluPlayerIdleLeftKnight,bluPlayerIdleLeftArcher}},{{bluPlayerWalkRightKnight,bluPlayerWalkRightArcher},null,null,null, {bluPlayerIdleRightKnight,bluPlayerIdleRightArcher}}};
    public static final Animation[][][] bluNonPlayer = {{{bluNonPlayerWalkLeftKnight,bluNonPlayerWalkLeftArcher},null,null,null, {bluNonPlayerIdleLeftKnight, bluNonPlayerIdleLeftArcher}},{{bluNonPlayerWalkRightKnight,bluNonPlayerWalkRightArcher},null,null,null, {bluNonPlayerIdleRightKnight,bluNonPlayerIdleRightArcher}}};

}
