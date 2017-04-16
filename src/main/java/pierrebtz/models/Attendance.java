package pierrebtz.models;

import javax.persistence.Embeddable;

@Embeddable
public enum Attendance {
    ALL, DINNER, ABSENT;

    public static Attendance get(boolean presenceBrunch, boolean absent) {
        if(absent) {
            return Attendance.ABSENT;
        }
        else if(presenceBrunch) {
            return Attendance.ALL;
        }
        return Attendance.DINNER;
    }
}
