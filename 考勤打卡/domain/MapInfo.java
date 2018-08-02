package com.mossle.domap.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * 
 * @author Hanyang
 *
 */
@Entity
@Table(name = "MAP_INFO")
public class MapInfo implements java.io.Serializable {

	private Long id;
	// 用户id
	private Long userId;
	//用户名
	private String userName ;
	//用户部门ID
	private String departmentId;
	// 打卡时间  因current_time 为数据库关键字  所以数据库命名为current_times
	private Date currentTime;
	// 上班时间
	private String startWorkTime;
	// 规定上班时间
	private String startTime;
	// 上班打卡地点
	private String startSite;
	// 上班外勤 0本地 1外勤
	private int startOut;
	// 上班外勤理由
	private String startOutReason;
	// 上班状态 0缺卡 1正常 2迟到
	private int startStatus;
	// 上班照片
	private MapException startPhoto;
	// 下班时间
	private String endWorkTime;
	// 规定下班时间
	private String endTime;
	// 下班打卡地点
	private String endSite;
	// 下班外勤 0本地 1外勤；
	private int endOut;
	// 下班外勤理由
	private String endOutReason;
	// 下班状态 0缺卡 1正常 2早退
	private int endStatus;
	// 下班照片
	private MapException endPhoto;
	// 当天上班时间
	private String allTime;
	// 加班开始时间
	private String overTime;
	// 加班时间
	private String overWorkTime;
	// 手机类型
	private String phoneId;
	// 分组名称
	private String groupName;
	//0上班1请假(半)2旷工3事假4出差5加班6休息
	private int choose;
	
	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@Column(name = "DEPARTMENT_ID")
	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	@Column(name = "CURRENT_TIMES")
	public Date getCurrentTime() {
		return currentTime;
	}

	
	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}

	@Column(name = "START_WORK_TIME",length=50)
	public String getStartWorkTime() {
		return startWorkTime;
	}

	public void setStartWorkTime(String startWorkTime) {
		this.startWorkTime = startWorkTime;
	}

	@Column(name = "START_TIME",length=50)
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "START_SITE",length=150)
	public String getStartSite() {
		return startSite;
	}

	public void setStartSite(String startSite) {
		this.startSite = startSite;
	}

	@Column(name = "START_OUT")
	public int getStartOut() {
		return startOut;
	}

	public void setStartOut(int startOut) {
		this.startOut = startOut;
	}

	@Column(name = "START_OUT_REASON",length=500)
	public String getStartOutReason() {
		return startOutReason;
	}

	public void setStartOutReason(String startOutReason) {
		this.startOutReason = startOutReason;
	}

	@Column(name = "START_STATUS")
	public int getStartStatus() {
		return startStatus;
	}

	public void setStartStatus(int startStatus) {
		this.startStatus = startStatus;
	}

	@Column(name = "END_WORK_TIME",length=50)
	public String getEndWorkTime() {
		return endWorkTime;
	}

	public void setEndWorkTime(String endWorkTime) {
		this.endWorkTime = endWorkTime;
	}

	@Column(name = "END_TIME",length=50)
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Column(name = "END_SITE",length=150)
	public String getEndSite() {
		return endSite;
	}

	public void setEndSite(String endSite) {
		this.endSite = endSite;
	}

	@Column(name = "END_OUT")
	public int getEndOut() {
		return endOut;
	}

	public void setEndOut(int endOut) {
		this.endOut = endOut;
	}

	@Column(name = "END_OUT_REASON",length=500)
	public String getEndOutReason() {
		return endOutReason;
	}

	public void setEndOutReason(String endOutReason) {
		this.endOutReason = endOutReason;
	}

	@Column(name = "END_STATUS")
	public int getEndStatus() {
		return endStatus;
	}

	public void setEndStatus(int endStatus) {
		this.endStatus = endStatus;
	}

	@Column(name = "OVER_TIME" ,length=50)
	public String getOverTime() {
		return overTime;
	}

	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}

	@Column(name = "CHOOSE")
	public int getChoose() {
		return choose;
	}

	public void setChoose(int choose) {
		this.choose = choose;
	}
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "START_PHOTO" )
	public MapException getStartPhoto() {
		return startPhoto;
	}

	public void setStartPhoto(MapException startPhoto) {
		this.startPhoto = startPhoto;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "END_PHOTO")
	public MapException getEndPhoto() {
		return endPhoto;
	}

	public void setEndPhoto(MapException endPhoto) {
		this.endPhoto = endPhoto;
	}

	@Column(name = "PHONE_ID" ,length=50)
	public String getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}

	@Column(name = "ALL_TIME",length=50)
	public String getAllTime() {
		return allTime;
	}

	public void setAllTime(String allTime) {
		this.allTime = allTime;
	}

	@Column(name = "GROUP_NAME",length=50)
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


	@Column(name = "USER_NAME" ,length=50)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	@Column(name = "OVER_WORK_TIME" ,length=50)
	public String getOverWorkTime() {
		return overWorkTime;
	}

	public void setOverWorkTime(String overWorkTime) {
		this.overWorkTime = overWorkTime;
	}

	public MapInfo() {

	}

	public MapInfo(Long id, Long userId, String userName, String departmentId, Date currentTime, String startWorkTime,
			String startTime, String startSite, int startOut, String startOutReason, int startStatus,
			MapException startPhoto, String endWorkTime, String endTime, String endSite, int endOut,
			String endOutReason, int endStatus, MapException endPhoto, String allTime, String overTime,
			String overWorkTime, String phoneId, String groupName, int choose) {
		super();
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.departmentId = departmentId;
		this.currentTime = currentTime;
		this.startWorkTime = startWorkTime;
		this.startTime = startTime;
		this.startSite = startSite;
		this.startOut = startOut;
		this.startOutReason = startOutReason;
		this.startStatus = startStatus;
		this.startPhoto = startPhoto;
		this.endWorkTime = endWorkTime;
		this.endTime = endTime;
		this.endSite = endSite;
		this.endOut = endOut;
		this.endOutReason = endOutReason;
		this.endStatus = endStatus;
		this.endPhoto = endPhoto;
		this.allTime = allTime;
		this.overTime = overTime;
		this.overWorkTime = overWorkTime;
		this.phoneId = phoneId;
		this.groupName = groupName;
		this.choose = choose;
	}






	
	




	

}
