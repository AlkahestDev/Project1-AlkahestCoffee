package me.dumfing.multiplayerTools;
//FILENAME
//Aaron Li  5/23/2017
//EXPLAIN

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationManager {
    private static TextureAtlas knightSprites; //= new TextureAtlas(Gdx.files.internal("SpriteSheets/KnightSprites.atlas"));
    private static TextureAtlas archerSprites;// = new TextureAtlas(Gdx.files.internal("SpriteSheets/ArcherSprites.atlas"));
    private static final float WALKINGFRAMETIME = 0.07f;
    private static final float IDLEFRAMETIME = 0.3f;
    private static final float ATTACKFRAMETIME = 0.02f;
    public static Animation[][][] redPlayer;
    public static Animation[][][] bluPlayer;
    public static Animation[] redFlag;
    public static Animation[] bluFlag;
    public static void init(AssetManager manager){
        knightSprites = manager.get("SpriteSheets/KnightSprites.atlas");
        archerSprites = manager.get("SpriteSheets/ArcherSprites.atlas");
        Animation<TextureRegion> redPlayerWalkRightKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wrrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkLeftKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wlrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkRightArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wrrpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkLeftArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wlrpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkRightKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wrbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkLeftKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("walk/wlbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkRightArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wrbpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkLeftArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wlbpa"), Animation.PlayMode.LOOP);

        Animation<TextureRegion> redPlayerIdleLeftKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/ilrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerIdleRightKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/irrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerIdleLeftArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("ilrpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerIdleRightArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("irrpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerIdleLeftKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/ilbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerIdleRightKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("idle/irbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerIdleLeftArcher = new Animation<TextureRegion>(IDLEFRAMETIME,archerSprites.findRegions("ilbpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerIdleRightArcher = new Animation<TextureRegion>(IDLEFRAMETIME,archerSprites.findRegions("irbpa"), Animation.PlayMode.LOOP);

        Animation<TextureRegion> redPlayerAttackRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME,knightSprites.findRegions("arrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerAttackLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME,knightSprites.findRegions("alrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerAttackRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("arbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerAttackLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("arbpk"), Animation.PlayMode.LOOP);
        //System.out.println(redPlayerWalkLeftArcher.getAnimationDuration());
        redFlag = new Animation[]{redPlayerWalkLeftArcher,redPlayerWalkRightArcher};
        bluFlag= new Animation[]{bluPlayerWalkLeftArcher,bluPlayerWalkRightArcher};
        redPlayer = new Animation[][][]{{{redPlayerWalkLeftKnight,redPlayerWalkLeftArcher},null,null,{redPlayerAttackLeftKnight,null}, {redPlayerIdleLeftKnight,redPlayerIdleLeftArcher}},{{redPlayerWalkRightKnight,redPlayerWalkRightArcher},null,null,{redPlayerAttackRightKnight,null}, {redPlayerIdleRightKnight,redPlayerIdleRightArcher}}};
        bluPlayer = new Animation[][][]{{{bluPlayerWalkLeftKnight,bluPlayerWalkLeftArcher},null,null,{bluPlayerAttackLeftKnight,null}, {bluPlayerIdleLeftKnight,bluPlayerIdleLeftArcher}},{{bluPlayerWalkRightKnight,bluPlayerWalkRightArcher},null,null,{bluPlayerAttackRightKnight,null  }, {bluPlayerIdleRightKnight,bluPlayerIdleRightArcher}}};
    }
    //file names are in format adctl
    //a is the animation (walk, jump, fall, etc.)
    //d is the direction (left, right)
    //c is the colour (red, blue)
    //t is the type (player, nonplayer)
    //l is the loadout (knight, archer)

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

    public static final int ATTACKFRAME = 9;



}
