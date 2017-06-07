package com.sxb.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @description handle of date util.
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date Aug 6, 2011, 11:33:15 AM
 * @version 3.0
 */
public class DateUtil {
	private static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
	private static String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * Get the previous time, from how many days to now.
	 * 
	 * @param days How many days.
	 * @return The new previous time.
	 */
	public static Date previous(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -days);
		return cal.getTime();
	}
	
	public static Date previousDate(int days, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, -days);
		return cal.getTime();
	}

	/**
	 * get field of date by calendar class
	 * 
	 * @param calendarField   The field of calendar
	 * 
	 * For the date fields: 
	 *                      YEAR/MONTH
	 *                      
	 *                      星期数指定
	 *                      WEEK_OF_YEAR(指示当前年中的星期数)
	 *                      WEEK_OF_MONTH(指示当前月中的星期数)
	 *                      
	 *                      天数指定
	 *                      DAY_OF_YEAR(指示当前年中的天数)
	 *                      DAY_OF_MONTH(指示一个月中的某天)
	 *                      DAY_OF_WEEK_IN_MONTH(指示当前月中的第几个星期)
	 *                      DAY_OF_WEEK(指示一个星期中的某天)
	 *                      
	 *                      
	 * For the time of day fields:
	 *                            HOUR_OF_DAY(指示一天中的小时)
	 *                            AM_PM + HOUR
	 *                            
	 *                            
	 * @param days   The number of days
	 * @return  date of transform calendar field.
	 */
	public static Date getFieldDateFromCurrent(int calendarField, int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(calendarField, days);
		return cal.getTime();
	}

	/**
	 * current Date forward some minutes
	 * 
	 * @param mins
	 *            The number of forward minute.
	 * @return Been forward new Date.
	 */
	public static Date forwardMin(int mins) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, mins);
		return cal.getTime();
	}
	
	/**
	 * 取得当前周的周六、日日期
	 * 
	 * @param date  默认为当前，也可以指定未来
	 * @return  包含两天的日期（六、日）
	 */
	public static List<Date> getCurrentWeekend(Date date) {
		List<Date> weekend = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		if(RegexUtil.notEmpty(date))cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
		weekend.add(cal.getTime());
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek()+6);
		weekend.add(cal.getTime());
		return weekend;
	}

	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm".
	 */
	public static String formatDateTime(Date d) {
		return new SimpleDateFormat(DATETIME_FORMAT).format(d);
	}

	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm".
	 */
	public static String formatDateTime(long d) {
		return new SimpleDateFormat(DATETIME_FORMAT).format(d);
	}

	/**
	 * Convert date to String like "HH:mm".
	 */
	public static String formatCurDateToTime() {
		Calendar cal = Calendar.getInstance();
		return String.format("%tR", cal);
	}

	public static int getField(int calendarField, Date date) {
		int field = 0;
		Calendar cal = Calendar.getInstance();
		if(RegexUtil.notEmpty(date)) cal.setTime(date);
		field = cal.get(calendarField);
		if(calendarField == Calendar.MONTH) field++;
		return field;
	}

	/**
	 * Convert date to String like "yyyy-MM-dd".
	 */
	public static String formatDate(Date d) {
		return new SimpleDateFormat(DATE_FORMAT).format(d);
	}

	/**
	 * Convert date to String like "yyyy-MM-dd".
	 */
	public static String forMatDate() {
		return new SimpleDateFormat(DATE_FORMAT).format(new Date());
	}

	/**
	 * Parse date like "yyyy-MM-dd".
	 */
	public static Date parseDate(String d) {
		Calendar cal = Calendar.getInstance();
		String[] str = d.split("-");
		cal.set(Integer.parseInt(str[0]), Integer.parseInt(str[1])-1, Integer.parseInt(str[2]));
		return cal.getTime();
	}

	/**
	 * Parse date and time like "yyyy-MM-dd hh:mm".
	 */
	public static Date parseDateTime(String dt) {
		try {
			return new SimpleDateFormat(DATETIME_FORMAT).parse(dt);
		} catch (Exception e) {
		}
		return null;
	}
	
	//获取季度
	public static int getQuarter(Date date) {
		Calendar cal = Calendar.getInstance();
		if(RegexUtil.notEmpty(date)) cal.setTime(date);
		int month = cal.get(Calendar.MONTH) + 1;
		int quarter = 1;
		if(month >= 1 && month <= 3) quarter = 1;
		else if(month >= 4 && month <= 6) quarter = 2;
		else if(month >= 7 && month <= 9) quarter = 3;
		else if(month >= 10 && month <= 12) quarter = 4;
		return quarter;
	}	
}