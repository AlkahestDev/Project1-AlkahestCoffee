package me.dumfing.multiplayerTools;
//FILENAME
//Aaron Li  5/23/2017
//EXPLAIN

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationManager {
    private static final float WALKINGFRAMETIME = 0.07f;
    private static final float IDLEFRAMETIME = 0.3f;
    private static final float ATTACKFRAMETIME = 0.025f;
    private static final float FLAGFRAMETIME = 0.1f;
    private static final float WALKATTACKFRAMETIME = 0.04f;
    private static final float BOWDRAWFRAMETIME = 0.6f;
    private static final float SHIELDDRAWFRAMETIME = 0.1f;
    public static Animation[][][] redPlayer;
    public static Animation[][][] bluPlayer;
    public static Animation[] redFlag;
    public static Animation[] bluFlag;
    public static Animation[] archerDrawLeft;
    public static Animation[] archerDrawRight;
    public static void init(AssetManager manager){

        // Atlas
        TextureAtlas knightSprites = manager.get("SpriteSheets/KnightSprites.atlas");
        TextureAtlas archerSprites = manager.get("SpriteSheets/ArcherSprites.atlas");
        TextureAtlas flagSprites = manager.get("SpriteSheets/FlagSprites.atlas");

        // Knight Walking
        Animation<TextureRegion> redPlayerWalkRightKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("wrrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkLeftKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("wlrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkRightKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("wrbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkLeftKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("wlbpk"), Animation.PlayMode.LOOP);

        // Knight Idle
        Animation<TextureRegion> redPlayerIdleLeftKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("ilrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerIdleRightKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("irrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerIdleLeftKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("ilbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerIdleRightKnight = new Animation<TextureRegion>(IDLEFRAMETIME, knightSprites.findRegions("irbpk"), Animation.PlayMode.LOOP);

        // Knight SwingAttackIdle
        Animation<TextureRegion> redPlayerAttackRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("sirrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerAttackLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("silrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerAttackRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("sirbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerAttackLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("silbpk"), Animation.PlayMode.LOOP);

        // Knight SwingAttackWalking
        Animation<TextureRegion> redPlayerWalkAttackRightKnight = qMakeA(knightSprites,"swrrpk",WALKATTACKFRAMETIME);
        Animation<TextureRegion> redPlayerWalkAttackLeftKnight = qMakeA(knightSprites,"swlrpk",WALKATTACKFRAMETIME);
        Animation<TextureRegion> bluPlayerWalkAttackRightKnight = qMakeA(knightSprites,"swrbpk",WALKATTACKFRAMETIME);
        Animation<TextureRegion> bluPlayerWalkAttackLeftKnight = qMakeA(knightSprites,"swlbpk",WALKATTACKFRAMETIME);

        // Knight StabbingIdle
        Animation<TextureRegion> bluPlayerStabLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("pilbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerStabRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("pirbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerStabLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("pilbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerStabRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("pirbpk"), Animation.PlayMode.LOOP);

        // Knight StabbingWalk
        Animation<TextureRegion> bluPlayerWalkStabLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("wslbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkStabRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("wsrbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkStabLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("wslrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkStabRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("wsrrpk"), Animation.PlayMode.LOOP);

        // Knight ShieldDrawIdle
        Animation<TextureRegion> bluPlayerIdleShieldDrawLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("idlbpk"), Animation.PlayMode.NORMAL);
        Animation<TextureRegion> bluPlayerIdleShieldDrawRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("idrbpk"), Animation.PlayMode.NORMAL);
        Animation<TextureRegion> redPlayerIdleShieldDrawLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("idlrpk"), Animation.PlayMode.NORMAL);
        Animation<TextureRegion> redPlayerIdleShieldDrawRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("idrrpk"), Animation.PlayMode.NORMAL);

        // Knight ShieldDrawWalking
        Animation<TextureRegion> bluPlayerWalkingShieldDrawLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("wdlbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkingShieldDrawRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("wdrbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkingShieldDrawLeftKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("wdlrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkingShieldDrawRightKnight = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("wdrrpk"), Animation.PlayMode.LOOP);

        // Knight ShieldWalking
        Animation<TextureRegion> bluPlayerWalkingShieldLeftKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("wshlbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkingShieldRightKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("wshrbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkingShieldLeftKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("wshlbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkingShieldRightKnight = new Animation<TextureRegion>(WALKINGFRAMETIME, knightSprites.findRegions("wshrbpk"), Animation.PlayMode.LOOP);

        // Knight ShieldIdle
        Animation<TextureRegion> bluPlayerIdleShieldLeftKnight = new Animation<TextureRegion>(SHIELDDRAWFRAMETIME, knightSprites.findRegions("shlbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerIdleShieldRightKnight = new Animation<TextureRegion>(SHIELDDRAWFRAMETIME, knightSprites.findRegions("shrbpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerIdleShieldLeftKnight = new Animation<TextureRegion>(SHIELDDRAWFRAMETIME, knightSprites.findRegions("shlrpk"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerIdleShieldRightKnight = new Animation<TextureRegion>(SHIELDDRAWFRAMETIME, knightSprites.findRegions("shrrpk"), Animation.PlayMode.LOOP);



        // Archer Walking
        Animation<TextureRegion> redPlayerWalkRightArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wrrpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkLeftArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wlrpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkRightArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wrbpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkLeftArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wlbpa"), Animation.PlayMode.LOOP);

        // Archer StabbingIdle
        Animation<TextureRegion> bluPlayerStabLeftArcher = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("silbpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerStabRightArcher = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("sirbpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerStabLeftArcher = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("silbpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerStabRightArcher = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("sirbpa"), Animation.PlayMode.LOOP);

        // Archer StabbingWalking
        Animation<TextureRegion> bluPlayerWalkStabLeftArcher = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("swlbpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkStabRightArcher = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("swrbpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkStabLeftArcher = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("swlrpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkStabRightArcher = new Animation<TextureRegion>(ATTACKFRAMETIME, knightSprites.findRegions("swrrpa"), Animation.PlayMode.LOOP);

        // Archer Idle
        Animation<TextureRegion> redPlayerIdleLeftArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("ilrpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerIdleRightArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("irrpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerIdleLeftArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("ilbpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerIdleRightArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("irbpa"), Animation.PlayMode.LOOP);

        // Archer IdleLegs
        Animation<TextureRegion> redPlayerIdleLegsLeftArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("illrpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerIdleLegsRightArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("ilrrpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerIdleLegsLeftArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("illbpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerIdleLegsRightArcher = new Animation<TextureRegion>(IDLEFRAMETIME, archerSprites.findRegions("ilrbpa"), Animation.PlayMode.LOOP);

        // Archer WalkingLegs
        Animation<TextureRegion> redPlayerWalkingLegsLeftArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wl1lrpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redPlayerWalkingLegsRightArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wl1rrpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkingLegsLeftArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wl1lbpa"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluPlayerWalkingLegsRightArcher = new Animation<TextureRegion>(WALKINGFRAMETIME, archerSprites.findRegions("wl1rbpa"), Animation.PlayMode.LOOP);

        // Archer PullBack
        Animation<TextureRegion> redPlayerPullBackLeftArcher = new Animation<TextureRegion>(BOWDRAWFRAMETIME, archerSprites.findRegions("flrpa"), Animation.PlayMode.NORMAL);
        Animation<TextureRegion> redPlayerPullBackRightArcher = new Animation<TextureRegion>(BOWDRAWFRAMETIME, archerSprites.findRegions("frrpa"), Animation.PlayMode.NORMAL);
        Animation<TextureRegion> bluPlayerPullBackLeftArcher = new Animation<TextureRegion>(BOWDRAWFRAMETIME, archerSprites.findRegions("flbpa"), Animation.PlayMode.NORMAL);
        Animation<TextureRegion> bluPlayerPullBackRightArcher = new Animation<TextureRegion>(BOWDRAWFRAMETIME, archerSprites.findRegions("frbpa"), Animation.PlayMode.NORMAL);

        // Flags
        Animation<TextureRegion> redFlagLeft = new Animation<TextureRegion>(FLAGFRAMETIME,flagSprites.findRegions("rFlagL"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> redFlagRight = new Animation<TextureRegion>(FLAGFRAMETIME, flagSprites.findRegions("rFlagR"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluFlagLeft = new Animation<TextureRegion>(FLAGFRAMETIME, flagSprites.findRegions("bFlagL"), Animation.PlayMode.LOOP);
        Animation<TextureRegion> bluFlagRight = new Animation<TextureRegion>(FLAGFRAMETIME, flagSprites.findRegions("bFlagR"), Animation.PlayMode.LOOP);

        redFlag = new Animation[]{redFlagLeft,redFlagRight};
        bluFlag= new Animation[]{bluFlagLeft,bluFlagRight};
        archerDrawLeft = new Animation[]{redPlayerPullBackLeftArcher,bluPlayerPullBackLeftArcher};
        archerDrawRight = new Animation[]{redPlayerPullBackRightArcher,bluPlayerPullBackRightArcher};
        redPlayer = new Animation[][][]{{{redPlayerWalkLeftKnight,redPlayerWalkLeftArcher},
                null,
                null,
                {redPlayerAttackLeftKnight,redPlayerIdleLegsLeftArcher},
                {redPlayerIdleLeftKnight,redPlayerIdleLeftArcher},
                {redPlayerWalkAttackLeftKnight,redPlayerWalkingLegsLeftArcher},
                {redPlayerIdleShieldLeftKnight,null},
                {redPlayerIdleShieldDrawLeftKnight,null},
                {redPlayerWalkingShieldLeftKnight}},
                {{redPlayerWalkRightKnight,redPlayerWalkRightArcher},
                        null,
                        null,
                        {redPlayerAttackRightKnight,redPlayerIdleLegsRightArcher},
                        {redPlayerIdleRightKnight,redPlayerIdleRightArcher},
                        {redPlayerWalkAttackRightKnight,redPlayerWalkingLegsRightArcher},
                        {redPlayerIdleShieldRightKnight,null},
                        {redPlayerIdleShieldDrawRightKnight},
                        {redPlayerWalkingShieldRightKnight}}};
        bluPlayer = new Animation[][][]{{{bluPlayerWalkLeftKnight,bluPlayerWalkLeftArcher},null,null,{bluPlayerAttackLeftKnight,bluPlayerIdleLegsLeftArcher}, {bluPlayerIdleLeftKnight,bluPlayerIdleLeftArcher},{bluPlayerWalkAttackLeftKnight,bluPlayerWalkingLegsLeftArcher},{bluPlayerIdleShieldLeftKnight,null},{bluPlayerIdleShieldDrawLeftKnight},{bluPlayerWalkingShieldLeftKnight}},{{bluPlayerWalkRightKnight,bluPlayerWalkRightArcher},null,null,{bluPlayerAttackRightKnight,bluPlayerIdleLegsRightArcher}, {bluPlayerIdleRightKnight,bluPlayerIdleRightArcher},{bluPlayerWalkAttackRightKnight,bluPlayerWalkingLegsRightArcher},{bluPlayerIdleShieldRightKnight,null},{bluPlayerIdleShieldDrawRightKnight},{bluPlayerWalkingShieldRightKnight}}};
    }

    /**
     * quickMakeAnimation<br/>
     * Lets you make Animations without adding extra fluff that takes longer to type
     * @param atlas
     * @param names
     * @param frameTime
     * @return
     */
    private static Animation<TextureRegion> qMakeA(TextureAtlas atlas, String names, float frameTime){
        return new Animation<TextureRegion>(frameTime,atlas.findRegions(names), Animation.PlayMode.LOOP);
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
    public static final int SHIELD_IDLE = 64;
    public static final int SHIELD_WALKING = 128;
    public static final int SHIELD_DRAW_IDLE = 256;
    public static final int SHIELD_DRAW_WALKING = 512;
    public static final int DIRECTION = 1;
    public static final int ISWALKING = 2;
    public static final int ISFALLING = 4;
    public static final int ISJUMPING = 8;
    public static final int ISATTACK = 16;
    public static final int ISIDLE = 32;

    public static final int ATTACKFRAME = 10;



}
