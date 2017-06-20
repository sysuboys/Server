package org.sysuboys.diaryu.business.service;

public interface LoginService {

	String login(String username, String password);
	
	String getUsername(String sessionid);
	
	String logout(String sessionid);

}
