package org.sysuboys.diaryu.business.dao;

import org.sysuboys.diaryu.business.entity.User;

public interface IUserDao {

	void create(User user);

	User update(User user);

	void delete(long userId);

	User findOne(long userId);

	User findByUsername(String username);

}
