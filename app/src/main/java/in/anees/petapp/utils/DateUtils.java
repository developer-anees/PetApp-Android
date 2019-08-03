package in.anees.petapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.anees.petapp.model.WorkingTime;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public class DateUtils {

    private DateUtils() {
        // This utility class is not publicly instantiable
    }

    /**
     * Use this method to check the user contacting time is between working hours.
     *
     * @param currentTime Parameter 1, Pass the current time.
     * @param openingTime Parameter 2, Pass Pet shop opening time.
     * @param closingTime Parameter 3, Pass Pet shop closing time.
     * @return boolean result.
     */
    public static boolean isWithinWorkingHours(Date currentTime, Date openingTime,
                                               Date closingTime) {
        return !(currentTime.before(openingTime) || currentTime.after(closingTime));
    }

    /**
     * This method will parse the String and get Opening and Closing Time. Then it returns
     * WorkingTime Object.
     * Example format of String expected: "M-F 9:00 - 18:00"
     * Note: This concept is written based on above String format, in future if any format changes
     * then we need to modify this method accordingly.
     *
     * @param workHours Parameter 1, Pass String workingHours.
     * @return WorkingTime Object.
     */
    public static WorkingTime getWorkingTimings(String workHours) throws ParseException {
        final Pattern pattern = Pattern.compile("(\\d*\\d:\\d\\d)");
        final Matcher matcher = pattern.matcher(workHours);
        ArrayList<String> timingList = new ArrayList<>(2);
        while (matcher.find()) {
            timingList.add(matcher.group());
        }

        //We expect it will have only two values
        if (timingList.size() == 2) {
            WorkingTime workingTime = new WorkingTime();
            workingTime.setOpeningTime(convertTimeToPresentDate(timingList.get(0)))
                    .setClosingTime(convertTimeToPresentDate(timingList.get(1)));

            return workingTime;
        }
        throw new RuntimeException("Wrong work hour syntax :" + workHours);
    }

    /**
     * This method will convert the Time into Present date.
     *
     * @param time Parameter 1, Pass String time.
     *             Example format : 9:00 or 18:00
     * @return Date Object.
     */
    private static Date convertTimeToPresentDate(String time) throws ParseException {
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(new Date());

        // Setting time to Today's date
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        // SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.US);
        Date date = df.parse(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, calendarToday.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, calendarToday.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, calendarToday.get(Calendar.DAY_OF_MONTH));

        return cal.getTime();
    }
}
