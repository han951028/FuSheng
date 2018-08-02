package com.mossle.domap.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mossle.domap.domain.MapGroup;
import com.mossle.domap.domain.MapHolidays;
import com.mossle.domap.domain.MapInfo;
import com.mossle.domap.domain.MapUser;
import com.mossle.domap.manager.MapInfoManager;
import com.mossle.domap.service.MapDaoService;
import com.mossle.domap.service.MapService;

@Component
public class MapScheduler {

	private MapService mapService;
	private MapDaoService mapDaoService;
	private MapInfoManager mapInfoManager;
	private JdbcTemplate jdbcTemplate;

// 测试
	
	
	//每天0点修改前一天的打卡信息
	@Scheduled(cron = "0 0 0 * * ?")
//	 @Scheduled(cron = "* * * * * ?")
	public void execute() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.add(Calendar.DATE, -1);// 减一天
		} catch (Exception e) {
			e.printStackTrace();
		}
		String yesterdayStr = sdf.format(calendar.getTime());
		Date yesterday = calendar.getTime();
		try {
			yesterday = sdf.parse(yesterdayStr);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			yesterday = calendar.getTime();
		}
		List<MapGroup> mapGroupList = mapDaoService.getALLMapGroup();
		for (MapGroup mapGroup : mapGroupList) {
			Long groupId = mapGroup.getGroupId();
			String groupName = mapGroup.getGroupName();
			String groupWeek = mapGroup.getGroupWeek();
			String yesterdayWeek = mapService.getWeekDay(yesterday);
			String[] weekArr = groupWeek.split(",");
			String startTime = mapGroup.getStartTime();
			String endTime = mapGroup.getEndTime();
			String overTime = "";
			overTime = mapGroup.getOverTime();
			int num = 0;
			// 获取今天上不上班
			for (int i = 0; i < weekArr.length; i++) {
				if (yesterdayWeek.equals(weekArr[i])) {
					num = 1;
					break;
				}
			}
			// 节假日休假
			List<MapHolidays> holidaysList = mapDaoService.getNowMapHoliDays(yesterday);
			if (holidaysList != null) {
				for (MapHolidays mapHolidays : holidaysList) {
					if (mapHolidays.getStatus() == 1) {
						num = 1;
					}
					if (mapHolidays.getStatus() == 0) {
						num = 0;
					}
				}
			}
			if (num == 1) {
				// 获取指定考勤组里的员工
				List<MapUser> mapUserList = mapDaoService.getMapUserInfo(groupId);

				for (MapUser mapUser : mapUserList) {
					Long userId = mapUser.getUserId();
					String userName = mapDaoService.getUserName(userId);
					List<MapInfo> mapInfoList = mapDaoService.getTodayMapInfoByDay(userId, yesterday);
					if (mapInfoList.isEmpty()) {
						String departmentId = mapDaoService.getDepartmentId(userId.toString());
						if ("".equals(departmentId) || departmentId == null) {
							departmentId = "";
						} 
						
						int choose = 2;
						// 请假
						Map<String, Object> getLeaveDate = new HashMap<>();
						try {
							getLeaveDate = mapDaoService.getLeaveDateByDay(userId, yesterday);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (!getLeaveDate.isEmpty()) {
							// 请假3
							choose = 3;
							Date startDate = (Date) getLeaveDate.get("startDate");
							Date endDate = (Date) getLeaveDate.get("endDate");
							if (mapService.getSftYMD(new Date()).equals(mapService.getSftYMD(endDate))) {

								String endDateTime = mapService.getSftHM(endDate);
								// 请假时间
								String[] endDateArr = endDateTime.split(":");
								// 实际考勤下班时间
								String[] endTimeArr = endTime.split(":");
								int endDateF = Integer.parseInt(endDateArr[0]);
								int endDateL = Integer.parseInt(endDateArr[1]);
								int endDateSum = endDateF * 60 + endDateL;
								int endTimeF = Integer.parseInt(endTimeArr[0]);
								int endTimeL = Integer.parseInt(endTimeArr[1]);
								int endTimeSum = endTimeF * 60 + endTimeL;
								if (endDateSum < endTimeSum) {
									choose = 1;
								}
							}
							if (mapService.getSftYMD(new Date()).equals(mapService.getSftYMD(startDate))) {

								String startDateTime = mapService.getSftHM(startDate);
								// 请假时间
								String[] startDateArr = startDateTime.split(":");
								// 实际考勤上班时间
								String[] startTimeArr = startTime.split(":");
								int startDateH = Integer.parseInt(startDateArr[0]);
								int startDateM = Integer.parseInt(startDateArr[1]);
								int startDateSum = startDateH * 60 + startDateM;
								int startTimeH = Integer.parseInt(startTimeArr[0]);
								int startTimeM = Integer.parseInt(startTimeArr[1]);
								int startTimeSum = startTimeH * 60 + startTimeM;
								if (startDateSum > startTimeSum) {
									choose = 1;
								}
							}
						}
						// 出差
						Map<String, Object> getOutDate = new HashMap<>();
						try {
							getOutDate = mapDaoService.getOutDateByDay(userId, yesterday);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (!getOutDate.isEmpty()) {
							// 出差4
							choose = 4;
						}
						// 修改数据库添加当天缺卡的考勤情况
						if (choose != 1) {
							MapInfo mapInfo = new MapInfo();
							mapInfo.setUserId(userId);
							mapInfo.setUserName(userName);
							mapInfo.setCurrentTime(yesterday);
							mapInfo.setChoose(choose);
							mapInfo.setGroupName(groupName);
							mapInfo.setDepartmentId(departmentId);
							mapInfo.setStartTime(startTime);
							mapInfo.setEndTime(endTime);
							mapInfo.setStartWorkTime("");
							mapInfo.setStartOut(0);
							mapInfo.setStartOutReason("");
							mapInfo.setStartSite("");
							mapInfo.setStartStatus(0);
							mapInfo.setEndWorkTime("");
							mapInfo.setEndOut(0);
							mapInfo.setEndOutReason("");
							mapInfo.setEndSite("");
							mapInfo.setEndStatus(0);
							mapInfo.setAllTime("");
							mapInfo.setOverTime(overTime);
							mapInfo.setOverWorkTime("");
							mapInfo.setPhoneId("");
							mapInfoManager.save(mapInfo);
						}

					}

				}

			} else if (num == 0) {
				// 添加休息日时候的考勤信息
				// 获取指定考勤组里的员工
				List<MapUser> mapUserList = mapDaoService.getMapUserInfo(groupId);
				for (MapUser mapUser : mapUserList) {
					Long userId = mapUser.getUserId();
					String userName = mapDaoService.getUserName(userId);
					String departmentId = mapDaoService.getDepartmentId(userId.toString());
					if ("".equals(departmentId) || departmentId == null) {
						departmentId = "";
					} 
					List<MapInfo> mapInfoList = mapDaoService.getTodayMapInfoByDay(userId, yesterday);
					if (mapInfoList.isEmpty()) {
						int choose = 6;
						MapInfo mapInfo = new MapInfo();
						mapInfo.setUserId(userId);
						mapInfo.setUserName(userName);
						mapInfo.setCurrentTime(yesterday);
						mapInfo.setChoose(choose);
						mapInfo.setGroupName(groupName);
						mapInfo.setDepartmentId(departmentId);
						mapInfo.setStartTime(startTime);
						mapInfo.setEndTime(endTime);
						mapInfo.setStartWorkTime("");
						mapInfo.setStartOut(0);
						mapInfo.setStartOutReason("");
						mapInfo.setStartSite("");
						mapInfo.setStartStatus(0);
						mapInfo.setEndWorkTime("");
						mapInfo.setEndOut(0);
						mapInfo.setEndOutReason("");
						mapInfo.setEndSite("");
						mapInfo.setEndStatus(0);
						mapInfo.setAllTime("");
						mapInfo.setOverTime(overTime);
						mapInfo.setOverWorkTime("");
						mapInfo.setPhoneId("");
						mapInfoManager.save(mapInfo);
					}
				}
			}
		}
	}

	@Resource
	public void setMapService(MapService mapService) {
		this.mapService = mapService;
	}
	@Resource
	public void setMapDaoService(MapDaoService mapDaoService) {
		this.mapDaoService = mapDaoService;
	}
	@Resource
	public void setMapInfoManager(MapInfoManager mapInfoManager) {
		this.mapInfoManager = mapInfoManager;
	}
	@Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
}
