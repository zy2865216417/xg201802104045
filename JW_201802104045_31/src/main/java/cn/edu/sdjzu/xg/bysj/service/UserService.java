package cn.edu.sdjzu.xg.bysj.service;


import cn.edu.sdjzu.xg.bysj.dao.UserDao;
import cn.edu.sdjzu.xg.bysj.domain.User;

import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();
	
	public UserService() {
	}
	
	public static UserService getInstance(){
		return UserService.userService;
	}

	public Collection<User> getUsers() throws SQLException,ClassNotFoundException{
		return userDao.findAll();
	}

	public User find(Integer id) throws SQLException,ClassNotFoundException{
		return userDao.find(id);
	}
	public User findByUsername(String username) throws SQLException,ClassNotFoundException{
		return userDao.findByUsername(username);
	}
	public boolean changePassword(String username,String newPassword) throws SQLException{
		return userDao.changePassword(username,newPassword);
	}
	public User getUser(Integer id) throws SQLException,ClassNotFoundException {
		return userDao.find(id);
	}
	
	public boolean updateUser(User user) throws SQLException,ClassNotFoundException{
		userDao.delete(user);
		return userDao.add(user);
	}
	
	public boolean add(User user) throws SQLException{
		return userDao.add(user);
	}

	public boolean delete(Integer id) throws SQLException,ClassNotFoundException{
		return userDao.delete(id);
	}
	
	public boolean delete(User user) throws SQLException,ClassNotFoundException{
		return userDao.delete(user);
	}
	
	
	public User login(String username, String password) throws SQLException,ClassNotFoundException{
		return userDao.login(username,password);
	}	
}
