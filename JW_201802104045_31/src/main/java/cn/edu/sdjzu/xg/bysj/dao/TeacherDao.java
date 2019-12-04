package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.*;
import cn.edu.sdjzu.xg.bysj.service.*;
import util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public final class TeacherDao {
	private static TeacherDao teacherDao=new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}
	public Set<Teacher> findAll() throws SQLException,ClassNotFoundException{
		Set<Teacher> teachers = new HashSet<Teacher>();
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		PreparedStatement preparedStatement = connection.prepareStatement("select * from teacher");
		ResultSet resultSet = preparedStatement.executeQuery();
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			teachers.add(new Teacher(resultSet.getInt("id"),
					resultSet.getString("name"),
					ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id")),
					DegreeService.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentService.getInstance().find(resultSet.getInt("department_id")),
					resultSet.getString("no")
			));
		}
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return teachers;
	}
	
	public Teacher find(Integer id) throws SQLException,ClassNotFoundException{
		Teacher teacher = null;
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		PreparedStatement preparedStatement = connection.prepareStatement("select * from teacher where id=?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//若结果集仍然有下一条记录，则执行循环体
		if(resultSet.next()){
			teacher = new Teacher(resultSet.getInt("id"),
					resultSet.getString("name"),
					ProfTitleService.getInstance().find(resultSet.getInt("profTitle_id")),
					DegreeService.getInstance().find(resultSet.getInt("degree_id")),
					DepartmentService.getInstance().find(resultSet.getInt("department_id")),
					resultSet.getString("no")
			);
		}
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return teacher;
	}

	public boolean update(Teacher teacher) throws SQLException{
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		PreparedStatement preparedStatement = connection.prepareStatement(
				"update teacher set name=?,proftitle_id=?,degree_id=?,department_id=?,no=? where id=?");
		//为预编译语句参数赋值
		preparedStatement.setString(1,teacher.getName());
		preparedStatement.setInt(2,teacher.getTitle().getId());
		preparedStatement.setInt(3,teacher.getDegree().getId());
		preparedStatement.setInt(4,teacher.getDepartment().getId());
		preparedStatement.setString(5,teacher.getNo());
		preparedStatement.setInt(6,teacher.getId());
		//执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
		int affectedRowNum = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum >0;
	}
	
	public boolean add(Teacher teacher) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = null;
		Date date = new Date();
		int affectedRowNum = 0;
		int teacher_id=0;
		try {
			//关闭连接的自动提交，事务开始
			connection.setAutoCommit(false);
			//在该连接上创建预编译语句对象
			preparedStatement = connection.prepareStatement("INSERT INTO teacher (no,name,profTitle_id,degree_id,department_id) VALUES" + " (?,?,?,?,?)");
			//为预编译参数赋值
			preparedStatement.setString(1, teacher.getNo());
			preparedStatement.setString(2, teacher.getName());
			preparedStatement.setInt(3, teacher.getTitle().getId());
			preparedStatement.setInt(4, teacher.getDegree().getId());
			preparedStatement.setInt(5, teacher.getDepartment().getId());
			//执行预编译语句，获取添加记录行数并赋值给affectedRowNum
			preparedStatement.executeUpdate();
			//在该连接上创建预编译语句对象
			preparedStatement = connection.prepareStatement("select * FROM teacher");
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				teacher_id = resultSet.getInt("id");
			}
			//在该连接上创建预编译语句对象
			preparedStatement = connection.prepareStatement("INSERT INTO user(username,password,loginTime,teacher_id) VALUES" + "(?,?,?,?)");
			//为预编译参数赋值
			preparedStatement.setString(1, teacher.getNo());
			preparedStatement.setString(2, teacher.getNo());
			preparedStatement.setDate(3, new java.sql.Date(date.getTime()));
			preparedStatement.setInt(4,teacher_id );
			preparedStatement.executeUpdate();
			//手动提交申请，事务结束
			connection.commit();
		} catch (SQLException e) {
			//若发生异常输出出错信息和错误码
			System.out.println(e.getMessage() + "\nErrorCode:" + e.getErrorCode());
			try {
				//如果连接不为空，则回滚到insert之前的状态
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException d) {
				d.printStackTrace();
			}
		} finally {//最终执行
			try {
				//如果连接不为空，重新开启自动提交
				if (connection != null) {
					connection.setAutoCommit(true);
				}
			} catch (SQLException f) {
				f.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(preparedStatement, connection);
			return affectedRowNum > 0;
		}
	}

	public boolean delete(Integer id) throws SQLException{
		Connection connection = JdbcHelper.getConn();
		PreparedStatement preparedStatement = null;
		int affectedRows =0;
		try {
			//关闭连接的自动提交，事务开始
			connection.setAutoCommit(false);
			//在该连接上创建预编译语句对象
			preparedStatement = connection.prepareStatement("DELETE FROM user WHERE teacher_id=?");
			//为预编译参数赋值
			preparedStatement.setInt(1, id);
			//关闭preparedStatement对象
			preparedStatement.executeUpdate();
			//在该连接上创建预编译语句对象
			preparedStatement = connection.prepareStatement("DELETE FROM teacher WHERE id=?");
			//为预编译参数赋值
			preparedStatement.setInt(1, id);
			//关闭preparedStatement对象
			affectedRows = preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			//若发生异常输出出错信息和错误码
			System.out.println(e.getMessage() + "\nErrorCode:" + e.getErrorCode());
			e.printStackTrace();
			try {
				//如果连接不为空，则回滚到insert之前的状态
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException d) {
				d.printStackTrace();
			}
		} finally {//最终执行
			try {
				//如果连接不为空，重新开启自动提交
				if (connection != null) {
					connection.setAutoCommit(true);
				}
			} catch (SQLException f) {
				f.printStackTrace();
			}
			//关闭资源
			JdbcHelper.close(preparedStatement, connection);
			return affectedRows > 0;
		}
	}
	
	public boolean delete(Teacher teacher) throws SQLException{
		return delete(teacher.getId());
	}

	public static void main(String[] args) throws SQLException,ClassNotFoundException{
		Degree degree = DegreeService.getInstance().find(1);
		ProfTitle profTitle = ProfTitleService.getInstance().find(6);
		Department department = DepartmentService.getInstance().find(8);
		Teacher teacherToAdd = new Teacher("王涛",profTitle,degree,department,"31");
		teacherDao.add(teacherToAdd);
		//teacherDao.delete(29);
	}
}
