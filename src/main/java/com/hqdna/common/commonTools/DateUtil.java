package com.hqdna.common.commonTools;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 日期时间处理类
 * 
 *
 */
public class DateUtil {
	private static ThreadLocal<DateUtil> du = new ThreadLocal<DateUtil>();
	private SimpleDateFormat sdf = null;
	private SimpleDateFormat sdf2 = null;
	private SimpleDateFormat sdf3 = null;
	private SimpleDateFormat sdf4 = null;
	private SimpleDateFormat sdf5 = null;
	private SimpleDateFormat sdf6 = null;
	private SimpleDateFormat sdf7 = null;
	private SimpleDateFormat sdf8 = null;
	private SimpleDateFormat sdf9 = null;
	private DateUtil() {
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		sdf3 = new SimpleDateFormat("HH:mm:ss");
		/* 不适宜用作文件命名，不符合文件命名规范 */
		sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		/* 用作文件命名 */
		sdf5 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		sdf6 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		sdf7 = new SimpleDateFormat("yyyy/MM/dd");
		sdf8 = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		sdf9 = new SimpleDateFormat("yyMMdd");
	}

	/**
	 * 日期、时间格式 转化格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date) {
		check();
		return du.get().sdf.format(date);
	}
	/**
	 * 日期、时间格式 转化格式为：yyyy-MM-dd_HH-mm-ss
	 * 
	 * @param date
	 * @return
	 */
	public static String formatSDateTime(Date date) {
		check();
		return du.get().sdf8.format(date);
	}
	/**
	 * 日期格式 转化格式为：yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String formatOnlyDate(Date date) {
		check();
		return du.get().sdf2.format(date);
	}
	/**
	 * 
	 * @param date  yyyy-MM-dd形式的字符串
	 * @return
	 */
	public static Date parseOnlyDate(String date) {
		check();
		try {
			return du.get().sdf2.parse(date);
		} catch (ParseException e) {
		}
		return null;
	}
	/**
	 * 
	 * @param date  yyyy/mm/dd形式的字符串
	 * @return
	 */
	public static Date parseOnlyDate02(String date) {
		check();
		try {
			return du.get().sdf7.parse(date);
		} catch (ParseException e) {
		}
		return null;
	}
	/**
	 * 此方法用于增加或减少天数,最后还是返回原来的字符串形式
	 * 
	 * @param date
	 * @param intervalDay
	 * @return
	 */
	public static String dayAddOrSub(String date, int intervalDay) {
		check();
		try {
			Calendar ca = Calendar.getInstance();
			ca.setTime(du.get().sdf2.parse(date));
			ca.add(Calendar.DAY_OF_MONTH, intervalDay);
			return du.get().sdf2.format(ca.getTime());
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 时间格式 转化格式为：HH:mm:ss  只得到时分秒
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTime(Date date) {
		check();
		return du.get().sdf3.format(date);
	}

	/**
	 * 日期、时间、毫秒格式 转化格式为：yyyy-MM-dd HH:mm:ss.SSS
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTimeMS(Date date) {
		check();
		return du.get().sdf4.format(date);
	}

	/**
	 * 用作文件命名 日期、时间、毫秒格式 转化格式为：yyyy-MM-dd-HH-mm-ss-SSS
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateTimeForFN(Date date) {
		check();
		return du.get().sdf5.format(date);
	}

	/**
	 * 用作文件命名 日期、时间、毫秒格式 转化格式为：yyyy-MM-dd-HH-mm-ss-SSS
	 * 
	 * @param datetime
	 * @return
	 */
	public static String formatDateTimeForDT(Timestamp datetime) {
		check();
		return du.get().sdf6.format(datetime);
	}
	
	/**
	 * 时间格式 转化格式为：yyMMdd 6位短日期
	 * 
	 * @param date
	 * @return
	 */
	public static String formatShortDt(Date date) {
		check();
		return du.get().sdf9.format(date);
	}

	/**
	 * 转化格式为：yyyyMMdd carl
	 * 
	 * @param datetime
	 * @return
	 */
	public static String formatDateTimeToStr() {
		Calendar cal = Calendar.getInstance();
		Date d1 = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(d1);
	}

	/**
	 * 日期、时间格式 转化格式为：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datetime
	 * @return
	 */
	public static String formatDateTime(Timestamp datetime) {
		check();
		return du.get().sdf.format(datetime);
	}

	/**
	 * 返回当前时间 by Aaron on 2014.10.22
	 * 
	 * @return
	 */
	public static Timestamp nowTs() {
		return new Timestamp(System.currentTimeMillis());
	}
	/**
	 * 返回当天时间yyyy-MM-dd 00:00:00 用于导入时间
	 * @return
	 */
	public static String currentDayStr() {
		Date now = new Date();
		String nowStr = formatOnlyDate(now);
		nowStr = nowStr + " 00:00:00";
		return nowStr;
	}
	/**
	 * 返回yyyy-MM-dd 00:00:00
	 * @return
	 */
	public static Timestamp currentDay() {
		Date now = new Date();
		String nowStr = formatOnlyDate(now);
		nowStr = nowStr + " 00:00:00";
		return parseDt(nowStr);
	}
	/**
	 * 将yyyy-MM-dd HH:mm:ss形式的字符串转换为Timestamp
	 * @param datetime
	 * @return
	 */
	public static Timestamp parseDt(String datetime) {
		try {
			check();
			Date date = du.get().sdf.parse(datetime);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回当前时间 by Aaron on 2014.10.22
	 * 
	 * @return
	 */
	public static long nowMilli() {
		return System.currentTimeMillis();
	}

	/**
	 * 计算b-a相差 by Aaron on 2014.10.22
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static String cal(long a, long b) {
		long diff = b - a;
		long milliSec = diff % 1000;
		diff = diff / 1000;
		long sec = diff % 60;
		diff = diff / 60;
		long min = diff % 60;
		diff = diff / 60;
		long hour = diff % 60;
		diff = diff / 60;
		return "耗时：" + hour + ":" + min + ":" + sec + "." + milliSec;
	}

	/**
	 * 返回某年某月的最后一天
	 * 
	 * @return
	 */
	public static String getLastDayOfMonth(int year, int month) {
		check();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
		return du.get().sdf2.format(cal.getTime());
	}

	/**
	 * 返回某年某月的第一天
	 * 
	 * @return
	 */
	public static String getFirstDayOfMonth(int year, int month) {
		check();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
		return du.get().sdf2.format(cal.getTime());
	}
	/**
	 * 返回当前时间前timeInterval天时间  "yyyy-MM-dd 
	 */
	public static String getIntervalDate(int timeInterval) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH,-timeInterval);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}
	/**
	 * @param args
	 *            --------------用户测试
	 */
	public static void main(String[] args) {
		// System.out.println(DateUtil.formatDateTime(new Date()));

		long a = System.currentTimeMillis();
		long b = a + 1031;
		System.out.print(cal(a, b));
	}

	private static void check() {
		if (du.get() == null) {
			du.set(new DateUtil());
		}
	}

	/**
	 * 
	 * @param datetime   yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date parseDate(String datetime) {
		try {
			check();
			Date date = du.get().sdf.parse(datetime);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> getDateListBetweenAandB(String startDt,String endDt){
		List<String> dateList = new ArrayList<String>();
		Long startTIme = parseDate(startDt).getTime();
		Long endTime = parseDate(endDt).getTime();
		Long oneDay = 1000 * 60 * 60 * 24l;
		Long time = startTIme;
		while (time <= endTime) {
		    Date d = new Date(time);  
		    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    dateList.add(df.format(d));
		    time += oneDay;
		}
		return dateList;
	}
	
	public static String utc2Local(String utcTime, String utcTimePatten,
			String localTimePatten) {
		SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = localFormater.format(gpsUTCDate.getTime());
		return localTime;
	}
}
