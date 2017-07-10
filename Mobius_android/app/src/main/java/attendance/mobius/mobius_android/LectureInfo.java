package attendance.mobius.mobius_android;

/**
 * Created by user on 2016-11-26.
 */

public class LectureInfo {
    int lecId;
    String lecName;
    String lecAccess;
    String lecRoom;
    String lecTime;

    public String getLecRoom() {
        return lecRoom;
    }

    public void setLecRoom(String lecRoom) {
        this.lecRoom = lecRoom;
    }

    public String getLecTime() {
        return lecTime;
    }

    public void setLecTime(String lecTime) {
        this.lecTime = lecTime;
    }

    public LectureInfo() {

    }

    public LectureInfo(int lecId, String lecName, String lecAccess, String lecRoom, String lecTime) {
        this.lecId = lecId;
        this.lecName = lecName;
        this.lecAccess = lecAccess;
        this.lecRoom = lecRoom;
        this.lecTime = lecTime;
    }

    public LectureInfo(int lecId, String lecName, String lecAccess) {
        this.lecId = lecId;
        this.lecName = lecName;
        this.lecAccess = lecAccess;

    }

    public String getLecName() {
        return lecName;
    }

    public String getLecAccess() {
        return lecAccess;
    }

    public int getLecId() {
        return lecId;
    }

    public void setLecName(String lecName) {
        this.lecName = lecName;
    }

    public void setLecAccess(String lecAccess) {
        this.lecAccess = lecAccess;
    }

    public void setLecId(int lecId) {
        this.lecId = lecId;
    }
}
