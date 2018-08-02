package com.mossle.domap.Util;

public class MapDataDto {

	//ID
	private Long id;
	// 用户名称
	private String userName;
	// 打卡时间
	private String currentTime;
	// 考勤组名称
	private String groupName;
	// 所在部门
	private String departmentName;
	// 规定上班时间
	private String startTime;
	// 规定下班时间
	private String endTime;
	// 签到时间
	private String startWorkTime;
	// 签退时间
	private String endWorkTime;
	// 上班打卡地点
	private String startSite;
	// 下班打卡地点
	private String endSite;
	// 上班外勤 0本地 1外勤
	private String startOut;
	// 上班外勤理由
	private String startOutReason;
	// 下班外勤 0本地 1外勤；
	private String endOut;
	// 下班外勤理由
	private String endOutReason;
	// 当天一共上班时间
	private String allTime;
	// 加班时间
	private String overWorkTime;
	// 状态
	private String choose;
	// 签到状态
	private String startChoose;
	// 签退状态
	private String endChoose;

	



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartWorkTime() {
		return startWorkTime;
	}

	public void setStartWorkTime(String startWorkTime) {
		this.startWorkTime = startWorkTime;
	}

	public String getEndWorkTime() {
		return endWorkTime;
	}

	public void setEndWorkTime(String endWorkTime) {
		this.endWorkTime = endWorkTime;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getStartSite() {
		return startSite;
	}

	public void setStartSite(String startSite) {
		this.startSite = startSite;
	}

	public String getEndSite() {
		return endSite;
	}

	public void setEndSite(String endSite) {
		this.endSite = endSite;
	}

	public String getStartOut() {
		return startOut;
	}

	public void setStartOut(String startOut) {
		this.startOut = startOut;
	}

	public String getStartOutReason() {
		return startOutReason;
	}

	public void setStartOutReason(String startOutReason) {
		this.startOutReason = startOutReason;
	}

	public String getEndOut() {
		return endOut;
	}

	public void setEndOut(String endOut) {
		this.endOut = endOut;
	}

	public String getEndOutReason() {
		return endOutReason;
	}

	public void setEndOutReason(String endOutReason) {
		this.endOutReason = endOutReason;
	}

	public String getAllTime() {
		return allTime;
	}

	public void setAllTime(String allTime) {
		this.allTime = allTime;
	}

	public String getChoose() {
		return choose;
	}

	public void setChoose(String choose) {
		this.choose = choose;
	}

	
	public String getOverWorkTime() {
		return overWorkTime;
	}

	public void setOverWorkTime(String overWorkTime) {
		this.overWorkTime = overWorkTime;
	}

	
	public String getStartChoose() {
		return startChoose;
	}

	public void setStartChoose(String startChoose) {
		this.startChoose = startChoose;
	}

	public String getEndChoose() {
		return endChoose;
	}

	public void setEndChoose(String endChoose) {
		this.endChoose = endChoose;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public MapDataDto() {
	
	}

	public MapDataDto(Long id, String userName, String currentTime, String groupName, String departmentName,
			String startTime, String endTime, String startWorkTime, String endWorkTime, String startSite,
			String endSite, String startOut, String startOutReason, String endOut, String endOutReason, String allTime,
			String overWorkTime, String choose, String startChoose, String endChoose) {
		super();
		this.id = id;
		this.userName = userName;
		this.currentTime = currentTime;
		this.groupName = groupName;
		this.departmentName = departmentName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.startWorkTime = startWorkTime;
		this.endWorkTime = endWorkTime;
		this.startSite = startSite;
		this.endSite = endSite;
		this.startOut = startOut;
		this.startOutReason = startOutReason;
		this.endOut = endOut;
		this.endOutReason = endOutReason;
		this.allTime = allTime;
		this.overWorkTime = overWorkTime;
		this.choose = choose;
		this.startChoose = startChoose;
		this.endChoose = endChoose;
	}


	





}
