package org.sysuboys.diaryu.business.service;

public interface ILoginService {

	String login(String username, String password);
	
	String getUsername(String sessionid);
	
	void logout(String sessionid);

}
