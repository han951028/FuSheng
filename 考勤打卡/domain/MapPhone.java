package com.mossle.domap.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="map_phone")
public class MapPhone implements java.io.Serializable{

	private Long id ;
	//用户ID
	private Long userId;
	//手机名称
	private String phoneName;
	//手机唯一标识
	private String phoneId;
	
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
	@Column(name = "PHONE_NAME")
	public String getPhoneName() {
		return phoneName;
	}
	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}
	@Column(name = "PHONE_ID")
	public String getPhoneId() {
		return phoneId;
	}
	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}
	public MapPhone(){
		
	}
	public MapPhone(Long id, Long userId, String phoneName, String phoneId) {
		super();
		this.id = id;
		this.userId = userId;
		this.phoneName = phoneName;
		this.phoneId = phoneId;
	}
	
	
	
	
}
