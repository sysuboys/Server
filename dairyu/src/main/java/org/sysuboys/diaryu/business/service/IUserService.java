package org.sysuboys.diaryu.business.service;

import org.sysuboys.diaryu.business.entity.User;

public interface IUserService {

	boolean exist(String username);

	void create(User user);

	User findByUsername(String username);

}
