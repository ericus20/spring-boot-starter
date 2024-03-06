package com.developersboard.shared.util.core;

import com.developersboard.web.payload.pojo.SeparateDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * This utility class holds all operations on date used in the application.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
public interface DateUtils {

  String DATE_FORMAT = "MM/dd/yyyy";

  /**
   * Formats date in the format Month/day/year like 05/23/2020.
   *
   * @return the formatter
   */
  static DateTimeFormatter formatter() {
    return DateTimeFormatter.ofPattern(DATE_FORMAT).withLocale(Locale.getDefault());
  }

  /**
   * Converts date to localDate.
   *
   * @param dateToConvert the date
   * @return the localDate
   */
  static LocalDate toLocalDate(Date dateToConvert) {
    return LocalDate.ofInstant(dateToConvert.toInstant(), ZoneId.systemDefault());
  }

  /**
   * Converts a date to localDateTime.
   *
   * @param dateToConvert the date
   * @return the localDateTime
   */
  static LocalDateTime toLocalDateTime(Date dateToConvert) {
    return LocalDateTime.ofInstant(dateToConvert.toInstant(), ZoneId.systemDefault());
  }

  /**
   * Converts a localDate to date.
   *
   * @param dateToConvert the localDate
   * @return the date.
   */
  static Date toDate(LocalDate dateToConvert) {
    return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
  }

  /**
   * Converts a localDateTime to date.
   *
   * @param dateToConvert the localDateTime
   * @return the date
   */
  static Date toDate(LocalDateTime dateToConvert) {
    return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
  }

  /**
   * Get difference between two days.
   *
   * @param from the period now
   * @param to the period then
   * @return the number of days
   */
  static long getDifferenceInDays(LocalDateTime from, LocalDateTime to) {
    return Math.abs(from.until(to, ChronoUnit.DAYS));
  }

  /**
   * Get difference between two days.
   *
   * @param from day 1
   * @param to day 2
   * @return the number of days
   */
  static long getDifferenceInDays(Date from, Date to) {
    long diff = to.getTime() - from.getTime();
    long numberOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    return Math.abs(numberOfDays);
  }

  /**
   * Return a string representation of low long, either days, minutes, hours or seconds an interval
   * is.
   *
   * @param localDateTime the date
   * @return the description
   */
  static String getTimeElapsedDescription(LocalDateTime localDateTime) {
    return getTimeElapsedDescription(getTimeElapsed(localDateTime));
  }

  /**
   * Return a string representation of low long, either days, minutes, hours or seconds an interval
   * is.
   *
   * @param separateDateFormat the separateDate
   * @return the description
   */
  static String getTimeElapsedDescription(SeparateDateFormat separateDateFormat) {
    int monthsInYear = 12;
    String durationFormat = "%d %s ago";

    if (separateDateFormat.getMonths() >= monthsInYear) {
      String duration = separateDateFormat.getMonths() == monthsInYear ? "year" : "years";
      return String.format(durationFormat, separateDateFormat.getMonths() / monthsInYear, duration);
    } else if (separateDateFormat.getMonths() > 0) {
      String duration = separateDateFormat.getMonths() == 1 ? "month" : "months";
      return String.format(durationFormat, separateDateFormat.getMonths(), duration);
    } else if (separateDateFormat.getWeeks() > 0) {
      String duration = separateDateFormat.getWeeks() == 1 ? "week" : "weeks";
      return String.format(durationFormat, separateDateFormat.getWeeks(), duration);
    } else if (separateDateFormat.getDays() > 0) {
      String duration = separateDateFormat.getDays() == 1 ? "day" : "days";
      return String.format(durationFormat, separateDateFormat.getDays(), duration);
    } else if (separateDateFormat.getHours() > 0) {
      String duration = separateDateFormat.getHours() == 1 ? "hour" : "hours";
      return String.format(durationFormat, separateDateFormat.getHours(), duration);
    } else if (separateDateFormat.getMinutes() > 0) {
      String duration = separateDateFormat.getMinutes() == 1 ? "minute" : "minutes";
      return String.format(durationFormat, separateDateFormat.getMinutes(), duration);
    } else {
      int seconds = 5;
      if (separateDateFormat.getSeconds() > seconds) {
        return String.format("%d seconds ago", separateDateFormat.getSeconds());
      } else {
        return "just now";
      }
    }
  }

  /**
   * Returns the time elapsed for a particular localDateTime.
   *
   * @param dateTillNow the date
   * @return the separateDateFormat
   */
  static SeparateDateFormat getTimeElapsed(LocalDateTime dateTillNow) {
    LocalDateTime now = LocalDateTime.now();
    long seconds = dateTillNow.until(now, ChronoUnit.SECONDS);
    long minutes = dateTillNow.until(now, ChronoUnit.MINUTES);
    long hours = dateTillNow.until(now, ChronoUnit.HOURS);
    long days = dateTillNow.until(now, ChronoUnit.DAYS);
    long weeks = dateTillNow.until(now, ChronoUnit.WEEKS);
    long months = dateTillNow.until(now, ChronoUnit.MONTHS);

    return SeparateDateFormat.builder()
        .seconds(Math.abs(seconds))
        .minutes(Math.abs(minutes))
        .hours(Math.abs(hours))
        .days(Math.abs(days))
        .weeks(Math.abs(weeks))
        .months(Math.abs(months))
        .build();
  }
}
