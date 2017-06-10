package me.dumfing.multiplayerTools;
//FILENAME
//Aaron Li  5/23/2017
//EXPLAIN

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerAnimations {
    private static final TextureAtlas knightSprites = new TextureAtlas(Gdx.files.internal("SpriteSheets/KnightSprites.atlas"));
    private static final TextureAtlas archerSprites = new TextureAtlas(Gdx.files.internal("SpriteSheets/ArcherSprites.atlas"));
    private static final float WALKINGFRAMETIME = 0.07f;
    private static final float IDLEFRAMETIME = 0.3f;
    private static final float ATTACKFRAMETIME = 0.01f;
    //file names are in format adctl
    //a is the animation (walk, jump, fall, etc.)
    //d is the direction (left, right)
    //c is the colour (red, blue)
    //t is the type (player, nonplayer)
    //l is the loadout (knight, archer)
    private static final Animation<TextureRegion> redPlayerWalkRightKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wrrpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerWalkLeftKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wlrpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerWalkRightArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wrrpa"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerWalkLeftArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wlrpa"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerWalkRightKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wrbpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerWalkLeftKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wlbpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerWalkRightArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wrbpa"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerWalkLeftArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wlbpa"), Animation.PlayMode.LOOP);

    private static final Animation<TextureRegion> redPlayerIdleLeftKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/ilrpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerIdleRightKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/irrpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerIdleLeftArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("ilrpa"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerIdleRightArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("irrpa"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerIdleLeftKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/ilbpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerIdleRightKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/irbpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerIdleLeftArcher = new Animation<TextureRegion>(IDLEFRAMETIME,archerSprites.findRegions("ilbpa"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerIdleRightArcher = new Animation<TextureRegion>(IDLEFRAMETIME,archerSprites.findRegions("irbpa"), Animation.PlayMode.LOOP);

    private static final Animation<TextureRegion> redPlayerAttackRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME,knightSprites.findRegions("arrpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> redPlayerAttackLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME,knightSprites.findRegions("alrpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerAttackRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("arbpk"), Animation.PlayMode.LOOP);
    private static final Animation<TextureRegion> bluPlayerAttackLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("arbpk"), Animation.PlayMode.LOOP);
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
    public static final Animation[][][] redPlayer = {{{redPlayerWalkLeftKnight,redPlayerWalkLeftArcher},null,null,{redPlayerAttackLeftKnight,null}, {redPlayerIdleLeftKnight,redPlayerIdleLeftArcher}},{{redPlayerWalkRightKnight,redPlayerWalkRightArcher},null,null,{redPlayerAttackRightKnight,null}, {redPlayerIdleRightKnight,redPlayerIdleRightArcher}}};
    public static final Animation[][][] bluPlayer = {{{bluPlayerWalkLeftKnight,bluPlayerWalkLeftArcher},null,null,{bluPlayerAttackLeftKnight,null}, {bluPlayerIdleLeftKnight,bluPlayerIdleLeftArcher}},{{bluPlayerWalkRightKnight,bluPlayerWalkRightArcher},null,null,{bluPlayerAttackRightKnight,null  }, {bluPlayerIdleRightKnight,bluPlayerIdleRightArcher}}};
}
