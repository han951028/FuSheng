package com.mossle.domap.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.mossle.domap.domain.MapException;
import com.mossle.domap.domain.MapGroup;
import com.mossle.domap.domain.MapHolidays;
import com.mossle.domap.domain.MapInfo;
import com.mossle.domap.domain.MapPhone;
import com.mossle.domap.domain.MapUser;
import com.mossle.domap.manager.MapExceptionManager;
import com.mossle.domap.manager.MapGroupManager;
import com.mossle.domap.manager.MapHolidaysManager;
import com.mossle.domap.manager.MapInfoManager;
import com.mossle.domap.manager.MapPhoneManager;
import com.mossle.domap.manager.MapUserManager;
import com.mossle.employee.persistence.domain.EmployeeLeaveInfo;
import com.mossle.employee.persistence.domain.EmployeeOutInfo;
import com.mossle.employee.persistence.manager.EmployeeLeaveInfoManager;
import com.mossle.employee.persistence.manager.EmployeeOutInfoManager;
import com.mossle.org.persistence.domain.OrgDepartment;
import com.mossle.org.persistence.manager.OrgDepartmentManager;
import com.mossle.user.persistence.manager.AccountInfoManager;
import com.mossle.user.persistence.manager.PersonInfoManager;

@Service
public class MapDaoService {

	private MapGroupManager mapGroupManager;
	private MapUserManager mapUserManager;
	private MapInfoManager mapInfoManager;
	private MapHolidaysManager mapHolidaysManager;
	private JdbcTemplate jdbcTemplate;
	private EmployeeLeaveInfoManager employeeLeaveInfoManager;
	private EmployeeOutInfoManager employeeOutInfoManager;
	private AccountInfoManager accountInfoManager;
	private PersonInfoManager personInfoManager;
	private MapPhoneManager mapPhoneManager;
	private MapExceptionManager mapExceptionManager;
	private OrgDepartmentManager orgDepartmentManager;
	/**
	 * 根据ID获取打卡的分组 如果没有此人的信息则返回空
	 * 
	 * @param userId
	 * @return
	 */
	public String getUserInfoById(Long userId) {
		String hql = "from MapUser where userId=?";
		List<MapUser> list = mapUserManager.find(hql, userId);
		String returnId = null;
		if (list == null) {
			return null;

		} else {
			for (MapUser mapUser : list) {
				returnId = String.valueOf(mapUser.getUserGroup().getGroupId());

			}
			return returnId;
		}

	}

	/**
	 * 根据获取的分组查询对应分组的数据
	 * 
	 * @param groupId
	 * @return
	 */

	public List<MapGroup> getMapGroup(Long groupId) {

		String hql = "from MapGroup where groupId=?";
		List<MapGroup> groupList = mapGroupManager.find(hql, groupId);
		return groupList;
	}

	/**
	 * 获取上次考勤打卡记录的手机类型
	 * @param userId
	 * @return
	 */
	public String getPhoneName(Long userId) {
		String sql = "SELECT PHONE_NAME FROM map_info WHERE USER_ID = ? AND PHONE_NAME IS NOT NULL ORDER BY CURRENT_TIMES DESC LIMIT 0,1 ";
		try {
			String strReturn = jdbcTemplate.queryForObject(sql, String.class, userId);
			return strReturn;
		} catch (Exception e) {
			return null;
		}
		
	}
	/**
	 *  获取某人手机名称是否存在
	 * @param userId
	 * @param phoneId
	 * @return
	 */
	public int getPhoneNameEmpty(Long userId, String phoneId){
		String hql = "from MapPhone where phoneId = ? AND userId <> ?";
		List<MapPhone> mapPhoneList = mapPhoneManager.find(hql,phoneId,userId);
		if(mapPhoneList.isEmpty()){
			return 0;
		}else{
			return 1;
		}
		 
	}
	/**
	 *  获取手机名称是否存在
	 * @param userId
	 * @param phoneId
	 * @return
	 */
	public List<MapPhone> getMapPhoneByPhoneId(String phoneId){
		String hql = "from MapPhone where phoneId = ? ";
		List<MapPhone> mapPhoneList = mapPhoneManager.find(hql,phoneId);
		return mapPhoneList;
		 
	}
	/**
	 * 查询替别人打卡的ID
	 * @param phoneId
	 * @return
	 */
	public List<MapPhone> getMapPhoneList(String phoneId){
		String hql = "from MapPhone where phoneId = ? ";
		List<MapPhone> mapPhoneList = mapPhoneManager.find(hql,phoneId);
		return mapPhoneList;
	}
	/**
	 * 保存手机信息
	 * @param phoneId
	 * @param phoneName
	 * @param userId
	 */
	public void  saveMapPhone(String phoneId, String phoneName ,Long userId){
		MapPhone mapPhone = new MapPhone();
		mapPhone.setPhoneId(phoneId);
		mapPhone.setPhoneName(phoneName);
		mapPhone.setUserId(userId);
		mapPhoneManager.save(mapPhone);
	}
	/**
	 * 保存异常打卡信息
	 * @param userId
	 * @param currentTime
	 * @param status
	 * @param phoneId
	 * @param exId
	 */
	public void saveMapException(MapException mapException ){
		mapExceptionManager.save(mapException);
	}

	/**
	 * 删除当天下午异常库里的信息  清理垃圾数据
	 * @param currentTimes
	 * @param userId
	 */
	public void deleteMapExceptionByNow(String currentTimes, String userId){
		String sql = "DELETE FROM map_exception WHERE  USER_ID = ? AND CURRENT_TIMES = ? AND PHONE_STATUS=?";
		jdbcTemplate.update(sql, userId,currentTimes,2);
	}
	
	/**
	 * 根据异常ID获取异常信息
	 * @param id
	 * @return
	 */
	public MapException getMapExceptionBy(Long id){
		String hql = "from MapException where id = ?";
		List<MapException> mapExceptionList = mapExceptionManager.find(hql, id);
		MapException mapExceptionRet = new MapException();
		for (MapException mapException : mapExceptionList) {
			mapExceptionRet.setPhoneStatus(mapException.getPhoneStatus());
			mapExceptionRet.setExId(mapException.getExId());
			mapExceptionRet.setPhoneName(mapException.getPhoneName());
			mapExceptionRet.setPhotoPath(mapException.getPhotoPath());
			mapExceptionRet.setId(id);
		}
		return mapExceptionRet;
	}
	/**
	 * 获取部门Id
	 * @param userId
	 * @return
	 */
	public String getDepartmentId(String userId){
		String sql = "SELECT DEPARTMENT_NAME FROM person_info WHERE code=?";
		try {
			String department = jdbcTemplate.queryForObject(sql, String.class, userId);
			return department;
		} catch (Exception e) {
			return null;
		}
		
		
	}
	/**
	 * 根据ID获取用户名
	 * @param userId
	 * @return
	 */
	public String getUserName(Long userId) {
		String sql = "SELECT DISPLAY_NAME FROM account_info WHERE ID = ?";
		String strReturn = jdbcTemplate.queryForObject(sql, String.class, userId);
		return strReturn;
	}

	/**
	 * 获取一人当天打卡记录
	 * 
	 * @param userId
	 * @return
	 */
	public List<MapInfo> getTodayMapInfo(Long userId) {

		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = sft.format(new Date());
		String hql = "FROM MapInfo WHERE  userId= ? AND currentTime =? ";
		Date nowdate = null;
		try {
			nowdate = sft.parse(currentTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<MapInfo> infoList = mapInfoManager.find(hql, userId, nowdate);
		return infoList;
	}

	/**
	 * 获取一人指定日期打卡记录
	 * 
	 * @param userId
	 * @return
	 */
	public List<MapInfo> getTodayMapInfoByDay(Long userId, Date date) {

		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = sft.format(new Date());
		String hql = "FROM MapInfo WHERE  userId= ? AND currentTime =? ";
		List<MapInfo> infoList = mapInfoManager.find(hql, userId, date);
		return infoList;
	}

	/**
	 * 获取今天是不是请假
	 * 
	 * @param ID
	 * @return
	 */
	public Map<String, Object> getLeaveDate(Long ID) throws Exception {
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		String time = sft.format(new Date());
		String sql = "SELECT start_date ,end_date FROM employee_leave WHERE ? >= DATE_FORMAT(start_date,'%Y-%m-%d') AND ? <= DATE_FORMAT(end_date,'%Y-%m-%d') AND user_id= ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, time, time, ID);
		Map<String, Object> mapRenturn = new HashMap<String, Object>();
		Date startDateTime = null;
		Date endDateTime = null;
		for (Map<String, Object> map : list) {
			if (startDateTime == null && endDateTime == null) {
				startDateTime = (Date) map.get("start_date");
				endDateTime = (Date) map.get("end_date");
			} else {
				if (startDateTime.after((Date) map.get("start_date"))) {
					startDateTime = (Date) map.get("start_date");
				}
				if (endDateTime.before((Date) map.get("end_date"))) {
					endDateTime = (Date) map.get("end_date");
				}
			}
		}
		if (startDateTime == null && endDateTime == null) {
			return mapRenturn;
		} else {
			mapRenturn.put("startDate", startDateTime);
			mapRenturn.put("endDate", endDateTime);
			return mapRenturn;
		}
	}

	/**
	 * 获取指定日期是不是请假
	 * 
	 * @param ID
	 * @return
	 */
	public Map<String, Object> getLeaveDateByDay(Long ID, Date time) throws Exception {
		String sql = "SELECT start_date ,end_date FROM employee_leave WHERE ? >= DATE_FORMAT(start_date,'%Y-%m-%d') AND ? <= DATE_FORMAT(end_date,'%Y-%m-%d') AND user_id= ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, time, time, ID);
		Map<String, Object> mapRenturn = new HashMap<String, Object>();
		Date startDateTime = null;
		Date endDateTime = null;
		for (Map<String, Object> map : list) {
			if (startDateTime == null && endDateTime == null) {
				startDateTime = (Date) map.get("start_date");
				endDateTime = (Date) map.get("end_date");
			} else {
				if (startDateTime.after((Date) map.get("start_date"))) {
					startDateTime = (Date) map.get("start_date");
				}
				if (endDateTime.before((Date) map.get("end_date"))) {
					endDateTime = (Date) map.get("end_date");
				}
			}
		}
		if (startDateTime == null && endDateTime == null) {
			return mapRenturn;
		} else {
			mapRenturn.put("startDate", startDateTime);
			mapRenturn.put("endDate", endDateTime);
			return mapRenturn;
		}
	}
	
	
	/**
	 * 获取指定日期是不是请假
	 * 
	 * @param ID
	 * @return
	 */
	public String getLeaveByDay(Long userId, Date time) throws Exception {
		String sql = "select human_Task_Id from employee_leave Where user_id =? and ? >= DATE_FORMAT(start_date,'%Y-%m-%d') AND ? <= DATE_FORMAT(end_date,'%Y-%m-%d')";
		try {
			String returnStr  = jdbcTemplate.queryForObject(sql,String.class,userId,time,time);
			return returnStr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	/**
	 * 获取某人全部请假的日期
	 * 
	 * @param userId
	 * @return
	 */
	public List<EmployeeLeaveInfo> getLeaveInfo(Long userId) {
		String hql = "from EmployeeLeaveInfo where user_id = ? ";
		List<EmployeeLeaveInfo> EmployeeLeaveInfoList = employeeLeaveInfoManager.find(hql, userId);
		return EmployeeLeaveInfoList;

	}

	/**
	 * 获取出差时间
	 * 
	 * @param ID
	 * @return
	 */
	public Map<String, Object> getOutDate(Long ID) throws Exception {

		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		String time = sft.format(new Date());
		String sql = "SELECT start_date ,end_date FROM employee_out WHERE ? >= DATE_FORMAT(start_date,'%Y-%m-%d') AND ? <= DATE_FORMAT(end_date,'%Y-%m-%d') AND user_id= ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, time, time, ID);
		Map<String, Object> mapRenturn = new HashMap<String, Object>();
		Date startDateTime = null;
		Date endDateTime = null;
		for (Map<String, Object> map : list) {
			if (startDateTime == null && endDateTime == null) {
				startDateTime = (Date) map.get("start_date");
				endDateTime = (Date) map.get("end_date");
			} else {
				if (startDateTime.after((Date) map.get("start_date"))) {
					startDateTime = (Date) map.get("start_date");
				}
				if (endDateTime.before((Date) map.get("end_date"))) {
					endDateTime = (Date) map.get("start_date");
				}
			}
		}
		if (startDateTime == null && endDateTime == null) {
			return mapRenturn;
		} else {
			mapRenturn.put("startDate", startDateTime);
			mapRenturn.put("endDate", endDateTime);
			return mapRenturn;
		}

	}

	/**
	 * 获取指定出差时间
	 * 
	 * @param ID
	 * @return
	 */
	public Map<String, Object> getOutDateByDay(Long ID, Date time) throws Exception {

		String sql = "SELECT start_date ,end_date FROM employee_out WHERE ? >= DATE_FORMAT(start_date,'%Y-%m-%d') AND ? <= DATE_FORMAT(end_date,'%Y-%m-%d') AND user_id= ?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, time, time, ID);
		Map<String, Object> mapRenturn = new HashMap<String, Object>();
		Date startDateTime = null;
		Date endDateTime = null;
		for (Map<String, Object> map : list) {
			if (startDateTime == null && endDateTime == null) {
				startDateTime = (Date) map.get("start_date");
				endDateTime = (Date) map.get("end_date");
			} else {
				if (startDateTime.after((Date) map.get("start_date"))) {
					startDateTime = (Date) map.get("start_date");
				}
				if (endDateTime.before((Date) map.get("end_date"))) {
					endDateTime = (Date) map.get("start_date");
				}
			}
		}
		if (startDateTime == null && endDateTime == null) {
			return mapRenturn;
		} else {
			mapRenturn.put("startDate", startDateTime);
			mapRenturn.put("endDate", endDateTime);
			return mapRenturn;
		}

	}
	/**
	 * 获取指定日期是不是出差
	 * 
	 * @param ID
	 * @return
	 */
	public String getOutByDay(Long userId, Date time) throws Exception {
		String sql = "select human_Task_Id from employee_out Where user_id =? and ? >= DATE_FORMAT(start_date,'%Y-%m-%d') AND ? <= DATE_FORMAT(end_date,'%Y-%m-%d')";
		try {
			String returnStr  = jdbcTemplate.queryForObject(sql,String.class,userId,time,time);
			return returnStr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	/**
	 * 获取某人全部出差的日期
	 * 
	 * @param userId
	 * @return
	 */
	public List<EmployeeOutInfo> getOutInfo(Long userId) {
		String hql = "from EmployeeOutInfo where id = ? ";
		List<EmployeeOutInfo> employeeOutInfoList = employeeOutInfoManager.find(hql, userId);
		return employeeOutInfoList;

	}

	/**
	 * 获取一人指定月的打卡记录
	 * 
	 * @param userId
	 * @param nowMonth
	 * @param nextMonth
	 * @return
	 */
	public List<MapInfo> getMonthMapInfo(Long userId, Date nowMonth, Date nextMonth) {
		String hql = "FROM MapInfo WHERE userId = ? AND currentTime >= ? AND currentTime<= ?";
		List<MapInfo> infoList = mapInfoManager.find(hql, userId, nowMonth, nextMonth);
		return infoList;
	}

	
	public List<OrgDepartment> getAllDepartment(){
		List<OrgDepartment> departmentList =orgDepartmentManager.getAll();
		return departmentList;
	}
	/**
	 * 查询所有考勤组
	 *
	 * @return
	 */
	public List<MapGroup> getALLMapGroup() {
		List<MapGroup> groupList = mapGroupManager.getAll();
		return groupList;
	}

	/**
	 * 查询指定ID的考勤组
	 *
	 * @return
	 */
	public List<MapGroup> getMapGroupById(Long groupId) {
		String hql = "FROM MapGroup WHERE groupId = ?";
		List<MapGroup> groupList = mapGroupManager.find(hql, groupId);
		return groupList;
	}

	/**
	 * 根据ID 获取打卡数据
	 * 
	 * @param id
	 * @return
	 */
	public List<MapInfo> getMapInfoById(Long id) {
		String hql = "from MapInfo where id = ? ";
		List<MapInfo> getMapInfoById = mapInfoManager.find(hql, id);
		return getMapInfoById;
	}
	/**
	 * 保存上班打卡记录
	 * @param mapInfo
	 */
	public void saveMapInfo(MapInfo mapInfo){
		mapInfoManager.save(mapInfo);
	}
	/**
	 *  修改打卡数据
	 * 
	 * @param mapInfo
	 */
	public void updateMapInfoById(MapInfo mapInfo) {
		String sql = "UPDATE map_info SET START_WORK_TIME=?,START_Site=?,START_TIME=?,START_OUT=?,START_OUT_REASON=?,START_STATUS=?,"
				+ "END_WORK_TIME=?,END_SITE=?,END_TIME=?,END_OUT=?,END_OUT_REASON=?,END_STATUS=?,ALL_TIME=?,OVER_WORK_TIME=?,CHOOSE=? WHERE ID =?";
		jdbcTemplate.update(sql, mapInfo.getStartWorkTime(),mapInfo.getStartSite(), mapInfo.getStartTime(), mapInfo.getStartOut(),
				mapInfo.getStartOutReason(), mapInfo.getStartStatus(), mapInfo.getEndWorkTime(), mapInfo.getEndSite(),mapInfo.getEndTime(),
				mapInfo.getEndOut(), mapInfo.getEndOutReason(),mapInfo.getEndStatus(),mapInfo.getAllTime(),mapInfo.getOverWorkTime(),mapInfo.getChoose(),mapInfo.getId());
	}
	
	/**
	 * 根据考勤组 修改数据
	 * 
	 * @param mapGroup
	 */
	public void MapGroupUpdate(MapGroup mapGroup) {
		String sql = "UPDATE map_group SET GROUP_NAME=?,GROUP_CENTER=?,CENTER_SITE=?,RADIUS=?,GTOUP_WEEK=?,START_TIME=?,END_TIME=?,OVER_TIME=?,ALERT_TIME=? WHERE GROUP_ID=?";
		jdbcTemplate.update(sql,mapGroup.getGroupName(),mapGroup.getGroupConter(),mapGroup.getCenterSite(),mapGroup.getRadius(),mapGroup.getGroupWeek(),mapGroup.getStartTime(),mapGroup.getEndTime(),mapGroup.getOverTime(),mapGroup.getAlertTime(),mapGroup.getGroupId());
	}

	
	/**
	 * 添加新的考勤组
	 * 
	 * @param mapGroup
	 */
	public void MapGroupsave(MapGroup mapGroup) {

		mapGroupManager.save(mapGroup);
	}

	/**
	 * 根据groupId删除考勤组
	 *
	 * @param groupId
	 */
	public void MapGroupdelete(Long groupId) {

		String  sql = "delete from map_group where group_id = ?";
		jdbcTemplate.update(sql,groupId);
	}

	/**
	 * 根据ID 获取打卡数据
	 * 
	 * @param id
	 * @return
	 */
	public List<MapHolidays> getMapHolidaysById(Long id) {
		String hql = "from MapHolidays where id = ? ";
		List<MapHolidays> getMapHolidaysById = mapHolidaysManager.find(hql, id);
		return getMapHolidaysById;
	}

	/**
	 * 添加法定日历
	 * 
	 * @param mapHolidays
	 */
	public void saveMapHolidays(MapHolidays mapHolidays) {

		mapHolidaysManager.save(mapHolidays);
	}

	/**
	 * 根据ID删除法定日历
	 * 
	 * @param mapId
	 */
	public void deleteMapHolidays(Long mapId) {
		mapHolidaysManager.removeById(mapId);
	}

	/**
	 * 改变法定日历
	 * 
	 * @param mapHolidays
	 */
	public void updateMapHolidays(MapHolidays mapHolidays) {
		mapHolidaysManager.update(mapHolidays);
	}

	/**
	 * 查询区间段内法定日历
	 * 
	 * @return
	 */
	public List<MapHolidays> getMapHolidays(Date startTime, Date endTime) {
		String hql = "from MapHolidays where mapTime >= ? and mapTime < ?";
		List<MapHolidays> holidaysList = mapHolidaysManager.find(hql, startTime, endTime);

		return holidaysList;
	}

	/**
	 * 获取指定日期是否为法定假日
	 * 
	 * @param mapTime
	 * @return 空或者数据
	 */
	public List<MapHolidays> getNowMapHoliDays(Date mapTime) {
		SimpleDateFormat sft = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = sft.format(mapTime);
		Date nowdate = null;
		try {
			nowdate = sft.parse(currentTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		String hql = "from MapHolidays where mapTime = ?";
		List<MapHolidays> holidaysList = mapHolidaysManager.find(hql, nowdate);
		return holidaysList;

	}

	/**
	 * 根据分组ID查数据
	 * 
	 * @param groupId
	 * @return
	 */
	public List<MapUser> getMapUserInfo(Long groupId) {
		MapGroup mapGroup = new  MapGroup();
		mapGroup.setGroupId(groupId);
		String hql = "from MapUser where userGroup = ? ";
		List<MapUser> MapUserList = mapUserManager.find(hql, mapGroup);

		return MapUserList;

	}
	/**
	 * 根据分组ID查数据
	 * 
	 * @param groupId
	 * @return
	 */
	public List<MapUser> getOtherMapUserInfo(Long groupId) {
		MapGroup mapGroup = new  MapGroup();
		mapGroup.setGroupId(groupId);
		String hql = "from MapUser where userGroup != ? ";
		List<MapUser> MapUserList = mapUserManager.find(hql, mapGroup);

		return MapUserList;

	}
	
	public List<MapUser> getAllMapUserInfo() {
		String hql = "from MapUser";
		List<MapUser> MapUserList = mapUserManager.find(hql);

		return MapUserList;

	}

	/**
	 * 根据获取的用户ID查询对应分组的数据
	 * 
	 * @param groupId
	 * @return
	 */

	public List<MapUser> getMapUserById(Long userId) {

		String hql = "from MapUser where userId=?";

		List<MapUser> groupList = mapUserManager.find(hql, userId);
		return groupList;
	}

	/**
	 * 根据时间获取有没有数据
	 * 
	 * @param groupId
	 * @return
	 */

	public List<MapHolidays> getMapHolidaysByTime(Date mapTime) {

		String hql = "from MapHolidays where mapTime=?";

		List<MapHolidays> MapHolidaysList = mapHolidaysManager.find(hql, mapTime);
		return MapHolidaysList;
	}

	/**
	 * 根据Id删除所有数据
	 * 
	 * @param groupId
	 */
	public void deleteAllMapUser(String userIdArr) {
		String sql = "DELETE FROM map_user WHERE USER_ID IN (" + userIdArr + ")";
		jdbcTemplate.update(sql);
	}
	
	/**
	 * 根据Id删除所有数据
	 * 
	 * @param groupId
	 */
	public void deleteAllMapUserByGroupId(Long groupId) {
		String sql = "DELETE FROM map_user WHERE USER_GROUP = ?";
		jdbcTemplate.update(sql,groupId);
	}

	/**
	 * ] 添加数据到mapUser表 用时看下此方法 value值为拼接的值
	 * 
	 * @param value
	 */
	public void insertMapUser(String value) {
		String sql = "INSERT INTO map_user(USER_ID,USER_GROUP) VALUE " + value;
		jdbcTemplate.update(sql);
	}
	
	
	
	
	@Resource
	public void setMapGroupManager(MapGroupManager mapGroupManager) {
		this.mapGroupManager = mapGroupManager;
	}
	@Resource
	public void setMapUserManager(MapUserManager mapUserManager) {
		this.mapUserManager = mapUserManager;
	}
	@Resource
	public void setMapInfoManager(MapInfoManager mapInfoManager) {
		this.mapInfoManager = mapInfoManager;
	}
	@Resource
	public void setMapHolidaysManager(MapHolidaysManager mapHolidaysManager) {
		this.mapHolidaysManager = mapHolidaysManager;
	}
	@Resource
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	@Resource
	public void setEmployeeLeaveInfoManager(EmployeeLeaveInfoManager employeeLeaveInfoManager) {
		this.employeeLeaveInfoManager = employeeLeaveInfoManager;
	}
	@Resource
	public void setEmployeeOutInfoManager(EmployeeOutInfoManager employeeOutInfoManager) {
		this.employeeOutInfoManager = employeeOutInfoManager;
	}
	@Resource
	public void setAccountInfoManager(AccountInfoManager accountInfoManager) {
		this.accountInfoManager = accountInfoManager;
	}
	@Resource
	public void setPersonInfoManager(PersonInfoManager personInfoManager) {
		this.personInfoManager = personInfoManager;
	}
	@Resource
	public void setMapPhoneManager(MapPhoneManager mapPhoneManager) {
		this.mapPhoneManager = mapPhoneManager;
	}
	@Resource
	public void setMapExceptionManager(MapExceptionManager mapExceptionManager) {
		this.mapExceptionManager = mapExceptionManager;
	}
	@Resource
	public void setOrgDepartmentManager(OrgDepartmentManager orgDepartmentManager) {
		this.orgDepartmentManager = orgDepartmentManager;
	}
	

}
