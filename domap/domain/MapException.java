package com.mossle.domap.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="MAP_EXCEPTION")
public class MapException implements java.io.Serializable{

	private Long id ;
	//用户ID
	private Long userId;
	//时间
	private Date currentTime;
	//状态 0上午 1下午
	private int phoneStatus;
	//手机ID
	private String phoneId;
	//手机名称
	private String phoneName;
	//帮助用户打卡的人
	private Long exId;
	//照片路径
	private String photoPath;
	
	private Set<MapInfo> MapInfos = new HashSet<MapInfo>();
	

	@OneToMany(fetch = FetchType.LAZY)
	public Set<MapInfo> getMapInfos() {
		return MapInfos;
	}
	public void setMapInfos(Set<MapInfo> mapInfos) {
		MapInfos = mapInfos;
	}
	
	
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
	
	@Column(name = "CURRENT_TIMES")
	public Date getCurrentTime() {
		return currentTime;
	}
	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}
	
	@Column(name = "PHONE_STATUS")
	public int getPhoneStatus() {
		return phoneStatus;
	}
	public void setPhoneStatus(int phoneStatus) {
		this.phoneStatus = phoneStatus;
	}
	
	@Column(name = "PHONE_ID")
	public String getPhoneId() {
		return phoneId;
	}
	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}

	@Column(name = "EX_ID")
	public Long getExId() {
		return exId;
	}
	public void setExId(Long exId) {
		this.exId = exId;
	}
	
	@Column(name="PHOTO_PATH")
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	
	
	@Column(name="PHONE_NAME")
	public String getPhoneName() {
		return phoneName;
	}

	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}

	public MapException(){
		
	}
	
	public MapException(Long id, Long userId, Date currentTime, int phoneStatus, String phoneId, String phoneName,
			Long exId, String photoPath, Set<MapInfo> mapInfos) {
		super();
		this.id = id;
		this.userId = userId;
		this.currentTime = currentTime;
		this.phoneStatus = phoneStatus;
		this.phoneId = phoneId;
		this.phoneName = phoneName;
		this.exId = exId;
		this.photoPath = photoPath;
		MapInfos = mapInfos;
	}






	

	
}
