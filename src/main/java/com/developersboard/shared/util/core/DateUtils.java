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
    return DateTimeFormatter.ofPattern(DATE_FORMAT).withLocale(Locale.US);
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
   * Format the birthday in the format MM/dd/yyyy.
   *
   * @param date the date
   * @return the formatted date
   */
  static String formatBirthDay(Date date) {
    return formatter().format(toLocalDate(date));
  }

  /**
   * Get difference between two days.
   *
   * @param now the period now
   * @param then the period then
   * @return the number of days
   */
  static long getDifferenceDays(LocalDateTime now, LocalDateTime then) {
    return now.until(then, ChronoUnit.DAYS);
  }

  /**
   * Get difference between two days.
   *
   * @param from day 1
   * @param to day 2
   * @return the number of days
   */
  static long getDifferenceDays(Date from, Date to) {
    long diff = to.getTime() - from.getTime();
    long numberOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    return Math.abs(numberOfDays);
  }

  /**
   * Get the number of hours from the time given.
   *
   * @param localDateTime the localDateTime
   * @return the hours
   */
  static long getHoursFromNow(LocalDateTime localDateTime) {
    return Math.abs(localDateTime.until(LocalDateTime.now(), ChronoUnit.HOURS));
  }

  /**
   * Return a string representation of low long, either days, minutes, hours or seconds an interval
   * is.
   *
   * @param separateDateFormat the separateDate
   * @return the description
   */
  static String getTimeElapsedDescription(SeparateDateFormat separateDateFormat) {
    int thirtyDays = 30;
    if (separateDateFormat.getDays() > thirtyDays) {
      return "Months ago";
    } else {

      int oneDay = 1;
      if (separateDateFormat.getDays() < thirtyDays && separateDateFormat.getDays() > oneDay) {
        return String.format("%s days ago", separateDateFormat.getDays());
      } else if (separateDateFormat.getHours() > 0) {
        return String.format("%s hours ago", separateDateFormat.getHours());
      } else if (separateDateFormat.getMinutes() > 0) {
        return String.format("%s minutes ago", separateDateFormat.getMinutes());
      } else {

        int fiveSeconds = 5;
        if (separateDateFormat.getSeconds() < fiveSeconds) {
          return "now";
        } else {
          return String.format("%s seconds ago", separateDateFormat.getSeconds());
        }
      }
    }
  }

  /**
   * Return a string representation of low long, either days, minutes, hours or seconds an interval
   * is.
   *
   * @param localDateTime the date
   * @return the description
   */
  static String getTimeElapsedDescription(LocalDateTime localDateTime) {
    return getTimeElapsedDescription(getTimeElapse(localDateTime));
  }

  /**
   * Returns the time elapsed for a particular localDateTime.
   *
   * @param dateTillNow the date
   * @return the separateDateFormat
   */
  static SeparateDateFormat getTimeElapse(LocalDateTime dateTillNow) {
    Date d1 = toDate(dateTillNow);
    Date d2 = new Date();
    long diff = d2.getTime() - d1.getTime();
    long seconds = diff / 1000 % 60;
    long minutes = diff / (60 * 1000) % 60;
    long hours = diff / (60 * 60 * 1000) % 24;
    long days = diff / (24 * 60 * 60 * 1000);
    return SeparateDateFormat.builder()
        .seconds(Math.abs(seconds))
        .minutes(Math.abs(minutes))
        .hours(Math.abs(hours))
        .days(Math.abs(days))
        .build();
  }
}
