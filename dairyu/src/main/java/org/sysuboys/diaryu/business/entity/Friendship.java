package org.sysuboys.diaryu.business.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "friendships")
public class Friendship implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long uid1;
	private Long uid2;
	
	public Friendship() {
		super();
	}
	
	public Friendship(Long uid1, Long uid2) {
		this.uid1 = uid1;
		this.uid2 = uid2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid1() {
		return uid1;
	}

	public void setUid1(Long uid1) {
		this.uid1 = uid1;
	}

	public Long getUid2() {
		return uid2;
	}

	public void setUid2(Long uid2) {
		this.uid2 = uid2;
	}

}
