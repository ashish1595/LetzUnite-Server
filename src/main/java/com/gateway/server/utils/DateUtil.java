package com.gateway.server.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtil {

	public static final String ZONE_ID = "Asia/Kolkata";
	public static final String MMDDYYY = "MM/dd/yyyy";
	public static final String DDMMYYY = "dd-MM-yyyy";


	public static long getMinusDayTimestamp(int minusDays) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime daysAgo = now.minusDays(minusDays);
		LocalDateTime midnight = daysAgo.toLocalDate().atStartOfDay();
		ZoneId zoneId = ZoneId.of(ZONE_ID); // or: ZoneId.of("Europe/Oslo");
		return midnight.atZone(zoneId).toEpochSecond();
	}

	public static long getStartOfDayTimestamp() {
		LocalDateTime now2 = LocalDateTime.now();
		LocalDateTime midnight = now2.toLocalDate().atStartOfDay();
		ZoneId zoneId = ZoneId.of(ZONE_ID);
		return midnight.atZone(zoneId).toEpochSecond();
	}

	public static long getStartOfDayTimestamp(long time) {
		if(time != 0L) {
			LocalDateTime now = LocalDateTime.ofInstant(Instant.ofEpochSecond(time),  ZoneId.of(ZONE_ID));
			LocalDateTime midnight = now.toLocalDate().atStartOfDay();
			return midnight.atZone(ZoneId.of(ZONE_ID)).toEpochSecond();
		}

		return 0L;
	}

	public static long getNextDayOfTimestamp(long time) {
		if(time == 0L)
			return 0L;
		LocalDateTime now = LocalDateTime.ofInstant(Instant.ofEpochSecond(time), ZoneId.of(ZONE_ID));
		LocalDateTime midnight = now.plusDays(1).toLocalDate().atStartOfDay();
		return midnight.atZone(ZoneId.of(ZONE_ID)).toEpochSecond();
	}
	
	public static Date getExpiryDate() {
//		LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.of(ZONE_ID));
//		LocalDateTime midnight = now.plusDays(Constants.EXPIRY_DAYS).toLocalDate().atStartOfDay();
//		return midnight.atZone(ZoneId.of(ZONE_ID)).toEpochSecond();
		
		Calendar today = Calendar.getInstance();
		today.setTimeZone(TimeZone.getTimeZone(ZONE_ID));
		today.add(Calendar.DAY_OF_MONTH, Constants.EXPIRY_DAYS);
		return today.getTime();
	}

	public static Long getTodayTimeStamp() {
		Calendar today = Calendar.getInstance();
		today.setTimeZone(TimeZone.getTimeZone(ZONE_ID));
		return today.getTime().getTime();
	}
	
	public static Date getTodaysDate() {
		Calendar today = Calendar.getInstance();
		today.setTimeZone(TimeZone.getTimeZone(ZONE_ID));
		today.setTime(new Date());
		return today.getTime();
	}
	
	//
	public static Date getDateFromStampWithStrip(Long timestamp) {
		Calendar date = Calendar.getInstance();
		date.setTimeZone(TimeZone.getTimeZone(ZONE_ID));
		date.setTime(new Date(timestamp));
		setZero(date);
		return date.getTime();
	}

	public static Calendar getTodayCalendar() {
		Calendar today = Calendar.getInstance();
		today.setTimeZone(TimeZone.getTimeZone(ZONE_ID));
		return today;
	}

	public static Boolean isToday(Timestamp date) {
		Calendar userDate = Calendar.getInstance();
		userDate.setTime(date);
		Calendar today = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DDMMYYY);
		sdf.setTimeZone(TimeZone.getTimeZone(ZONE_ID));
		return sdf.format(today.getTime()).equals(sdf.format(userDate.getTime()));
	}

	public static String getStringDate(){
		SimpleDateFormat dateFormat = new SimpleDateFormat(DDMMYYY);
		return dateFormat.format(getTodayTimeStamp());
	}

	public static String getStringDate(String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(getTodayTimeStamp());
	}

	public static String getDateMMDDYYY(Timestamp date) {
		SimpleDateFormat sdf = new SimpleDateFormat(MMDDYYY);
		sdf.setTimeZone(TimeZone.getTimeZone(ZONE_ID));
		Calendar userDate = Calendar.getInstance();
		userDate.setTime(date);
		return sdf.format(userDate.getTime());
	}

	public static  Timestamp parseDate(String date,String format) {
		date= ""+date.trim();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(TimeZone.getTimeZone(ZONE_ID));
		try {
			sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return new Timestamp(sdf.getCalendar().getTime().getTime());
	}

	public static long getDiffrence(Timestamp date) {
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(date.getTime());
		time = setZero(time);
		Calendar today = Calendar.getInstance();
		today = setZero(today);
		long diff = today.getTime().getTime() - time.getTime().getTime();
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	public static  Calendar setZero(Calendar time){
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		return time;
	}

	public static Long getTimeStampMinusN(Integer n) {
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE, -n);
		today.setTimeZone(TimeZone.getTimeZone(ZONE_ID));
		return today.getTime().getTime();
	}
	
	public static Long getTimeStampPlusN(Integer n) {
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE, n);
		today.setTimeZone(TimeZone.getTimeZone(ZONE_ID));
		return today.getTime().getTime();
	}

	public static Date getTimeBeforeStartOfTheDay() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
		cal.set(Calendar.MINUTE, 0);                 // set minute in hour
		cal.set(Calendar.SECOND, 0);                 // set second in minute
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
}