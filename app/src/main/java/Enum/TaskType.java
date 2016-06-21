package Enum;

/**
 * Created by kuush on 6/17/2016.
 */
public enum TaskType {

    USER_RATING(1),
    PARK_USER(2),
    PARK_OUT_USER(3);
    int value; private TaskType(int value) { this.value = value; }


}
