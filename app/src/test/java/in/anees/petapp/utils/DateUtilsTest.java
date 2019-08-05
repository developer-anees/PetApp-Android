package in.anees.petapp.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import in.anees.petapp.model.WorkingTime;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Anees Thyrantakath on 2019-08-04.
 */
@RunWith(MockitoJUnitRunner.class)
public class DateUtilsTest {

    private static final String TAG = "DateUtilsTest";
    /* ----------------------------------------START--------------------------------------------- */
    // Method --> getWorkingTimings(String workHours) --> Returns WorkTime object
    // We need String a with opening and closing time format is -> "M-F 9:00 - 18:00"
    // Scenarios to test are below
    // 1. We need to confirm getWorkingTimings() will return opening and closing Date if we provide string like this-> "M-F 9:00 - 18:00"

    @Test
    public void getWorkingTimings_correctStringPassed_correctWorkTimeObjectReturned() throws ParseException {
        // Arrange
        String workHours = "M-F 04:00 - 14:59";
//         String workHours = "M-F 9:00 - 18:00";
        // Act
        Object result = DateUtils.getWorkingTimings(workHours);
        // Assert
        assertThat(result, instanceOf(WorkingTime.class));
    }
    // 2. Confirm if we provide incorrect Time format it throws -> ParseException
    @Test
    public void getWorkingTimings_incorrectStringPassed_RuntimeReturned() throws ParseException {
        // Arrange
        String workHours = "M-F 912:000 - 184:00";
        Object result = null;
        RuntimeException exception = null;
        // Act
        try {
            result = DateUtils.getWorkingTimings(workHours);
        } catch (RuntimeException e) {
            exception = e;
        }
        // Assert
        assertNull(result);
        assertNotNull(exception);
    }
    // 3. Confirm it throws -> RuntimeException if String not exactly contains two time (opening time and closing time)
    @Test
    public void getWorkingTimings_incorrectStringPassed_RuntimeExceptionReturned() {
        // Arrange
        String workHours = "M-F 9:00 - 18:00 - 14:00";
        Object result = null;
        Exception exception = null;
        // Act
        try {
            result = DateUtils.getWorkingTimings(workHours);
        } catch (ParseException e) {
            exception = e;
        } catch (RuntimeException e) {
            exception = e;
        }
        // Assert
        assertNull(result);
        assertNotNull(exception);
        assertThat(exception, instanceOf(RuntimeException.class));
    }
    // 4. Confirm opening time is less than closing time
    @Test
    public void getWorkingTimings_passedOpeningTimeGreaterThanClosingTime_RuntimeExceptionReturned() throws ParseException {
        // Arrange
        String workHours = "M-F 14:000 - 04:00";
        Object result = null;
        Exception exception = null;
        // Act
        try {
            result = DateUtils.getWorkingTimings(workHours);
        } catch (RuntimeException e) {
            exception = e;
        }
        // Assert
        assertNull(result);
        assertNotNull(exception);
        assertThat(exception, instanceOf(RuntimeException.class));
    }

    /* -----------------------------------------STOP--------------------------------------------- */

    /* -----------------------------------------START-------------------------------------------- */
    // Method --> isWithinWorkingHours(Date currentTime, Date openingTime, Date closingTime) -> Return Boolean
    // Scenarios to test are below
    // 1. Confirm current time is between opening and closing time.
    @Test
    public void isWithinWorkingHours_currentTimeIsBetWeenOpeningAndClosingTime_TrueReturned() throws ParseException {
        // Arrange
        String workHours = "9:00 - 18:00";
        WorkingTime workingTime = DateUtils.getWorkingTimings(workHours);
        String workHoursCurrent = "10:00";

        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(new Date());
        // Setting time to Today's date
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        // SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.US);
        Date date = df.parse(workHoursCurrent);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, calendarToday.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, calendarToday.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, calendarToday.get(Calendar.DAY_OF_MONTH));
        Date openingTime = workingTime.getOpeningTime();
        Date closingTime = workingTime.getClosingTime();

        // Act
        boolean result = DateUtils.isWithinWorkingHours(cal.getTime(), openingTime,closingTime);
        // Assert
        assertTrue(result);
    }

    /* -----------------------------------------STOP--------------------------------------------- */
}