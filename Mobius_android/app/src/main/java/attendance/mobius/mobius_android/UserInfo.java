package attendance.mobius.mobius_android;

/**
 * Created by user on 2016-11-26.
 */

public class UserInfo {
    int userId;
    String userName;
    boolean isPro;

    public UserInfo() {
    }

    public UserInfo(int userId, String userName, boolean isPro) {

        this.userId = userId;
        this.userName = userName;
        this.isPro = isPro;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public boolean getIsPro() {
        return isPro;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setIsPro(boolean pro) {
        isPro = pro;
    }
}
