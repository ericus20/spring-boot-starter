package com.developersboard.shared.util.core;

import com.developersboard.web.payload.pojo.SeparateDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DateUtilsTest {

  private transient Date date;
  private transient String expectedDateFormat;

  @BeforeAll
  void beforeAll() {
    date = new GregorianCalendar(2022, Calendar.MAY, 24).getTime();
    expectedDateFormat = "05/24/2022";
  }

  @Test
  void formatLocalDate() {

    LocalDate localDate = DateUtils.toLocalDate(date);
    String formattedLocalDate = localDate.format(DateUtils.formatter());

    Assertions.assertEquals(expectedDateFormat, formattedLocalDate);
  }

  @Test
  void formatLocalDateTime() {

    LocalDateTime localDateTime = DateUtils.toLocalDateTime(date);
    String formattedLocalDateTime = localDateTime.format(DateUtils.formatter());

    Assertions.assertEquals(expectedDateFormat, formattedLocalDateTime);
  }

  @Test
  void convertLocalDateToDate() {
    LocalDate localDate = DateUtils.toLocalDate(date);
    Date dateFromLocalDate = DateUtils.toDate(localDate);

    Assertions.assertEquals(date, dateFromLocalDate);
  }

  @Test
  void convertLocalDateTimeToDate() {
    LocalDateTime localDateTime = DateUtils.toLocalDateTime(date);
    Date dateFromLocalDateTime = DateUtils.toDate(localDateTime);

    Assertions.assertEquals(date, dateFromLocalDateTime);
  }

  @Test
  void testLocalDateTimeDifferenceInDays() {
    int numberOfDays = 1;
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime yesterday = now.minusDays(numberOfDays);

    long differenceInDays = DateUtils.getDifferenceInDays(now, yesterday);
    Assertions.assertEquals(numberOfDays, differenceInDays);
  }

  @Test
  void testDateDifferenceInDays() {
    int numberOfDays = 1;
    Date now = new Date();
    Date yesterday = new DateTime(now).minusDays(numberOfDays).toDate();

    long differenceInDays = DateUtils.getDifferenceInDays(now, yesterday);
    Assertions.assertEquals(numberOfDays, differenceInDays);
  }

  @Test
  void getTimeElapsed() {
    int numberOfDays = 10;
    LocalDateTime localDateTime = LocalDateTime.now().plusDays(numberOfDays);
    SeparateDateFormat timeElapsed = DateUtils.getTimeElapsed(localDateTime);

    Assertions.assertTrue(timeElapsed.getDays() <= numberOfDays);
  }

  @Test
  void getTimeElapsedDescriptionForNow() {
    var localDateTime = LocalDateTime.now();
    var timeElapsedDescription = DateUtils.getTimeElapsedDescription(localDateTime);

    Assertions.assertEquals("just now", timeElapsedDescription);
  }

  @Test
  void getTimeElapsedDescriptionForSeconds() {
    int numberOfSeconds = 20;
    LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(numberOfSeconds);
    String timeElapsedDescription = DateUtils.getTimeElapsedDescription(localDateTime);

    Assertions.assertTrue(timeElapsedDescription.contains("seconds ago"));
  }

  @Test
  void getTimeElapsedDescriptionForMinutes() {
    int numberOfMinutes = 5;
    LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(numberOfMinutes);
    String timeElapsedDescription = DateUtils.getTimeElapsedDescription(localDateTime);

    Assertions.assertTrue(timeElapsedDescription.contains("minutes ago"));
  }

  @Test
  void getTimeElapsedDescriptionForHours() {
    int numberOfHours = 2;
    LocalDateTime localDateTime = LocalDateTime.now().plusHours(numberOfHours);
    String timeElapsedDescription = DateUtils.getTimeElapsedDescription(localDateTime);

    Assertions.assertTrue(timeElapsedDescription.contains("hours ago"));
  }

  @Test
  void getTimeElapsedDescriptionForDays() {
    int numberOfDays = 2;
    LocalDateTime localDateTime = LocalDateTime.now().plusDays(numberOfDays);
    String timeElapsedDescription = DateUtils.getTimeElapsedDescription(localDateTime);

    Assertions.assertTrue(timeElapsedDescription.contains("days ago"));
  }

  @Test
  void getTimeElapsedDescriptionForWeeks() {
    int numberOfWeeks = 2;
    LocalDateTime localDateTime = LocalDateTime.now().plusWeeks(numberOfWeeks);
    String timeElapsedDescription = DateUtils.getTimeElapsedDescription(localDateTime);

    Assertions.assertTrue(timeElapsedDescription.contains("weeks ago"));
  }

  @Test
  void getTimeElapsedDescriptionForMonths() {
    int numberOfMonths = 10;
    LocalDateTime localDateTime = LocalDateTime.now().plusMonths(numberOfMonths);
    String timeElapsedDescription = DateUtils.getTimeElapsedDescription(localDateTime);

    Assertions.assertTrue(timeElapsedDescription.contains("months ago"));
  }

  @Test
  void getTimeElapsedDescriptionForYears() {
    int numberOfYears = 10;
    LocalDateTime localDateTime = LocalDateTime.now().plusYears(numberOfYears);
    String timeElapsedDescription = DateUtils.getTimeElapsedDescription(localDateTime);

    Assertions.assertTrue(timeElapsedDescription.contains("years ago"));
  }
}
