package me.dumfing.gdxtools;

/**
 * Created by dumpl on 5/12/2017.
 */
public class MathTools {
    /**
     * Moves a number towards zero by a given amount
     * @param numIn Number to be moved
     * @param amount Amount to move number by
     * @return The number moved by the given amount
     */
    public static float towardsZero(float numIn, float amount){
            if(numIn > 0) {
                return Math.max(0, numIn - amount);
            }
            else if(numIn < 0){
                return Math.min(0, numIn + amount);
            }
            else{
                return numIn;
            }
    }
    @Deprecated
    public static float towardsZero(float numIn){
        return towardsZero(numIn,0.5f);
    }
}
