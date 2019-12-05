package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.User;
import cn.edu.sdjzu.xg.bysj.service.TeacherService;
import cn.edu.sdjzu.xg.bysj.service.UserService;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;


public final class UserDao {
	private static UserDao userDao=new UserDao();
	private UserDao(){}
	public static UserDao getInstance(){
		return userDao;
	}
	
	private static Collection<User> users;
	public Collection<User> findAll() throws SQLException,ClassNotFoundException{
	    //Users集合
		Collection<User> users = new HashSet<>();
        //获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
        //在该连接上创建语句盒子对象
		PreparedStatement preparedStatement = connection.prepareStatement("select * from user");
		//执行预编译语句对象
		ResultSet resultSet = preparedStatement.executeQuery();
        //若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			users.add(new User(resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					resultSet.getDate("loginTime"),
					TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
			));
		}
        //关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return users;
	}
	
	public User find(Integer id) throws SQLException,ClassNotFoundException {
		User desiredUser = null;
        //获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
        //在该连接上创建语句盒子对象
		PreparedStatement preparedStatement = connection.prepareStatement("select * from user where id =?");
		//为预编译语句赋值
		preparedStatement.setInt(1,id);
        //执行预编译语句对象
		ResultSet resultSet = preparedStatement.executeQuery();
        //若结果集仍然有下一条记录，则执行循环体
		if(resultSet.next()){
			desiredUser = new User(resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					resultSet.getDate("loginTime"),
					TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
			);
		}
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return desiredUser;
	}

	public User findByUsername (String username) throws SQLException,ClassNotFoundException{
		User user = null;
        //获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
        //在该连接上创建语句盒子对象
		PreparedStatement preparedStatement = connection.prepareStatement("select * from user where username=?");
        //为预编译语句赋值
		preparedStatement.setString(1,username);
        //执行预编译语句
		ResultSet resultSet = preparedStatement.executeQuery();
        //若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			user = new User(resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					resultSet.getDate("loginTime"),
					TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
			);
		}
		return  user;
	}

	//登录判断
	public User login(String username,String password) throws SQLException,ClassNotFoundException{
		User user = null;
        //获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
        //在该连接上创建语句盒子对象
		PreparedStatement preparedStatement = connection.prepareStatement("select * from user where username=? and password=?");
        //为预编译语句赋值
		preparedStatement.setString(1,username);
		preparedStatement.setString(2,password);
        //执行预编译语句
		ResultSet resultSet = preparedStatement.executeQuery();
        //若结果集仍然有下一条记录，则执行循环体
		if(resultSet.next()){
			user = new User(resultSet.getInt("id"),
					resultSet.getString("username"),
					resultSet.getString("password"),
					resultSet.getDate("loginTime"),
					TeacherService.getInstance().find(resultSet.getInt("teacher_id"))
            );
		}
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return user;
	}

	public boolean changePassword(User user) throws SQLException{
        //获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
        //在该连接上创建语句盒子对象
		PreparedStatement preparedStatement = connection.prepareStatement("update user set password = ? where id=?");
        //为预编译语句赋值
		preparedStatement.setString(1,user.getPassword());
		preparedStatement.setInt(2,user.getId());
        //执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
		int affectedRowNum = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum > 0;
	}

	public boolean update(User user){
		users.remove(user);
		return users.add(user);		
	}
	
	public boolean add(User user) throws SQLException{
        return users.add(user);
	}

	public boolean delete(Integer id) throws SQLException,ClassNotFoundException{
		User user = this.find(id);
		return this.delete(user);
	}
	
	public boolean delete(User user) throws SQLException,ClassNotFoundException{
		return delete(user.getId());
	}
	
	
	public static void main(String[] args) throws SQLException,ClassNotFoundException{
		User user = UserService.getInstance().login("28","28");
		System.out.println(user);
		User user1 = UserService.getInstance().find(10);
		System.out.println(user1);
		User user2 = UserService.getInstance().findByUsername("28");
		System.out.println(user2);
		userDao.delete(28);
	}

	private static void display(Collection<User> users) {
		for (User user : users) {
			System.out.println(user);
		}
	}
}
