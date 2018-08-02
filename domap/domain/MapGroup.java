package com.mossle.domap.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 * 
 * @author Hanyang
 *
 */
@Entity
@Table(name="MAP_GROUP")
public class MapGroup implements java.io.Serializable{

	
	private static final long serialVersionUID = 0L;
	//分组ID
	private Long groupId;
	//分组名称
	private String groupName;
	//围栏中心点坐标
	private String groupConter;
	//中心点地址名称
	private String centerSite;
	//围栏半径
	private int radius;
	//考勤日期
	private String groupWeek;
	//上班时间
	private String startTime;
	//下班时间
	private String endTime;
	//加班开始时间
	private String overTime;
	//提醒时间
	private String alertTime;
	
	private Set<MapUser> mapUsers =new HashSet<MapUser>();
	
	@OneToMany(fetch=FetchType.LAZY)
	public Set<MapUser> getMapUsers() {
		return mapUsers;
	}
	public void setMapUsers(Set<MapUser> mapUsers) {
		this.mapUsers = mapUsers;
	}
	
	
	@Id
	@Column(name = "GROUP_ID", unique = true, nullable = false)
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	@Column(name = "GROUP_NAME")
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Column(name = "GROUP_CENTER")
	public String getGroupConter() {
		return groupConter;
	}
	public void setGroupConter(String groupConter) {
		this.groupConter = groupConter;
	}

	@Column(name = "CENTER_SITE")
	public String getCenterSite() {
		return centerSite;
	}
	public void setCenterSite(String centerSite) {
		this.centerSite = centerSite;
	}
	
	@Column(name = "RADIUS")
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	@Column(name = "GTOUP_WEEK")
	public String getGroupWeek() {
		return groupWeek;
	}
	public void setGroupWeek(String groupWeek) {
		this.groupWeek = groupWeek;
	}
	
	@Column(name = "START_TIME")
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	@Column(name = "END_TIME")
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	@Column(name = "OVER_TIME")
	public String getOverTime() {
		return overTime;
	}
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
	
	@Column(name = "ALERT_TIME")
	public String getAlertTime() {
		return alertTime;
	}
	public void setAlertTime(String alertTime) {
		this.alertTime = alertTime;
	}
	public MapGroup(){
		
	}
	public MapGroup(Long groupId, String groupName, String groupConter, String centerSite, int radius, String groupWeek,
			String startTime, String endTime, String overTime, String alertTime) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupConter = groupConter;
		this.centerSite = centerSite;
		this.radius = radius;
		this.groupWeek = groupWeek;
		this.startTime = startTime;
		this.endTime = endTime;
		this.overTime = overTime;
		this.alertTime = alertTime;
	}



	
	
	
	
	
	
	
}
