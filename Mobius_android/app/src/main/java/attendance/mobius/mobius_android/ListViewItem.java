package attendance.mobius.mobius_android;

/**
 * Created by user on 2016-11-12.
 */

public class ListViewItem {
    String lectureName;
    String lectureRoom;
    String lectureTime;

    public String getLectureName() {
        return lectureName;
    }

    public String getLectureRoom() {
        return lectureRoom;
    }

    public String getLectureTime() {
        return lectureTime;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public void setLectureRoom(String lectureRoom) {
        this.lectureRoom = lectureRoom;
    }

    public void setLectureTime(String lectureTime) {
        this.lectureTime = lectureTime;
    }
}
