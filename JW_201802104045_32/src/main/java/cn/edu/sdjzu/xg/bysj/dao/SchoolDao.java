package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.School;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class SchoolDao {
	private static SchoolDao schoolDao = new SchoolDao();
	private static Collection<School> schools;

	
	public static SchoolDao getInstance(){
		return schoolDao;
	}

	public Set<School> findAll() throws SQLException,ClassNotFoundException{
		Set<School> schools = new HashSet<School>();
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement stmt = connection.createStatement();
		ResultSet resultSet = stmt.executeQuery("select * from school");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			//以当前记录中的id,description,no,remarks值为参数，创建School对象
			schools.add(new School(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks")));
		}
		//执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
		JdbcHelper.close(stmt,connection);
		return schools;
	}

	public  School addWithSP(School school) throws SQLException,ClassNotFoundException{
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//根据连接对象准备可调用语句对象，sp_addSchool为存贮过程名称，后面为四个参数
		CallableStatement callableStatement = connection.prepareCall("{call sp_addSchool (?,?,?,?)}");
		//将第四个参数设为输出参数，类型为长整型（数据库的数据类型）
		callableStatement.registerOutParameter(4, Types.BIGINT);
		callableStatement.setString(1,school.getDescription());
		callableStatement.setString(2,school.getNo());
		callableStatement.setString(3,school.getRemarks());
		//执行可调用语句callableStatement
		callableStatement.execute();
		//获得第四个参数的值：数据库为该记录自动生成的id
		int id = callableStatement.getInt(4);
		//为参数school的id字段赋值
		school.setId(id);
		callableStatement.close();
		connection.close();
		return school;
	}
	public School find(Integer id) throws SQLException,ClassNotFoundException{
		School school = null;
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		PreparedStatement preparedStatement = connection.prepareStatement("select * from school where id = ?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//若结果集仍然有下一条记录，则执行循环体
		if (resultSet.next()){
			school = new School(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"));
		}
		//执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
		JdbcHelper.close(preparedStatement,connection);
		return school;
	}
	
	public boolean update(School school) throws SQLException,ClassNotFoundException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//根据连接对象准备语句对象
		PreparedStatement preparedStatement = connection.prepareStatement("update school set description=?,no=?,remarks=? where id=?");
		//为预编译语句赋值
		preparedStatement.setString(1,school.getDescription());
		preparedStatement.setString(2,school.getNo());
		preparedStatement.setString(3,school.getRemarks());
		preparedStatement.setInt(4,school.getId());
		//执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
		int affectedRowNum = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum >0;
	}
	
	public boolean add(School school) throws SQLException,ClassNotFoundException{
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//根据连接对象准备语句对象，如果sql语句为多行，注意语句不同部分之间要有空格
		PreparedStatement pstmt = connection.prepareStatement("insert into school" +
				"(no,description,remarks)" +
				" values (?,?,?)");
		pstmt.setString(1, school.getNo());
		pstmt.setString(2, school.getDescription());
		pstmt.setString(3, school.getRemarks());
		int affectedRowNum = pstmt.executeUpdate();
		//执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
		JdbcHelper.close(pstmt, connection);
		return affectedRowNum > 0;
	}

	public boolean delete(Integer id) throws SQLException,ClassNotFoundException{
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//创建sql语句
		String deleteSchool_sql = "DELETE FROM school" + " WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement pstmt = connection.prepareStatement(deleteSchool_sql);
		//为预编译语句赋值
		pstmt.setInt(1,id);
		int affectedRows = pstmt.executeUpdate();
		JdbcHelper.close(pstmt,connection);
		return affectedRows>0;
	}
	
	public boolean delete(School school) throws SQLException,ClassNotFoundException{
		return delete(school.getId());
	}

	public static void main(String[] args) throws SQLException,ClassNotFoundException{
		//获得对应的school对象
		School school1 = SchoolDao.getInstance().find(1);
		//读出数据库中的原来的信息
		System.out.println(school1.getDescription());
		//修改
		school1.setDescription("管理");
		//更新
		SchoolDao.getInstance().update(school1);
		//找出更新对象
		School school2 = SchoolDao.getInstance().find(1);
		//输出更新对象的description
		System.out.println(school2.getDescription());
	}
}
