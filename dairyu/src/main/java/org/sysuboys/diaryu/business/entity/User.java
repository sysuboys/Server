package org.sysuboys.diaryu.business.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.sysuboys.diaryu.util.SecurityUtil;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String username;
	private String password;
	private String salt;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Diary> diarys;

	public User() {
		super();
	}

	public User(String username, String clearPassword) {
		super();
		this.username = username;
		this.salt = SecurityUtil.generate32();
		Md5Hash hash = new Md5Hash(clearPassword, this.salt);
		this.password = hash.toString();
	}
	
	// 明文
	public boolean checkPassword(String clearPassword) {
		Md5Hash hash = new Md5Hash(clearPassword, this.salt);
		return this.password.equals(hash.toString());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public List<Diary> getDiarys() {
		return diarys;
	}

	public void setDiarys(List<Diary> diarys) {
		this.diarys = diarys;
	}

}
