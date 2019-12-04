package cn.edu.sdjzu.xg.bysj.dao;

import cn.edu.sdjzu.xg.bysj.domain.ProfTitle;
import util.JdbcHelper;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class ProfTitleDao {
	private static ProfTitleDao profTitleDao=new ProfTitleDao();
	private static Collection<ProfTitle> profTitles;
	public static ProfTitleDao getInstance(){
		return profTitleDao;
	}

	public Set<ProfTitle> findAll() throws SQLException,ClassNotFoundException{
		Set<ProfTitle> profTitles = new HashSet<ProfTitle>();
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from proftitle");
		//若结果集仍然有下一条记录，则执行循环体
		while (resultSet.next()){
			profTitles.add(new ProfTitle(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks")));
		}
		//关闭资源
		JdbcHelper.close(statement,connection);
		return profTitles;
	}

	public ProfTitle find(Integer id) throws SQLException,ClassNotFoundException{
		ProfTitle profTitle = null;
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建语句盒子对象
		PreparedStatement preparedStatement = connection.prepareStatement("select * from proftitle where id = ?");
		preparedStatement.setInt(1,id);
		ResultSet resultSet = preparedStatement.executeQuery();
		//若结果集仍然有下一条记录，则执行循环体
		if(resultSet.next()){
			profTitle = new ProfTitle(resultSet.getInt("id"),
					resultSet.getString("description"),
					resultSet.getString("no"),
					resultSet.getString("remarks"));
		}
		//执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
		JdbcHelper.close(preparedStatement,connection);
		return profTitle;
	}

	public boolean update(ProfTitle profTitle) throws SQLException,ClassNotFoundException{
		//创建数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement("update proftitle set description=?,no=?,remarks=? where id=?");
		//为预编译语句赋值
		preparedStatement.setString(1,profTitle.getDescription());
		preparedStatement.setString(2,profTitle.getNo());
		preparedStatement.setString(3,profTitle.getRemarks());
		preparedStatement.setInt(4,profTitle.getId());
		//执行预编译语句
		int affectedRowNum = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum >0;
	}

	public boolean add(ProfTitle profTitle) throws SQLException {
		//获得连接对象
		Connection connection = JdbcHelper.getConn();
		//根据连接对象准备语句对象，如果sql语句为多行，注意语句不同部分之间要有空格
		PreparedStatement preparedStatement = connection.prepareStatement("insert into Proftitle " +
				"(no, description,remarks)" +
				" values (?,?,?)");
		preparedStatement.setString(1,profTitle.getNo());
		preparedStatement.setString(2,profTitle.getDescription());
		preparedStatement.setString(3,profTitle.getRemarks());
		//执行预编译语句，用其返回值、影响的行为数为赋值affectedRowNum
		int affectedRowNum = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return affectedRowNum>0;
	}

	public boolean delete(Integer id) throws SQLException,ClassNotFoundException{
		//获取数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement("delete from proftitle where id=?");
		//为预编译语句赋值
		preparedStatement.setInt(1,id);
		int affectedRows = preparedStatement.executeUpdate();
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}

	public boolean delete(ProfTitle profTitle) throws ClassNotFoundException,SQLException{
		return delete(profTitle.getId());
	}

	public static void main(String[] args) throws SQLException,ClassNotFoundException{
		//输出原来信息
		ProfTitle profTitle1 = ProfTitleDao.getInstance().find(6);
		System.out.println(profTitle1.getDescription());
		profTitle1.setDescription("the best");
		ProfTitleDao.getInstance().update(profTitle1);
		ProfTitle profTitle2 = ProfTitleDao.getInstance().find(6);
		System.out.println(profTitle2.getDescription());

	}
}

