package com.mossle.domap.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Hanyang
 *
 */
@Entity
@Table(name = "MAP_USER")
public class MapUser implements java.io.Serializable {

	private static final long serialVersionUID = 0L;
	// ID
	private Long id;
	// 用户ID
	private Long userId;
	// 用户分组
	private MapGroup userGroup;

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


	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "USER_GROUP")
	public MapGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(MapGroup userGroup) {
		this.userGroup = userGroup;
	}

	public MapUser() {

	}

	public MapUser(Long id, Long userId, MapGroup userGroup) {
		super();
		this.id = id;
		this.userId = userId;
		this.userGroup = userGroup;
	}



}
