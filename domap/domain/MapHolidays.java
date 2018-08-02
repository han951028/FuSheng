package com.mossle.domap.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author Hanyang
 *
 */
@Entity
@Table(name="MAP_HOLIDAYS")
public class MapHolidays implements java.io.Serializable{
	
	
	private static final long serialVersionUID = 0L;
	//Id
	private Long mapId;
	//日期
	private Date mapTime;
	//是否上班
	private int status;
	//备注
	private String mapNotes;
	
	
	@Id
	@Column(name = "MAP_ID", unique = true, nullable = false)
	public Long getMapId() {
		return mapId;
	}
	public void setMapId(Long mapId) {
		this.mapId = mapId;
	}
	
	@Column(name = "STATUS")
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "MAP_TIME")
	public Date getMapTime() {
		return mapTime;
	}
	public void setMapTime(Date mapTime) {
		this.mapTime = mapTime;
	}
	
	@Column(name = "MAP_NOTES" ,length=500)
	public String getMapNotes() {
		return mapNotes;
	}
	public void setMapNotes(String mapNotes) {
		this.mapNotes = mapNotes;
	}
	public MapHolidays(){
		
	}
	public MapHolidays(Long mapId, Date mapTime, int status, String mapNotes) {
		super();
		this.mapId = mapId;
		this.mapTime = mapTime;
		this.status = status;
		this.mapNotes = mapNotes;
	}



	
	
	
	
	
	
	
	}
