package attendance.mobius.mobius_android;

/**
 * Created by user on 2016-11-27.
 */



public interface OrientationListener {


    public void onOrientationChanged(float azimuth, float pitch, float roll);
    /**
     * Top side of the phone is up
     * The phone is standing on its bottom side
     */
    public void onTopUp();
    /**
     * Bottom side of the phone is up
     * The phone is standing on its top side
     */
    public void onBottomUp();
    /**
     * Right side of the phone is up
     * The phone is standing on its left side
     */
    public void onRightUp();
    /**
     * Left side of the phone is up
     * The phone is standing on its right side
     */
    public void onLeftUp();

}
