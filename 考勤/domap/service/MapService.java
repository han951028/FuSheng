package com.mossle.domap.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class MapService {

	/**
	 * 
	 * 获取所在月的第一天 用于between and
	 * 
	 * @param date
	 * @return
	 */
	public Date getNowMonth(String date) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(date));
			calendar.set(Calendar.DATE, 1);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return calendar.getTime();

	}

	/**
	 * 
	 * 获取所在月的最后一天 用于between and
	 * 
	 * @param date
	 * @return
	 */
	public Date getNextMonth(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(date));
			calendar.set(Calendar.DATE, 1);// 设为当前月的1号
			calendar.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
			calendar.add(Calendar.DATE, -1);// 减一天
		} catch (Exception e) {

			e.printStackTrace();
		}
		return calendar.getTime();

	}

	/**
	 * 将时间转换为yyyy-MM-dd格式
	 * 
	 * @param time
	 * @return
	 */
	public String getSftYMD(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		return sdf.format(time);
	}

	/**
	 * 将时间转换为HH:mm格式
	 * 
	 * @param time
	 * @return
	 */
	public String getSftHM(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

		return sdf.format(time);
	}

	/**
	 * 将时间转换为HH:mm:ss格式 然后计算H*3600+M*60+S
	 *
	 * @param time
	 * @return
	 */
	public int getSftHMS(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String sdfDate = sdf.format(time);
		String[] sdfDateArr = sdfDate.split(":");
		int dateH = Integer.parseInt(sdfDateArr[0]);
		int dateM = Integer.parseInt(sdfDateArr[1]);
		int dateS = Integer.parseInt(sdfDateArr[2]);
		int sdfReturn = dateH * 3600 + dateM * 60 + dateS;
		return sdfReturn;
	}

	/**
	 * 将时间转换为yyyy-MM-dd HH:mm:ss格式
	 * 
	 * @param time
	 * @return
	 */
	public String getSftDate(Date time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return sdf.format(time);
	}

	/**
	 * 获取当天周几
	 * 
	 * @param time
	 * @return
	 */
	public String getWeekDate(Date time) {
		Date date = new Date();
		SimpleDateFormat dateFm = new SimpleDateFormat("E");
		String dateStr = dateFm.format(date);
		
		Pattern pattern= Pattern.compile("^[A-Za-z]+$");
		Matcher matcher = pattern.matcher(dateStr);
		if (matcher.find()) {
			switch (dateStr.toLowerCase().substring(0, 2)) {
			case "mo":
				dateStr = "星期一";
				break;
			case "tu":
				dateStr = "星期二";
				break;
			case "we":
				dateStr = "星期三";
				break;
			case "th":
				dateStr = "星期四";
				break;
			case "fr":
				dateStr = "星期五";
				break;
			case "sa":
				dateStr = "星期六";
				break;
			case "su":
				dateStr = "星期日";
				break;
			}
		}
		return dateStr;
	}

	/**
	 * 获取指定周几
	 * 
	 * @param time
	 * @return
	 */
	public String getWeekDay(Date time) {
		SimpleDateFormat dateFm = new SimpleDateFormat("E");
		return dateFm.format(time);
	}

	public List<Date> getBetweenDates(Date begin, Date end) {
		List<Date> result = new ArrayList<Date>();
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(begin);
		while (begin.getTime() <= end.getTime()) {
			result.add(tempStart.getTime());
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
			begin = tempStart.getTime();
		}
		return result;
	}
}
