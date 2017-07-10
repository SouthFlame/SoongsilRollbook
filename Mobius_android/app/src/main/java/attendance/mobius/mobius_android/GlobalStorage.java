package attendance.mobius.mobius_android;

import android.app.Application;

import java.util.ArrayList;

/**
 * Created by user on 2016-11-26.
 */

public class GlobalStorage extends Application{
    public UserInfo userInfo;
    public ArrayList<LectureInfo> lectureInfos;


    @Override
    public void onCreate() {
//        userInfo = new UserInfo();
//        lectureInfos = new ArrayList<LectureInfo>();
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public ArrayList<LectureInfo> getLectureInfos() {
        return lectureInfos;
    }

    public void setLectureInfos(ArrayList<LectureInfo> lectureInfos) {
        this.lectureInfos = lectureInfos;
    }

}
