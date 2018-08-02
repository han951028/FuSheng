package com.mossle.android.rs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.mossle.domap.domain.MapException;
import com.mossle.domap.domain.MapGroup;
import com.mossle.domap.domain.MapHolidays;
import com.mossle.domap.domain.MapInfo;
import com.mossle.domap.domain.MapPhone;
import com.mossle.domap.manager.MapInfoManager;
import com.mossle.domap.service.MapDaoService;
import com.mossle.domap.service.MapService;
import com.mossle.user.persistence.domain.PersonInfo;
import com.mossle.user.persistence.manager.PersonInfoManager;

import net.sf.json.JSONObject;

@Component
@Path("android/domap")
public class AndroidMapAppResource {
	private static Logger logger = LoggerFactory.getLogger(AndroidMapAppResource.class);
	private MapService mapService;
	private MapDaoService mapDaoService;
	private JdbcTemplate jdbcTemplate;

	@POST
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getUserInfo(@FormParam("userId") String userId) {
		logger.info("getUserInfo start");
		Long userIdLong = Long.parseLong(userId);
		String groupId = mapDaoService.getUserInfoById(userIdLong);
		// 第一层
		JSONObject jsonreturn = new JSONObject();
		try {
			if (groupId != null) {
				// 因请假有可能是半天假需要覆盖choose参数 所以先添加
				jsonreturn.put("choose", 0);
				Long group = Long.parseLong(groupId);// 分组ID
				// 第二层
				JSONObject jsondate = new JSONObject();
				String week = "";
				String startTime = "";
				String endTime = "";
				String departmentId = "";
				// 第三层
				// 获取打卡小组数据
				JSONObject jsongroup = new JSONObject();
				List<MapGroup> groupList = mapDaoService.getMapGroup(group);
				for (MapGroup mapGroup : groupList) {
					jsongroup.put("groupName", mapGroup.getGroupName());
					jsongroup.put("groupConter", mapGroup.getGroupConter());
					jsongroup.put("centerSite", mapGroup.getCenterSite());
					jsongroup.put("radius", mapGroup.getRadius());
					jsongroup.put("startTime", mapGroup.getStartTime());
					jsongroup.put("endTime", mapGroup.getEndTime());
					jsongroup.put("overTime", mapGroup.getOverTime());
					// jsongroup.put("alertTime", mapGroup.getAlertTime());
					startTime = mapGroup.getStartTime();
					endTime = mapGroup.getEndTime();
					week = mapGroup.getGroupWeek();
				}

				// 获取部门ID
				departmentId = mapDaoService.getDepartmentId(userIdLong.toString());
				if (!"".equals(departmentId) && departmentId != null) {
					jsondate.put("departmentId", departmentId);
				} else {
					jsondate.put("departmentId", "");
				}

				// 上次签到的手机型号 暂时不用了
				// String phoneId = mapDaoService.getPhoneName(userIdLong);
				// if (phoneId == null || "".equals(phoneId)) {
				// jsondate.put("phoneId", 400);
				// } else {
				// jsondate.put("phoneId", phoneId);
				// }

				// 判断是否休息
				int num = 0;
				if (!"".equals(week) && week != null) {
					String weekDate = mapService.getWeekDate(new Date());
					logger.info("weekDate" + weekDate);

					String[] weekArr = week.split(",");
					for (int i = 0; i < weekArr.length; i++) {
						logger.info(i + ":" + weekArr[i]);
						if (weekDate.equals(weekArr[i])) {
							logger.info(weekDate);
							num = 1;
							break;
						}
					}
				}
				logger.info(String.valueOf(num));
				// 节假日休假
				List<MapHolidays> holidaysList = mapDaoService.getNowMapHoliDays(new Date());
				if (!holidaysList.isEmpty()) {
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
					logger.info(String.valueOf(num));
					// 请假
					Map<String, Object> getLeaveDate = mapDaoService.getLeaveDate(userIdLong);
					if (!getLeaveDate.isEmpty()) {
						num = 0;
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
								num = 1;
								if (mapService.getSftYMD(new Date()).equals(mapService.getSftYMD(startDate))) {
									String startDateTime = mapService.getSftHM(startDate);
									// 请假时间
									String[] startDateArr = startDateTime.split(":");
									// 实际时间
									String[] startTimeArr = startTime.split(":");
									int startDateF = Integer.parseInt(startDateArr[0]);
									int startDateL = Integer.parseInt(startDateArr[1]);
									int startDateSum = startDateF * 60 + startDateL;
									int startTimeF = Integer.parseInt(startTimeArr[0]);
									int startTimeL = Integer.parseInt(startTimeArr[1]);
									int startTimeSum = startTimeF * 60 + startTimeL;
									if (startDateSum <= startTimeSum) {
										jsongroup.put("startTime", endDateTime);
										jsonreturn.put("choose", 1);
									}
								} else {
									jsongroup.put("startTime", endDateTime);
									jsonreturn.put("choose", 1);
								}
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
								num = 1;
								if (mapService.getSftYMD(new Date()).equals(mapService.getSftYMD(endDate))) {
									String endDateTime = mapService.getSftHM(endDate);
									// 请假时间
									String[] endDateArr = endDateTime.split(":");
									// 实际正常下班时间
									String[] endTimeArr = endTime.split(":");
									int endDateH = Integer.parseInt(endDateArr[0]);
									int endDateM = Integer.parseInt(endDateArr[1]);
									int endDateSum = endDateH * 60 + endDateM;
									int endTimeH = Integer.parseInt(endTimeArr[0]);
									int endTimeM = Integer.parseInt(endTimeArr[1]);
									int endTimeSum = endTimeH * 60 + endTimeM;
									if (endDateSum >= endTimeSum) {
										jsongroup.put("endTime", startDateTime);
										jsonreturn.put("choose", 1);
									}
								} else {
									jsongroup.put("endTime", startDateTime);
									jsonreturn.put("choose", 1);
								}
							}
						}
					}

					logger.info(String.valueOf(num));
					// 出差
					Map<String, Object> getOutDate = mapDaoService.getOutDate(userIdLong);
					if (!getOutDate.isEmpty()) {
						num = 0;
					}
				}

				// 添加考勤组数据
				jsondate.put("group", jsongroup);
				logger.info(String.valueOf(num));
				// 获取当天打卡信息
				List<MapInfo> infoList = mapDaoService.getTodayMapInfo(userIdLong);
				if (!infoList.isEmpty()) {
					// 第三层
					JSONObject jsonInfo = new JSONObject();
					for (MapInfo mapInfo : infoList) {
						jsonInfo.put("userId", mapInfo.getUserId());
						jsonInfo.put("startWorkTime", mapInfo.getStartWorkTime());
						jsonInfo.put("startSite", mapInfo.getStartSite());
						jsonInfo.put("startOut", mapInfo.getStartOut());
						jsonInfo.put("startOutReason", mapInfo.getStartOutReason());
						jsonInfo.put("startStatus", mapInfo.getStartStatus());
						jsonInfo.put("endWorkTime", mapInfo.getEndWorkTime());
						jsonInfo.put("endSite", mapInfo.getEndSite());
						jsonInfo.put("endOut", mapInfo.getEndOut());
						jsonInfo.put("endOutReason", mapInfo.getEndOutReason());
						jsonInfo.put("endStatus", mapInfo.getEndStatus());
					}
					jsondate.put("infoToday", jsonInfo);
				} else {
					// 400为无打卡记录
					jsondate.put("infoToday", 400);
				}

				// num =1 上班 0休息
				if (num == 1) {
					// 最后
					// code 200 今天上班
					jsonreturn.put("code", 200);
					jsonreturn.put("data", jsondate);
					jsonreturn.put("nowTime",
							mapService.getSftYMD(new Date()) + "," + mapService.getSftHMS(new Date()));

				} else {
					// code 300 今天不上班
					jsonreturn.put("code", 300);
					// 5 为不上班
					jsonreturn.put("choose", 5);
					jsonreturn.put("data", jsondate);
					jsonreturn.put("nowTime",
							mapService.getSftYMD(new Date()) + "," + mapService.getSftHMS(new Date()));
				}

			} else {
				// code 500 此人不参加考勤系统
				jsonreturn.put("code", 500);
				jsonreturn.put("data", "");
				jsonreturn.put("nowTime", mapService.getSftYMD(new Date()) + "," + mapService.getSftHMS(new Date()));
				return jsonreturn;
			}
		} catch (Exception e) {
			// 错误404
			logger.error(e.getMessage());
			jsonreturn.put("code", 404);
		}
		return jsonreturn;
	}

	@POST
	@Path("save")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject setUserInfo(@FormParam("userId") String userId, @FormParam("userName") String userName,
			@FormParam("departmentId") String departmentId, @FormParam("currentTime") String currentTime,
			@FormParam("startTime") String startTime, @FormParam("endTime") String endTime,
			@FormParam("workTime") String workTime, @FormParam("site") String site, @FormParam("out") String out,
			@FormParam("outReason") String outReason, @FormParam("choose") String choose,
			@FormParam("overTime") String overTime, @FormParam("groupName") String groupName,
			@FormParam("phoneId") String phoneId, @FormParam("phoneName") String phoneName,
			@FormParam("filename") String filename) {
		JSONObject json = new JSONObject();
		Long userIdL = Long.parseLong(userId);
		List<MapInfo> infoList = mapDaoService.getTodayMapInfo(userIdL);
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		Date currentTimeDate = null;
		try {
			if (infoList.isEmpty()) {
				// 添加早上打卡情况 和半天班
				if ("0".equals(choose) || "1".equals(choose)) {
					int startStatusInt = 0;
					String[] startTimeArr = startTime.split(":");
					String[] workTimeArr = workTime.split(":");
					int startTimeH = Integer.parseInt(startTimeArr[0]);
					int startTimeM = Integer.parseInt(startTimeArr[1]);
					int startTimeSum = startTimeH * 60 + startTimeM;
					int workTimeH = Integer.parseInt(workTimeArr[0]);
					int workTimeM = Integer.parseInt(workTimeArr[1]);
					int workTimeSum = workTimeH * 60 + workTimeM;
					if (startTimeSum >= workTimeSum) {
						startStatusInt = 1;
					} else {
						startStatusInt = 2;
					}
					MapInfo mapInfo = new MapInfo();
					mapInfo.setUserId(userIdL);
					mapInfo.setUserName(userName);
					mapInfo.setDepartmentId(departmentId);
					mapInfo.setCurrentTime(sft.parse(currentTime));
					mapInfo.setChoose(Integer.parseInt(choose));
					mapInfo.setGroupName(groupName);
					mapInfo.setStartTime(startTime);
					mapInfo.setEndTime(endTime);
					mapInfo.setStartWorkTime(workTime);
					mapInfo.setStartOut(Integer.parseInt(out));
					mapInfo.setStartOutReason(outReason);
					mapInfo.setStartSite(site);
					MapException mapException = new MapException();
					// 将手机信息上传至数据库
					if (!"".equals(filename)) {
						// 上传至异常库
						List<MapPhone> mapPhoneList = mapDaoService.getMapPhoneList(phoneId);
						Long exId = null;
						if (!mapPhoneList.isEmpty()) {
							for (MapPhone mapPhone : mapPhoneList) {
								exId = mapPhone.getUserId();
							}
						}
						mapException.setCurrentTime(sft.parse(currentTime));
						mapException.setExId(exId);
						mapException.setPhotoPath(filename);
						mapException.setPhoneId(phoneId);
						mapException.setUserId(Long.parseLong(userId));
						mapException.setPhoneStatus(0);
						mapException.setPhoneName(phoneName);
						mapDaoService.saveMapException(mapException);

					} else {
						// 判断手机库里是否存在 如不存在录入
						List<MapPhone> mapPhoneList = mapDaoService.getMapPhoneByPhoneId(phoneId);
						if (mapPhoneList.isEmpty()) {
							// 录入
							mapDaoService.saveMapPhone(phoneId, phoneName, Long.parseLong(userId));
						}
						mapException = null;
					}

					mapInfo.setStartPhoto(mapException);
					mapInfo.setStartStatus(startStatusInt);
					mapInfo.setEndWorkTime("");
					mapInfo.setEndOut(0);
					mapInfo.setEndOutReason("");
					mapInfo.setEndSite("");
					mapInfo.setEndStatus(0);
					mapInfo.setEndPhoto(null);
					mapInfo.setAllTime("");
					mapInfo.setOverTime(overTime);
					mapInfo.setOverWorkTime("");
					mapInfo.setPhoneId(phoneId);
					mapDaoService.saveMapInfo(mapInfo);
					json.put("code", "200");
				} else if ("5".equals(choose)) {
					MapInfo mapInfo = new MapInfo();
					mapInfo.setUserId(userIdL);
					mapInfo.setUserName(userName);
					mapInfo.setDepartmentId(departmentId);
					mapInfo.setCurrentTime(sft.parse(currentTime));
					mapInfo.setChoose(Integer.parseInt(choose));
					mapInfo.setGroupName(groupName);
					mapInfo.setStartTime(startTime);
					mapInfo.setEndTime(endTime);
					mapInfo.setStartWorkTime(workTime);
					mapInfo.setStartOut(Integer.parseInt(out));
					mapInfo.setStartOutReason(outReason);
					mapInfo.setStartSite(site);
					MapException mapException = new MapException();
					// 将手机信息上传至数据库
					if (!"".equals(filename)) {
						// 上传至异常库
						List<MapPhone> mapPhoneList = mapDaoService.getMapPhoneList(phoneId);
						Long exId = null;
						if (!mapPhoneList.isEmpty()) {
							for (MapPhone mapPhone : mapPhoneList) {
								exId = mapPhone.getUserId();
							}
						}
						mapException.setCurrentTime(sft.parse(currentTime));
						mapException.setExId(exId);
						mapException.setPhotoPath(filename);
						mapException.setPhoneId(phoneId);
						mapException.setUserId(Long.parseLong(userId));
						mapException.setPhoneStatus(0);
						mapException.setPhoneName(phoneName);
						mapDaoService.saveMapException(mapException);
					} else {
						// 判断手机库里是否存在 如不存在录入
						List<MapPhone> mapPhoneList = mapDaoService.getMapPhoneByPhoneId(phoneId);
						if (mapPhoneList.isEmpty()) {
							// 录入
							mapDaoService.saveMapPhone(phoneId, phoneName, Long.parseLong(userId));
						}
						mapException = null;
					}
					mapInfo.setStartPhoto(mapException);
					mapInfo.setStartStatus(1);
					mapInfo.setEndWorkTime("");
					mapInfo.setEndOut(0);
					mapInfo.setEndOutReason("");
					mapInfo.setEndSite("");
					mapInfo.setEndPhoto(null);
					mapInfo.setEndStatus(0);
					mapInfo.setAllTime("");
					mapInfo.setOverTime(overTime);
					mapInfo.setOverWorkTime("");
					mapInfo.setPhoneId(phoneId);
					mapDaoService.saveMapInfo(mapInfo);
					// 将手机信息上传至数据库
					json.put("code", "200");
				}
				return json;
			} else {
				String sql = "UPDATE map_info SET END_WORK_TIME=?,END_SITE=?,END_OUT=?,END_OUT_REASON=?,START_STATUS=?,END_STATUS=?,CHOOSE=?,ALL_TIME=?,OVER_TIME=?,OVER_WORK_TIME=?,START_TIME=?,END_TIME=? ,PHONE_ID=? ,END_PHOTO=?  WHERE ID =?";
				Long id = new Long(0);
				String startWorkTime = null;
				for (MapInfo mapInfo : infoList) {
					id = mapInfo.getId();
					startWorkTime = mapInfo.getStartWorkTime();
				}

				// 获取实际早上打卡时间
				String[] startWorkTimeArr = startWorkTime.split(":");
				int startWorkTimeH = Integer.parseInt(startWorkTimeArr[0]);
				int startWorkTimeM = Integer.parseInt(startWorkTimeArr[1]);
				int startWorkTimeSum = startWorkTimeH * 60 + startWorkTimeM;

				// 获取实际下班时间
				String[] workTimeArr = workTime.split(":");
				int workTimeH = Integer.parseInt(workTimeArr[0]);
				int workTimeM = Integer.parseInt(workTimeArr[1]);
				int workTimeSum = workTimeH * 60 + workTimeM;

				// 签到状态
				int startStatusInt = 1;

				// 正常上下班 和半天班
				if ("0".equals(choose) || "1".equals(choose)) {
					// 获取早上打卡时间
					String[] startTimeArr = startTime.split(":");
					int startTimeH = Integer.parseInt(startTimeArr[0]);
					int startTimeM = Integer.parseInt(startTimeArr[1]);
					int startTimeSum = startTimeH * 60 + startTimeM;
					if (startTimeSum >= startWorkTimeSum) {
						startStatusInt = 1;
					} else {
						startStatusInt = 2;
					}

					// 获取规定下班时间
					String[] endTimeArr = endTime.split(":");
					int endTimeH = Integer.parseInt(endTimeArr[0]);
					int endTimeM = Integer.parseInt(endTimeArr[1]);
					int endTimeSum = endTimeH * 60 + endTimeM;

					// 获取设置的加班点
					int overWorkTime = 0;
					if (!"".equals(overTime)) {
						String[] overDateArr = overTime.split(":");
						int overDateH = Integer.parseInt(overDateArr[0]);
						int overDateM = Integer.parseInt(overDateArr[1]);
						overWorkTime = overDateH * 60 + overDateM;
					}
					// 全天上班时间XX：XX
					int allTimeSum = workTimeSum - startWorkTimeSum;
					int allTimeH = allTimeSum / 60;
					int allTimeM = allTimeSum % 60;
					String allTimeHStr = String.valueOf(allTimeH);
					String allTimeMStr = String.valueOf(allTimeM);
					if (allTimeH < 10) {
						allTimeHStr = "0" + allTimeH;
					}
					if (allTimeM < 10) {
						allTimeMStr = "0" + allTimeM;
					}
					String allTimeReturn = "";
					allTimeReturn = allTimeHStr + ":" + allTimeMStr;
					String overTimeReturn = "";
					if (overWorkTime != 0) {
						// 添加加班时间
						if (workTimeSum >= overWorkTime) {
							if (startWorkTimeSum <= overWorkTime) {
								int overDateMin = workTimeSum - overWorkTime;
								int overDateMinH = overDateMin / 60;
								int overDateMinM = overDateMin % 60;
								String overDateMinHStr = String.valueOf(overDateMinH);
								String overDateMinMStr = String.valueOf(overDateMinM);
								if (overDateMinH < 10) {
									overDateMinHStr = "0" + overDateMinH;
								}
								if (overDateMinM < 10) {
									overDateMinMStr = "0" + overDateMinM;
								}
								overTimeReturn = overDateMinHStr + ":" + overDateMinMStr;
							} else {
								overTimeReturn = allTimeReturn;
							}
						}
					}
					// 下班签到状态
					int endStatusInt = 0;
					if (workTimeSum >= endTimeSum) {
						endStatusInt = 1;
					} else {
						endStatusInt = 2;
					}

					String mapExceptionRet = null;
					// 删除当天下午异常库里的信息 清理垃圾数据
					mapDaoService.deleteMapExceptionByNow(currentTime, userId);

					// 将手机信息上传至数据库
					if (!"".equals(filename)) {
						// 上传至异常库
						List<MapPhone> mapPhoneList = mapDaoService.getMapPhoneList(phoneId);
						Long exId = null;
						if (!mapPhoneList.isEmpty()) {
							for (MapPhone mapPhone : mapPhoneList) {
								exId = mapPhone.getUserId();
							}
						}
						MapException mapException = new MapException();
						mapException.setCurrentTime(sft.parse(currentTime));
						mapException.setExId(exId);
						mapException.setPhotoPath(filename);
						mapException.setPhoneId(phoneId);
						mapException.setUserId(Long.parseLong(userId));
						mapException.setPhoneStatus(1);
						mapException.setPhoneName(phoneName);
						mapDaoService.saveMapException(mapException);
						mapExceptionRet = mapException.getId().toString();
					} else {
						// 判断手机库里是否存在 如不存在录入
						List<MapPhone> mapPhoneList = mapDaoService.getMapPhoneByPhoneId(phoneId);
						if (mapPhoneList.isEmpty()) {
							// 录入
							mapDaoService.saveMapPhone(phoneId, phoneName, Long.parseLong(userId));
						}
					}
					jdbcTemplate.update(sql, workTime, site, Integer.parseInt(out), outReason, startStatusInt,
							endStatusInt, choose, allTimeReturn, overTime, overTimeReturn, startTime, endTime, phoneId,
							mapExceptionRet, id);
					json.put("code", 200);

				} else if ("5".equals(choose)) {
					// 全天上班时间XX：XX
					int allTimeSum = workTimeSum - startWorkTimeSum;
					int allTimeH = allTimeSum / 60;
					int allTimeM = allTimeSum % 60;
					String allTimeHStr = String.valueOf(allTimeH);
					String allTimeMStr = String.valueOf(allTimeM);
					if (allTimeH < 10) {
						allTimeHStr = "0" + allTimeH;
					}
					if (allTimeM < 10) {
						allTimeMStr = "0" + allTimeM;
					}
					String allTimeReturn = allTimeHStr + ":" + allTimeMStr;
					startStatusInt = 1;
					int endStatusInt = 1;
					String overTimeReturn = "";
					if ("".equals(overTime)) {
						overTimeReturn = "";
					} else {
						overTimeReturn = allTimeReturn;
					}

					String mapExceptionRet = null;
					// 删除当天下午异常库里的信息 清理垃圾数据
					mapDaoService.deleteMapExceptionByNow(currentTime, userId);

					// 将手机信息上传至数据库
					if (!"".equals(filename)) {
						// 上传至异常库
						List<MapPhone> mapPhoneList = mapDaoService.getMapPhoneList(phoneId);
						Long exId = null;
						if (!mapPhoneList.isEmpty()) {
							for (MapPhone mapPhone : mapPhoneList) {
								exId = mapPhone.getUserId();
							}
						}
						MapException mapException = new MapException();
						mapException.setCurrentTime(sft.parse(currentTime));
						mapException.setExId(exId);
						mapException.setPhotoPath(filename);
						mapException.setPhoneId(phoneId);
						mapException.setUserId(Long.parseLong(userId));
						mapException.setPhoneStatus(1);
						mapException.setPhoneName(phoneName);
						mapDaoService.saveMapException(mapException);
						mapExceptionRet = mapException.getId().toString();
					} else {
						// 判断手机库里是否存在 如不存在录入
						List<MapPhone> mapPhoneList = mapDaoService.getMapPhoneByPhoneId(phoneId);
						if (mapPhoneList.isEmpty()) {
							// 录入
							mapDaoService.saveMapPhone(phoneId, phoneName, Long.parseLong(userId));
						}
					}
					jdbcTemplate.update(sql, workTime, site, Integer.parseInt(out), outReason, startStatusInt,
							endStatusInt, choose, allTimeReturn, overTime, overTimeReturn, startTime, endTime, phoneId,
							mapExceptionRet, id);
					json.put("code", 200);
				}
				return json;
			}
		} catch (Exception e) {
			json.put("code", 404);
			return json;
		}

	}

	//
	// @RequestMapping("map-Info-list")
	// @ResponseBody
	// public JSONObject getUserInfo(Long userId,String date){
	// //出勤天数
	// int days = 0;
	// //休息天数
	// int holidays = 0;
	// //迟到
	// int lateDays = 0;
	// //早退
	// int leaveDays = 0;
	// //缺卡
	// int loseDays=0;
	// //旷工
	// int
	// //外勤
	// int outDays=0;
	// //加班
	// int overDays = 0;
	// Date monthF = mapService.getNextMonth(date);
	// Date monthL = mapService.getNowMonth(date);
	// List<MapInfo> mapInfoList= mapDaoService.getMonthMapInfo(userId, monthF,
	// monthL);
	// for (MapInfo mapInfo : mapInfoList) {
	//
	// }
	// return null;
	// }

	@POST
	@Path("phoneId")
	@Produces(MediaType.APPLICATION_JSON)
	public JSONObject getPhone(@FormParam("userId") String userId, @FormParam("phoneId") String phoneId) {
		JSONObject countJson = new JSONObject();
		if ("".equals(phoneId)) {
			countJson.put("code", 200);
			countJson.put("data", 0);
			return countJson;
		}
		try {
			int count = mapDaoService.getPhoneNameEmpty(Long.parseLong(userId), phoneId);

			if (count == 0) {
				countJson.put("code", 200);
				countJson.put("data", 0);
				return countJson;
			} else {
				countJson.put("code", 200);
				countJson.put("data", 1);
				return countJson;
			}
		} catch (Exception e) {
			countJson.put("code", 400);
			return countJson;
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
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
