package HTTP;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by kuush on 6/9/2016.
 */
public class DateTime {

    /**
     * Function to get Data and Time
     *
     * @return
     */
    public static String GetDateAndTime() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());


        String IN_TIME = formattedDate;

        return IN_TIME;
    }
}
